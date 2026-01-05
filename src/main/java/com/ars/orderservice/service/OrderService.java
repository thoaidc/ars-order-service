package com.ars.orderservice.service;

import com.ars.orderservice.dto.request.OrderRequestDTO;
import com.ars.orderservice.dto.request.SearchOrderRequestDTO;
import com.dct.model.dto.response.BaseResponseDTO;
import com.dct.model.event.PaymentFailureEvent;
import com.dct.model.event.PaymentSuccessEvent;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface OrderService {
    BaseResponseDTO createOrder(OrderRequestDTO requestDTO);
    ResponseEntity<Resource> getOrderProductFile(Integer orderProductId);
    BaseResponseDTO saveDesignFile(Integer orderProductId, MultipartFile file);
    BaseResponseDTO getOrderWithPaging(SearchOrderRequestDTO requestDTO);
    BaseResponseDTO getOrderWithPagingForUser(SearchOrderRequestDTO requestDTO);
    BaseResponseDTO getOrderWithPagingForShop(SearchOrderRequestDTO requestDTO);
    BaseResponseDTO getOrderDetail(Integer orderId);
    BaseResponseDTO getOrderDetailForUser(Integer orderId);
    BaseResponseDTO getOrderDetailForShop(Integer orderId);
    BaseResponseDTO getTotalOrderToday(boolean forAdmin);
    BaseResponseDTO getTotalOrderSalesLastSevenDay(boolean forAdmin);
    void orderCompletion(PaymentSuccessEvent paymentSuccessEvent);
    void cancelOrder(PaymentFailureEvent paymentFailureEvent);
}
