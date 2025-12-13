package com.ars.orderservice.repository;

import com.ars.orderservice.entity.SubOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubOrderRepository extends JpaRepository<SubOrder, Integer>, SubOrderRepositoryCustom {}
