package com.ars.orderservice.repository;

import com.ars.orderservice.dto.request.SearchOrderRequestDTO;
import com.ars.orderservice.dto.response.OrderDTO;
import org.springframework.data.domain.Page;

public interface OrderRepositoryCustom {
    Page<OrderDTO> getOrderWithPaging(SearchOrderRequestDTO requestDTO);
}
