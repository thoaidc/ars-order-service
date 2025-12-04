package com.ars.orderservice.service;

import com.ars.orderservice.dto.request.OrderRequestDTO;
import com.ars.orderservice.dto.request.SearchOrderForUserRequestDTO;
import com.ars.orderservice.dto.request.SearchOrderRequestDTO;
import com.dct.model.dto.response.BaseResponseDTO;
import com.dct.model.event.PaymentFailureEvent;
import com.dct.model.event.PaymentSuccessEvent;

public interface OrderService {
    BaseResponseDTO createOrder(OrderRequestDTO requestDTO);
    BaseResponseDTO getOrderWithPaging(SearchOrderRequestDTO requestDTO);
    BaseResponseDTO getOrderWithPagingForUser(SearchOrderForUserRequestDTO requestDTO);
    BaseResponseDTO getOrderDetail(Integer orderId);
    BaseResponseDTO getOrderDetailForUser(Integer orderId);
    void orderCompletion(PaymentSuccessEvent paymentSuccessEvent);
    void cancelOrder(PaymentFailureEvent paymentFailureEvent);
}
