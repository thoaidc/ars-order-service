package com.ars.orderservice.repository;

import com.ars.orderservice.dto.mapping.CartMapping;
import com.ars.orderservice.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    @Query(value = "SELECT c.id, c.user_id as userId, c.quantity FROM cart c WHERE c.user_id = ?", nativeQuery = true)
    Optional<CartMapping> findCartByUserId(Integer userId);
}
