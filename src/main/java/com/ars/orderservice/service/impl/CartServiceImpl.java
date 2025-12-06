package com.ars.orderservice.service.impl;

import com.ars.orderservice.dto.CartDTO;
import com.ars.orderservice.dto.mapping.CartMapping;
import com.ars.orderservice.dto.mapping.CartProductMapping;
import com.ars.orderservice.entity.Cart;
import com.ars.orderservice.entity.CartProduct;
import com.ars.orderservice.repository.CartProductRepository;
import com.ars.orderservice.repository.CartRepository;
import com.ars.orderservice.service.CartService;
import com.dct.model.dto.response.BaseResponseDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartProductRepository cartProductRepository;

    public CartServiceImpl(CartRepository cartRepository, CartProductRepository cartProductRepository) {
        this.cartRepository = cartRepository;
        this.cartProductRepository = cartProductRepository;
    }

    @Override
    public BaseResponseDTO getCartDetail(Integer userId) {
        Optional<CartMapping> cartMappingOptional = cartRepository.findCartByUserId(userId);
        CartDTO cartDTO = new CartDTO();

        if (cartMappingOptional.isEmpty()) {
            Cart cart = new Cart();
            cart.setUserId(userId);
            cart.setQuantity(0);
            cartRepository.save(cart);
            BeanUtils.copyProperties(cart, cartDTO, "products");
        } else {
            BeanUtils.copyProperties(cartMappingOptional.get(), cartDTO, "products");
        }

        List<CartProductMapping> cartProductMappings = cartProductRepository.findCartProductsByCartId(cartDTO.getId());
        List<CartDTO.CartProduct> cartProducts = cartProductMappings.stream()
                .map(cartProductMapping -> {
                    CartDTO.CartProduct cartProduct = new CartDTO.CartProduct();
                    BeanUtils.copyProperties(cartProductMapping, cartProduct);
                    return cartProduct;
                }).toList();
        cartDTO.setProducts(cartProducts);
        return BaseResponseDTO.builder().ok(cartDTO);
    }

    @Override
    public BaseResponseDTO updateCart(CartDTO cartDTO) {
        Optional<Cart> cartOptional = cartRepository.findById(cartDTO.getId());
        Cart cart = cartOptional.orElseGet(Cart::new);
        BeanUtils.copyProperties(cartDTO, cart, "products");
        List<CartProduct> oldCartProducts = cart.getProducts();
        Map<Integer, CartProduct> oldCartProductMap = oldCartProducts.stream()
                .collect(Collectors.toMap(CartProduct::getId, cartProduct -> cartProduct));
        List<Integer> oldCartProductIds = new ArrayList<>();
        List<CartProduct> newCartProducts = new ArrayList<>();

        cartDTO.getProducts().forEach(cartProduct -> {
            if (Objects.isNull(cartProduct.getId())) {
                CartProduct newCartProduct = new CartProduct();
                BeanUtils.copyProperties(cartProduct, newCartProduct, "id", "cartId");
                newCartProduct.setCart(cart);
                newCartProducts.add(newCartProduct);
            } else {
                oldCartProductIds.add(cartProduct.getId());
                CartProduct oldCartProduct = oldCartProductMap.get(cartProduct.getId());

                if (Objects.nonNull(oldCartProduct)) {
                    BeanUtils.copyProperties(cartProduct, oldCartProduct, "id", "cartId");
                }
            }
        });

        oldCartProducts.removeIf(oldCartProduct -> oldCartProductIds.stream()
            .noneMatch(oldCartProductId -> Objects.equals(oldCartProductId, oldCartProduct.getId()))
        );
        oldCartProducts.addAll(newCartProducts);
        return BaseResponseDTO.builder().ok(cartRepository.save(cart));
    }
}
