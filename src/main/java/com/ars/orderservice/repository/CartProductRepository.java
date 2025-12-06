package com.ars.orderservice.repository;

import com.ars.orderservice.dto.mapping.CartProductMapping;
import com.ars.orderservice.entity.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartProductRepository extends JpaRepository<CartProduct, Integer> {
    @Query(value = """
            SELECT cp.id, cp.product_id as productId,
                cp.cart_id as cartId,
                cp.product_name as productName,
                cp.thumbnail, cp.price, cp.data
            FROM cart_product cp
            WHERE cp.cart_id = ?
        """,
        nativeQuery = true
    )
    List<CartProductMapping> findCartProductsByCartId(Integer cartId);
}
