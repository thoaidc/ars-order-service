package com.ars.orderservice.service;

import com.ars.orderservice.dto.CartDTO;
import com.dct.model.dto.response.BaseResponseDTO;

public interface CartService {
    BaseResponseDTO getCartDetail(Integer userId);
    BaseResponseDTO updateCart(CartDTO cartDTO);
}
