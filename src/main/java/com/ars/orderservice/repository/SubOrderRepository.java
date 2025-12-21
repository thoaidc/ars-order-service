package com.ars.orderservice.repository;

import com.ars.orderservice.entity.SubOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SubOrderRepository extends JpaRepository<SubOrder, Integer>, SubOrderRepositoryCustom {
    @Query(value = "SELECT COUNT(*) FROM sub_order WHERE shop_id = ? AND created_date >= CURDATE()", nativeQuery = true)
    long getTotalOrdersTodayByShopId(Integer shopId);
}
