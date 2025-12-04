package com.ars.orderservice.resource;

import com.ars.orderservice.dto.CartDTO;
import com.ars.orderservice.service.CartService;
import com.dct.model.dto.response.BaseResponseDTO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/carts")
public class CartResource {
    private final CartService cartService;

    public CartResource(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{userId}")
    public BaseResponseDTO getCartDetail(@PathVariable Integer userId) {
        return cartService.getCartDetail(userId);
    }

    @PutMapping
    public BaseResponseDTO updateCart(@Valid @RequestBody CartDTO cartDTO) {
        return cartService.updateCart(cartDTO);
    }
}
