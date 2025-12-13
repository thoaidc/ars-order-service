package com.ars.orderservice.repository;

import com.ars.orderservice.dto.mapping.OrderResponse;
import com.ars.orderservice.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>, OrderRepositoryCustom {

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
