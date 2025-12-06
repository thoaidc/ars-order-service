package com.ars.orderservice.repository;

import com.ars.orderservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Modifying
    @Query(value = "UPDATE orders SET status = ?2 WHERE id = ?1", nativeQuery = true)
    void updateOrderStatusById(Integer orderId, String status);
}
