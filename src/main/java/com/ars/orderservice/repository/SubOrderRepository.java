package com.ars.orderservice.repository;

import com.ars.orderservice.dto.mapping.OrderSalesMapping;
import com.ars.orderservice.entity.SubOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubOrderRepository extends JpaRepository<SubOrder, Integer>, SubOrderRepositoryCustom {
    @Query(value = "SELECT COUNT(*) FROM sub_order WHERE shop_id = ? AND created_date >= CURDATE()", nativeQuery = true)
    long getTotalOrdersTodayByShopId(Integer shopId);

    @Query(value = """
            SELECT
                DATE(created_date) AS date,
                SUM(amount) AS amount
            FROM sub_order
            WHERE shop_id = ? AND status = 'SUCCESS' AND created_date >= DATE_SUB(CURDATE(), INTERVAL 6 DAY)
            GROUP BY DATE(created_date)
            ORDER BY date
        """,
        nativeQuery = true
    )
    List<OrderSalesMapping> getTotalOrderSalesToDay(Integer shopId);
}
