package com.ars.orderservice.repository;

import com.ars.orderservice.dto.request.RevenueReportFilter;
import com.ars.orderservice.dto.request.SearchOrderRequestDTO;
import com.ars.orderservice.dto.response.OrderDTO;
import com.ars.orderservice.dto.response.RevenueReportDTO;
import org.springframework.data.domain.Page;

public interface OrderRepositoryCustom {
    Page<OrderDTO> getOrderWithPaging(SearchOrderRequestDTO requestDTO);
    Page<RevenueReportDTO> getRevenueReport(RevenueReportFilter requestDTO);
}
