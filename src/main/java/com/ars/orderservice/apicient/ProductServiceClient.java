package com.ars.orderservice.apicient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "PRODUCT-SERVICE")
public interface ProductServiceClient {
    @GetMapping("/api/internal/products/files/download/{productId}")
    ResponseEntity<Resource> getProductOriginalFile(@PathVariable("productId") Integer productId);
}
