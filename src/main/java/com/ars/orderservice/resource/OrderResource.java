package com.ars.orderservice.resource;

import com.ars.orderservice.dto.request.OrderRequestDTO;
import com.ars.orderservice.dto.request.SearchOrderRequestDTO;
import com.ars.orderservice.service.OrderService;
import com.dct.model.dto.response.BaseResponseDTO;

import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderResource {
    private final OrderService orderService;

    public OrderResource(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/products/files/download/{orderProductId}")
    public ResponseEntity<Resource> getOrderProductDesignFile(@PathVariable Integer orderProductId) {
        return orderService.getOrderProductFile(orderProductId);
    }

    @PostMapping(value = "/products/files/uploads", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponseDTO uploadDesign(
        @RequestParam("file") MultipartFile file,
        @RequestParam("orderProductId") Integer orderProductId
    ) {
        return orderService.saveDesignFile(orderProductId, file);
    }

    @GetMapping
    public BaseResponseDTO getOrderWithPaging(@ModelAttribute SearchOrderRequestDTO requestDTO) {
        return orderService.getOrderWithPaging(requestDTO);
    }

    @GetMapping("/{orderId}")
    public BaseResponseDTO getOrderDetail(@PathVariable Integer orderId) {
        return orderService.getOrderDetail(orderId);
    }

    @GetMapping("/by-user")
    public BaseResponseDTO getOrderWithPagingForUser(@ModelAttribute SearchOrderRequestDTO requestDTO) {
        return orderService.getOrderWithPagingForUser(requestDTO);
    }

    @GetMapping("/by-user/{orderId}")
    public BaseResponseDTO getOrderDetailForUser(@PathVariable Integer orderId) {
        return orderService.getOrderDetailForUser(orderId);
    }

    @GetMapping("/by-shop")
    public BaseResponseDTO getOrderWithPagingForShop(@ModelAttribute SearchOrderRequestDTO requestDTO) {
        return orderService.getOrderWithPagingForShop(requestDTO);
    }

    @GetMapping("/by-shop/{orderId}")
    public BaseResponseDTO getOrderDetailForShop(@PathVariable Integer orderId) {
        return orderService.getOrderDetailForShop(orderId);
    }

    @GetMapping("/today")
    public BaseResponseDTO getTotalOrderTodayForAdmin() {
        return orderService.getTotalOrderToday(true);
    }

    @GetMapping("/today/by-shop")
    public BaseResponseDTO getTotalOrderTodayForShop() {
        return orderService.getTotalOrderToday(false);
    }

    @GetMapping("/last-seven-day/by-shop")
    public BaseResponseDTO getTotalOrdersLastSevenDayForShop() {
        return orderService.getTotalOrderSalesLastSevenDay(false);
    }

    @GetMapping("/last-seven-day/by-admin")
    public BaseResponseDTO getTotalOrdersLastSevenDayForAdmin() {
        return orderService.getTotalOrderSalesLastSevenDay(true);
    }

    @PostMapping
    public BaseResponseDTO createOrder(@Valid @RequestBody OrderRequestDTO requestDTO) {
        return orderService.createOrder(requestDTO);
    }
}
