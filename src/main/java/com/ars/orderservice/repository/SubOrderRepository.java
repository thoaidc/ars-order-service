package com.ars.orderservice.repository;

import com.ars.orderservice.dto.mapping.OrderSalesMapping;
import com.ars.orderservice.entity.SubOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubOrderRepository extends JpaRepository<SubOrder, Integer>, SubOrderRepositoryCustom {
    @Query(value = """
            SELECT COUNT(*)
            FROM sub_order
            WHERE shop_id = ? AND status = 'COMPLETED' AND payment_status = 'PAID' AND created_date >= CURDATE()
        """,
        nativeQuery = true
    )
    long getTotalOrdersTodayByShopId(Integer shopId);

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
            LEFT JOIN sub_order s ON DATE(s.created_date) = d.calendar_date
            AND s.shop_id = ?
            AND s.status = 'COMPLETED' AND s.payment_status = 'PAID'
            GROUP BY d.calendar_date
            ORDER BY d.calendar_date
        """,
        nativeQuery = true
    )
    List<OrderSalesMapping> getTotalOrderSalesLastSevenDay(Integer shopId);
}
