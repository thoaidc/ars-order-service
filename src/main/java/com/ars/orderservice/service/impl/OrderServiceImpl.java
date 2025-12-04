package com.ars.orderservice.service.impl;

import com.ars.orderservice.dto.request.OrderRequestDTO;
import com.ars.orderservice.dto.request.SearchOrderForUserRequestDTO;
import com.ars.orderservice.dto.request.SearchOrderRequestDTO;
import com.ars.orderservice.repository.OrderRepository;
import com.ars.orderservice.service.OrderService;
import com.dct.model.dto.response.BaseResponseDTO;
import com.dct.model.event.PaymentFailureEvent;
import com.dct.model.event.PaymentSuccessEvent;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public BaseResponseDTO createOrder(OrderRequestDTO requestDTO) {
        return null;
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
    public void orderCompletion(PaymentSuccessEvent paymentSuccessEvent) {

    }

    @Override
    public void cancelOrder(PaymentFailureEvent paymentFailureEvent) {

    }
}
