package com.ars.orderservice.service.impl;

import com.ars.orderservice.constants.OrderConstants;
import com.ars.orderservice.dto.mapping.OrderProductResponse;
import com.ars.orderservice.dto.mapping.OrderResponse;
import com.ars.orderservice.dto.request.CheckOrderInfoRequestDTO;
import com.ars.orderservice.dto.request.OrderRequestDTO;
import com.ars.orderservice.dto.request.SearchOrderRequestDTO;
import com.ars.orderservice.dto.response.CheckOrderInfoResponseDTO;
import com.ars.orderservice.dto.response.OrderDTO;
import com.ars.orderservice.dto.response.OrderDetailDTO;
import com.ars.orderservice.dto.response.SubOrderDetailDTO;
import com.ars.orderservice.entity.Order;
import com.ars.orderservice.entity.OrderProduct;
import com.ars.orderservice.entity.OutBox;
import com.ars.orderservice.entity.SubOrder;
import com.ars.orderservice.repository.OrderProductRepository;
import com.ars.orderservice.repository.OrderRepository;
import com.ars.orderservice.repository.OutBoxRepository;
import com.ars.orderservice.repository.SubOrderRepository;
import com.ars.orderservice.service.OrderService;
import com.dct.config.common.HttpClientUtils;

import com.dct.model.common.DateUtils;
import com.dct.model.common.JsonUtils;
import com.dct.model.constants.BaseDatetimeConstants;
import com.dct.model.constants.BaseOutBoxConstants;
import com.dct.model.constants.BasePaymentConstants;
import com.dct.model.dto.response.BaseResponseDTO;
import com.dct.model.event.ChangeBalanceAmountEvent;
import com.dct.model.event.OrderCreatedEvent;
import com.dct.model.event.PaymentFailureEvent;
import com.dct.model.event.PaymentSuccessEvent;
import com.dct.model.exception.BaseBadRequestException;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private static final String ENTITY_NAME = "com.ars.orderservice.service.impl.OrderServiceImpl";
    private final RestTemplate restTemplate;
    private final OrderRepository orderRepository;
    private final OutBoxRepository outBoxRepository;
    private final SubOrderRepository subOrderRepository;
    private final OrderProductRepository orderProductRepository;

    public OrderServiceImpl(RestTemplate restTemplate,
                            OrderRepository orderRepository,
                            OutBoxRepository outBoxRepository,
                            SubOrderRepository subOrderRepository,
                            OrderProductRepository orderProductRepository) {
        this.restTemplate = restTemplate;
        this.orderRepository = orderRepository;
        this.outBoxRepository = outBoxRepository;
        this.subOrderRepository = subOrderRepository;
        this.orderProductRepository = orderProductRepository;
    }

    @Override
    @Transactional
    public BaseResponseDTO createOrder(OrderRequestDTO requestDTO) {
        Order order = new Order();
        order.setCustomerId(requestDTO.getCustomerId());
        order.setCustomerName(requestDTO.getCustomerName());
        order.setPaymentMethod(requestDTO.getPaymentMethod());
        order.setPaymentStatus(OrderConstants.PaymentStatus.UNPAID);
        order.setQuantity(requestDTO.getProducts().size());
        order.setStatus(OrderConstants.Status.PENDING);
        CheckOrderInfoResponseDTO checkOrderInfoResponse = validateOrderInfo(requestDTO);
        calculateOrderAmount(order, checkOrderInfoResponse);
        Map<Integer, OrderProduct> orderProductMap = checkOrderInfoResponse.getProducts()
                .stream()
                .collect(Collectors.toMap(CheckOrderInfoResponseDTO.ProductDTO::getId, product -> {
                    OrderProduct orderProduct = new OrderProduct();
                    orderProduct.setProductCode(product.getCode());
                    orderProduct.setProductName(product.getName());
                    orderProduct.setTotalAmount(product.getPrice());
                    orderProduct.setProductThumbnail(product.getThumbnailUrl());
                    return orderProduct;
                }));
        Map<Integer, List<OrderRequestDTO.OrderProduct>> orderRequestMap = requestDTO.getProducts().stream()
                .collect(Collectors.groupingBy(OrderRequestDTO.OrderProduct::getShopId));
        List<SubOrder> subOrders = new ArrayList<>();

        for (Map.Entry<Integer, List<OrderRequestDTO.OrderProduct>> entry : orderRequestMap.entrySet()) {
            SubOrder subOrder = new SubOrder();
            BeanUtils.copyProperties(order, subOrder, "id", "subOrders", "amount", "discount", "totalAmount");
            subOrder.setOrder(order);
            subOrder.setCode(UUID.randomUUID().toString());
            subOrder.setShopId(entry.getKey());
            mapSubOrderRequest(subOrder, entry.getValue(), orderProductMap);
            calculateSubOrderAmount(subOrder, checkOrderInfoResponse);
            subOrders.add(subOrder);
        }

        order.setSubOrders(subOrders);
        order.setCode(UUID.randomUUID().toString());
        orderRepository.save(order);
        saveOutboxEvent(order, requestDTO);
        return BaseResponseDTO.builder().ok(order.getId());
    }

    private void saveOutboxEvent(Order order, OrderRequestDTO request) {
        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent();
        orderCreatedEvent.setOrderId(order.getId());
        orderCreatedEvent.setUserId(request.getCustomerId());
        orderCreatedEvent.setAmount(order.getAmount());
        orderCreatedEvent.setPaymentMethod(order.getPaymentMethod());
        OutBox outBox = new OutBox();
        outBox.setRefId(order.getId());
        outBox.setType(BaseOutBoxConstants.Type.ORDER_CREATED);
        outBox.setStatus(BaseOutBoxConstants.Status.PENDING);
        outBox.setValue(JsonUtils.toJsonString(orderCreatedEvent));
        outBoxRepository.save(outBox);
    }

    private CheckOrderInfoResponseDTO validateOrderInfo(OrderRequestDTO requestDTO) {
        CheckOrderInfoRequestDTO checkOrderInfoRequest = new CheckOrderInfoRequestDTO();
        Set<Integer> productIds = requestDTO.getProducts()
                .stream()
                .map(OrderRequestDTO.OrderProduct::getProductId)
                .collect(Collectors.toSet());
        checkOrderInfoRequest.setVoucherIds(requestDTO.getVoucherIds());
        checkOrderInfoRequest.setProductIds(productIds);
        BaseResponseDTO checkOrderInfoResponse = HttpClientUtils.builder()
                .restTemplate(restTemplate)
                .url("http://localhost:8002/api/internal/products/check-order-info")
                .method(HttpMethod.POST)
                .body(checkOrderInfoRequest)
                .execute(BaseResponseDTO.class);

        if (Objects.isNull(checkOrderInfoResponse)) {
            throw new BaseBadRequestException(ENTITY_NAME, "Invalid order request, not found products or vouchers info");
        }

        CheckOrderInfoResponseDTO checkOrderInfoResult = JsonUtils.convertValue(
            checkOrderInfoResponse.getResult(),
            CheckOrderInfoResponseDTO.class
        );
        checkProductsInfo(checkOrderInfoResult.getProducts(), productIds.size());
        checkVouchersInfo(checkOrderInfoResult.getVouchers(), requestDTO.getVoucherIds().size());
        return checkOrderInfoResult;
    }

    private void calculateOrderAmount(Order order, CheckOrderInfoResponseDTO checkOrderInfo) {
        BigDecimal amount = checkOrderInfo.getProducts()
                .stream()
                .map(CheckOrderInfoResponseDTO.ProductDTO::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setAmount(amount);
        BigDecimal totalDiscount = BigDecimal.ZERO;

        for (CheckOrderInfoResponseDTO.VoucherDTO voucher : checkOrderInfo.getVouchers()) {
            totalDiscount = totalDiscount.add(calculateDiscount(voucher, order.getAmount()));
        }

        if (totalDiscount.compareTo(order.getAmount()) > 0) {
            totalDiscount = order.getAmount();
        }

        order.setDiscount(totalDiscount);
        order.setTotalAmount(order.getAmount().subtract(order.getDiscount()));
    }

    private void calculateSubOrderAmount(SubOrder subOrder, CheckOrderInfoResponseDTO checkOrderInfo) {
        BigDecimal amount = subOrder.getProducts()
                .stream()
                .map(OrderProduct::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        subOrder.setAmount(amount);
        BigDecimal totalDiscount = BigDecimal.ZERO;

        for (CheckOrderInfoResponseDTO.VoucherDTO voucher : checkOrderInfo.getVouchers()) {
            if (Objects.equals(voucher.getShopId(), subOrder.getShopId())) {
                totalDiscount = totalDiscount.add(calculateDiscount(voucher, subOrder.getAmount()));
            }
        }

        if (totalDiscount.compareTo(subOrder.getAmount()) > 0) {
            totalDiscount = subOrder.getAmount();
        }

        subOrder.setDiscount(totalDiscount);
        subOrder.setTotalAmount(subOrder.getAmount().subtract(subOrder.getDiscount()));
    }

    private BigDecimal calculateDiscount(CheckOrderInfoResponseDTO.VoucherDTO voucher, BigDecimal orderAmount) {
        BigDecimal voucherValue = voucher.getValue();

        if (Objects.equals(voucher.getType(), 2)) {
            return orderAmount.multiply(voucherValue).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        }

        return voucherValue;
    }

    private void checkProductsInfo(List<CheckOrderInfoResponseDTO.ProductDTO> productDTOS, int totalItems) {
        if (Objects.isNull(productDTOS) || productDTOS.size() != totalItems) {
            throw new BaseBadRequestException(ENTITY_NAME, "Products not found");
        }

        productDTOS.forEach(productDTO -> {
            if (!Objects.equals(productDTO.getStatus(), "ACTIVE")) {
                throw new BaseBadRequestException(ENTITY_NAME, "Invalid product info, this product is not available");
            }
        });
    }

    private void checkVouchersInfo(List<CheckOrderInfoResponseDTO.VoucherDTO> voucherDTOS, int totalItems) {
        if (Objects.isNull(voucherDTOS) || voucherDTOS.size() != totalItems) {
            throw new BaseBadRequestException(ENTITY_NAME, "Vouchers not found");
        }

        String nowStr = DateUtils.now().toString(BaseDatetimeConstants.Formatter.YYYY_MM_DD_NORMALIZED);
        int now = Integer.parseInt(nowStr);

        voucherDTOS.forEach(voucherDTO -> {
            if (!Objects.equals(voucherDTO.getStatus(), 1)) {
                throw new BaseBadRequestException(ENTITY_NAME, "Invalid voucher info, this voucher is not available");
            }

            if (voucherDTO.getDateExpired() < now || voucherDTO.getDateStarted() > now) {
                throw new BaseBadRequestException(ENTITY_NAME, "Voucher does not available in this time");
            }
        });
    }

    private void mapSubOrderRequest(SubOrder subOrder,
                                    List<OrderRequestDTO.OrderProduct> orderProductRequests,
                                    Map<Integer, OrderProduct> productMap) {
        List<OrderProduct> orderProducts = orderProductRequests.stream()
                .map(product -> {
                    OrderProduct orderProduct = productMap.get(product.getProductId());

                    if (Objects.isNull(orderProduct)) {
                        throw new BaseBadRequestException(ENTITY_NAME, "Product not found!");
                    }

                    orderProduct.setSubOrder(subOrder);
                    orderProduct.setShopId(product.getShopId());
                    orderProduct.setProductId(product.getProductId());
                    orderProduct.setNote(product.getNote());
                    orderProduct.setData(product.getData());
                    return orderProduct;
                }).toList();
        subOrder.setProducts(orderProducts);
    }

    @Override
    public BaseResponseDTO getOrderWithPaging(SearchOrderRequestDTO requestDTO) {
        Page<OrderDTO> orderPage = orderRepository.getOrderWithPaging(requestDTO);
        return BaseResponseDTO.builder().total(orderPage.getTotalElements()).ok(orderPage.getContent());
    }

    @Override
    public BaseResponseDTO getOrderWithPagingForUser(SearchOrderRequestDTO request) {
        Page<OrderResponse> page = orderRepository.getOrderWithPagingForUser(request.getUserId(), request.getPageable());
        return BaseResponseDTO.builder().total(page.getTotalElements()).ok(page.getContent());
    }

    @Override
    public BaseResponseDTO getOrderWithPagingForShop(SearchOrderRequestDTO requestDTO) {
        Page<OrderDTO> orderPage = subOrderRepository.getSubOrderWithPaging(requestDTO);
        return BaseResponseDTO.builder().total(orderPage.getTotalElements()).ok(orderPage.getContent());
    }

    @Override
    public BaseResponseDTO getOrderDetail(Integer orderId) {
        Optional<Order> orderOptional = orderRepository.findByIdWithSubOrders(orderId);

        if (orderOptional.isEmpty()) {
            throw new BaseBadRequestException(ENTITY_NAME, "Order not found - ID: " + orderId);
        }

        Order order = orderOptional.get();
        OrderDetailDTO orderDetail = new OrderDetailDTO();
        BeanUtils.copyProperties(order, orderDetail, "subOrders");
        List<OrderProductResponse> orderProductResponses = orderProductRepository.findAllOrderProductByOrderId(orderId);
        MultiValueMap<Integer, OrderDTO.OrderProductDTO> orderProductsMap = new LinkedMultiValueMap<>();
        List<OrderDTO.OrderProductDTO> orderProducts = orderProductResponses.stream()
                .map(orderProductResponse -> {
                    OrderDTO.OrderProductDTO orderProductDTO = new OrderDTO.OrderProductDTO();
                    BeanUtils.copyProperties(orderProductResponse, orderProductDTO);
                    orderProductsMap.add(orderProductDTO.getSubOrderId(), orderProductDTO);
                    return orderProductDTO;
                }).toList();
        orderDetail.setProducts(orderProducts);
        List<SubOrderDetailDTO> subOrders = order.getSubOrders().stream()
                .map(subOrder -> {
                    SubOrderDetailDTO subOrderDetailDTO = new SubOrderDetailDTO();
                    BeanUtils.copyProperties(subOrder, subOrderDetailDTO, "order", "products");
                    subOrderDetailDTO.setOrderId(orderId);
                    subOrderDetailDTO.setProducts(orderProductsMap.get(subOrder.getId()));
                    return subOrderDetailDTO;
                }).toList();
        orderDetail.setSubOrders(subOrders);
        return BaseResponseDTO.builder().ok(orderDetail);
    }

    @Override
    public BaseResponseDTO getOrderDetailForUser(Integer orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);

        if (orderOptional.isEmpty()) {
            throw new BaseBadRequestException(ENTITY_NAME, "Order not found - ID: " + orderId);
        }

        Order order = orderOptional.get();
        OrderDetailDTO orderDetail = new OrderDetailDTO();
        BeanUtils.copyProperties(order, orderDetail, "subOrders");
        List<OrderProductResponse> orderProductResponses = orderProductRepository.findAllOrderProductByOrderId(orderId);
        List<OrderDTO.OrderProductDTO> orderProducts = orderProductResponses.stream()
                .map(orderProductResponse -> {
                    OrderDTO.OrderProductDTO orderProductDTO = new OrderDTO.OrderProductDTO();
                    BeanUtils.copyProperties(orderProductResponse, orderProductDTO);
                    return orderProductDTO;
                }).toList();
        orderDetail.setProducts(orderProducts);
        orderDetail.setOrderDate(DateUtils.ofInstant(order.getCreatedDate()).toString());
        return BaseResponseDTO.builder().ok(orderDetail);
    }

    @Override
    public BaseResponseDTO getOrderDetailForShop(Integer orderId) {
        Optional<SubOrder> subOrderOptional = subOrderRepository.findById(orderId);

        if (subOrderOptional.isEmpty()) {
            throw new BaseBadRequestException(ENTITY_NAME, "Order not found - ID: " + orderId);
        }

        SubOrder subOrder = subOrderOptional.get();
        OrderDetailDTO subOrderDetail = new OrderDetailDTO();
        BeanUtils.copyProperties(subOrder, subOrderDetail, "order", "products");
        List<OrderProductResponse> orderProductResponses = orderProductRepository.findAllOrderProductBySubOrderId(orderId);
        List<OrderDTO.OrderProductDTO> subOrderProducts = orderProductResponses.stream()
                .map(orderProductResponse -> {
                    OrderDTO.OrderProductDTO orderProductDTO = new OrderDTO.OrderProductDTO();
                    BeanUtils.copyProperties(orderProductResponse, orderProductDTO);
                    return orderProductDTO;
                }).toList();
        subOrderDetail.setOrderDate(DateUtils.ofInstant(subOrder.getCreatedDate()).toString());
        subOrderDetail.setProducts(subOrderProducts);
        return BaseResponseDTO.builder().ok(subOrderDetail);
    }

    @Override
    @Transactional
    public void orderCompletion(PaymentSuccessEvent paymentSuccessEvent) {
        Optional<Order> orderOptional = orderRepository.findById(paymentSuccessEvent.getOrderId());

        if (orderOptional.isEmpty()) {
            throw new BaseBadRequestException(ENTITY_NAME, "Order not found - ID: " + paymentSuccessEvent.getOrderId());
        }

        Order order = orderOptional.get();
        order.setStatus(OrderConstants.Status.COMPLETED);
        order.setPaymentStatus(OrderConstants.PaymentStatus.PAID);
        order.getSubOrders().forEach(subOrder -> {
            subOrder.setStatus(OrderConstants.Status.COMPLETED);
            subOrder.setPaymentStatus(OrderConstants.PaymentStatus.PAID);
        });
        updateBalanceForShops(order.getSubOrders());
        orderRepository.save(order);
    }

    private void updateBalanceForShops(List<SubOrder> subOrders) {
        List<OutBox> outBoxes = new ArrayList<>();

        subOrders.forEach(subOrder -> {
            BigDecimal platformFeeAmount = subOrder.getTotalAmount().multiply(OrderConstants.PLATFORM_FEE_FACTOR);
            platformFeeAmount = platformFeeAmount.setScale(OrderConstants.SCALE_NUMBER, RoundingMode.HALF_UP);
            outBoxes.add(createChangeBalanceOutbox(subOrder, platformFeeAmount, BasePaymentConstants.BalanceType.SHOP));
            outBoxes.add(createChangeBalanceOutbox(subOrder, platformFeeAmount, BasePaymentConstants.BalanceType.SYSTEM));
        });

        outBoxRepository.saveAll(outBoxes);
    }

    private OutBox createChangeBalanceOutbox(SubOrder order, BigDecimal platformFeeAmount, Integer type) {
        ChangeBalanceAmountEvent changeBalanceAmountEvent = new ChangeBalanceAmountEvent();

        switch (type) {
            case BasePaymentConstants.BalanceType.SHOP:
                changeBalanceAmountEvent.setAmount(order.getTotalAmount().subtract(platformFeeAmount));
                changeBalanceAmountEvent.setReceiverId(order.getShopId());
                break;
            case BasePaymentConstants.BalanceType.SYSTEM:
                changeBalanceAmountEvent.setAmount(platformFeeAmount);
                changeBalanceAmountEvent.setReceiverId(OrderConstants.SYSTEM_ACCOUNT_ID);
        }

        changeBalanceAmountEvent.setRefId(order.getId());
        changeBalanceAmountEvent.setType(type);
        changeBalanceAmountEvent.setDescription("Cong tien don hang: " + order.getCode());
        OutBox changeBalanceAmountOutBoxEvent = new OutBox();
        changeBalanceAmountOutBoxEvent.setRefId(changeBalanceAmountEvent.getRefId());
        changeBalanceAmountOutBoxEvent.setType(BaseOutBoxConstants.Type.CHANGE_BALANCE_AMOUNT);
        changeBalanceAmountOutBoxEvent.setStatus(BaseOutBoxConstants.Status.PENDING);
        changeBalanceAmountOutBoxEvent.setValue(JsonUtils.toJsonString(changeBalanceAmountEvent));
        return changeBalanceAmountOutBoxEvent;
    }

    @Override
    @Transactional
    public void cancelOrder(PaymentFailureEvent paymentFailureEvent) {
        Optional<Order> orderOptional = orderRepository.findById(paymentFailureEvent.getOrderId());

        if (orderOptional.isEmpty()) {
            throw new BaseBadRequestException(ENTITY_NAME, "Order not found - ID: " + paymentFailureEvent.getOrderId());
        }

        Order order = orderOptional.get();
        order.setStatus(OrderConstants.Status.FAILED);
        order.getSubOrders().forEach(subOrder -> subOrder.setStatus(OrderConstants.Status.FAILED));
        orderRepository.save(order);
    }
}
