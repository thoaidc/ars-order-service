package com.ars.orderservice.service.impl;

import com.ars.orderservice.constants.OrderConstants;
import com.ars.orderservice.dto.request.OrderRequestDTO;
import com.ars.orderservice.dto.request.SearchOrderForUserRequestDTO;
import com.ars.orderservice.dto.request.SearchOrderRequestDTO;
import com.ars.orderservice.repository.OrderRepository;
import com.ars.orderservice.service.OrderService;
import com.dct.model.dto.response.BaseResponseDTO;
import com.dct.model.event.PaymentFailureEvent;
import com.dct.model.event.PaymentSuccessEvent;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional
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
