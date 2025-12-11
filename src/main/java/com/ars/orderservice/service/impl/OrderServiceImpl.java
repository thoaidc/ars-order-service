package com.ars.orderservice.service.impl;

import com.ars.orderservice.constants.OrderConstants;
import com.ars.orderservice.dto.request.CheckOrderInfoRequestDTO;
import com.ars.orderservice.dto.request.OrderRequestDTO;
import com.ars.orderservice.dto.request.SearchOrderForUserRequestDTO;
import com.ars.orderservice.dto.request.SearchOrderRequestDTO;
import com.ars.orderservice.dto.response.CheckOrderInfoResponseDTO;
import com.ars.orderservice.entity.Order;
import com.ars.orderservice.entity.OrderProduct;
import com.ars.orderservice.entity.OutBox;
import com.ars.orderservice.entity.SubOrder;
import com.ars.orderservice.repository.OrderRepository;
import com.ars.orderservice.repository.OutBoxRepository;
import com.ars.orderservice.service.OrderService;
import com.dct.config.common.HttpClientUtils;
import com.dct.model.common.DateUtils;
import com.dct.model.common.JsonUtils;
import com.dct.model.constants.BaseDatetimeConstants;
import com.dct.model.constants.BaseOutBoxConstants;
import com.dct.model.dto.response.BaseResponseDTO;
import com.dct.model.event.OrderCreatedEvent;
import com.dct.model.event.PaymentFailureEvent;
import com.dct.model.event.PaymentSuccessEvent;
import com.dct.model.exception.BaseBadRequestException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);
    private static final String ENTITY_NAME = "com.ars.orderservice.service.impl.OrderServiceImpl";
    private final RestTemplate restTemplate;
    private final OrderRepository orderRepository;
    private final OutBoxRepository outBoxRepository;

    public OrderServiceImpl(RestTemplate restTemplate,
                            OrderRepository orderRepository,
                            OutBoxRepository outBoxRepository) {
        this.restTemplate = restTemplate;
        this.orderRepository = orderRepository;
        this.outBoxRepository = outBoxRepository;
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
        orderCreatedEvent.setSagaId(UUID.randomUUID().toString());
        OutBox outBox = new OutBox();
        outBox.setSagaId(orderCreatedEvent.getSagaId());
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

        CheckOrderInfoResponseDTO checkOrderInfoResponse = HttpClientUtils.builder()
                .restTemplate(restTemplate)
                .url("http://localhost:8002/api/internal/products/check-order-info")
                .method(HttpMethod.POST)
                .body(checkOrderInfoRequest)
                .execute(CheckOrderInfoResponseDTO.class);

        if (Objects.isNull(checkOrderInfoResponse)) {
            throw new BaseBadRequestException(ENTITY_NAME, "Invalid order request, not found products or vouchers info");
        }

        checkProductsInfo(checkOrderInfoResponse.getProducts());
        checkVouchersInfo(checkOrderInfoResponse.getVouchers());
        return checkOrderInfoResponse;
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

    private void checkProductsInfo(List<CheckOrderInfoResponseDTO.ProductDTO> productDTOS) {
        productDTOS.forEach(productDTO -> {
            if (!Objects.equals(productDTO.getStatus(), "ACTIVE")) {
                throw new BaseBadRequestException(ENTITY_NAME, "Invalid product info, this product is not available");
            }
        });
    }

    private void checkVouchersInfo(List<CheckOrderInfoResponseDTO.VoucherDTO> voucherDTOS) {
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
        return null;
    }

    @Override
    public BaseResponseDTO getOrderWithPagingForUser(SearchOrderForUserRequestDTO requestDTO) {
        return null;
    }

    @Override
    public BaseResponseDTO getOrderDetail(Integer orderId) {
        return null;
    }

    @Override
    public BaseResponseDTO getOrderDetailForUser(Integer orderId) {
        return null;
    }

    @Override
    @Transactional
    public void orderCompletion(PaymentSuccessEvent paymentSuccessEvent) {
        orderRepository.updateOrderStatusById(paymentSuccessEvent.getOrderId(), OrderConstants.Status.COMPLETED);
    }

    @Override
    @Transactional
    public void cancelOrder(PaymentFailureEvent paymentFailureEvent) {
        orderRepository.updateOrderStatusById(paymentFailureEvent.getOrderId(), OrderConstants.Status.FAILED);
    }
}
