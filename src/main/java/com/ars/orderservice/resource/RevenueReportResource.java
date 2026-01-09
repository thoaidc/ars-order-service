package com.ars.orderservice.resource;

import com.ars.orderservice.dto.request.RevenueReportFilter;
import com.ars.orderservice.service.OrderService;
import com.dct.model.dto.response.BaseResponseDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/internal/orders")
public class RevenueReportResource {
    private final OrderService orderService;

    public RevenueReportResource(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/reports/revenues")
    public BaseResponseDTO getRevenueReport(@ModelAttribute RevenueReportFilter requestDTO) {
        return orderService.getRevenueReportInternal(requestDTO);
    }
}
