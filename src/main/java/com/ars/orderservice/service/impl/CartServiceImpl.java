package com.ars.orderservice.service.impl;

import com.ars.orderservice.dto.CartDTO;
import com.ars.orderservice.repository.CartRepository;
import com.ars.orderservice.service.CartService;
import com.dct.model.dto.response.BaseResponseDTO;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;

    public CartServiceImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public BaseResponseDTO getCartDetail(Integer userId) {
        return null;
    }

    @Override
    public BaseResponseDTO updateCart(CartDTO cartDTO) {
        return null;
    }
}
