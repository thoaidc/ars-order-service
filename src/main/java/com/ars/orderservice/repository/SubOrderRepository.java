package com.ars.orderservice.repository;

import com.ars.orderservice.entity.SubOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubOrderRepository extends JpaRepository<SubOrder, Integer>, SubOrderRepositoryCustom {
    @Query(value = "SELECT shop_id FROM sub_order WHERE order_id = ?", nativeQuery = true)
    List<Integer> findShopIdsByOrderId(Integer orderId);
}
