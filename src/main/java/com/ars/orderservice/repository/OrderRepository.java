package com.ars.orderservice.repository;

import com.ars.orderservice.dto.mapping.OrderResponse;
import com.ars.orderservice.dto.mapping.OrderSalesMapping;
import com.ars.orderservice.entity.Order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    @Query(value = """
            SELECT COUNT(*) FROM orders WHERE status = 'COMPLETED' AND payment_status = 'PAID' AND created_date >= CURDATE()
        """,
        nativeQuery = true
    )
    long getTotalOrdersToday();

    @Query(value = """ 
            WITH RECURSIVE dates AS (
                SELECT DATE_SUB(CURDATE(), INTERVAL 6 DAY) AS calendar_date
                UNION ALL
                SELECT calendar_date + INTERVAL 1 DAY
                FROM dates
                WHERE calendar_date < CURDATE()
            )
            SELECT
                d.calendar_date AS date,
                COUNT(s.id) AS amount
            FROM dates d
            LEFT JOIN orders s ON DATE(s.created_date) = d.calendar_date
            AND s.status = 'COMPLETED' AND s.payment_status = 'PAID'
            GROUP BY d.calendar_date
            ORDER BY d.calendar_date
        """,
        nativeQuery = true
    )
    List<OrderSalesMapping> getTotalOrderSalesLastSevenDay();
}
