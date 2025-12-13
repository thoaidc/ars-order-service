package com.ars.orderservice.repository;

import com.ars.orderservice.dto.mapping.OrderResponse;
import com.ars.orderservice.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>, OrderRepositoryCustom {
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.subOrders WHERE o.id = :orderId")
    Optional<Order> findByIdWithSubOrders(Integer orderId);

    @Query(value = """
            SELECT id, code, amount, discount, status,
                   total_amount as totalAmount,
                   payment_method as paymentMethod,
                   payment_status as paymentStatus,
                   created_date as orderDate
            FROM orders WHERE customer_id = ? ORDER BY id DESC
        """,
        nativeQuery = true
    )
    Page<OrderResponse> getOrderWithPagingForUser(Integer userId, Pageable pageable);
}
