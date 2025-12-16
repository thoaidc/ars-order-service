package com.ars.orderservice.resource;

import com.ars.orderservice.service.OrderService;
import com.dct.model.dto.response.BaseResponseDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/internal/orders")
public class InternalOrderResource {
    private final OrderService orderService;

    public InternalOrderResource(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/shop-ids/{orderId}")
    public BaseResponseDTO getShopIdsByOrderId(@PathVariable Integer orderId) {
        return orderService.getShopIdsByOrderId(orderId);
    }
}
