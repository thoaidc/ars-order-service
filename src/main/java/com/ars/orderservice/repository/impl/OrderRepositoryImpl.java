package com.ars.orderservice.repository.impl;

import com.ars.orderservice.dto.request.SearchOrderRequestDTO;
import com.ars.orderservice.dto.response.OrderDTO;
import com.ars.orderservice.repository.OrderRepositoryCustom;
import com.dct.config.common.SqlUtils;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class OrderRepositoryImpl implements OrderRepositoryCustom {
    private final EntityManager entityManager;

    public OrderRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Page<OrderDTO> getOrderWithPaging(SearchOrderRequestDTO requestDTO) {
        String countSql = "SELECT COUNT(*)";
        String querySql = """
            SELECT o.id, o.code, o.status, o.quantity,
                   o.customer_id as customerId,
                   o.customer_name as customerName,
                   o.total_amount as totalAmount,
                   o.payment_method as paymentMethod,
                   o.payment_status as paymentStatus,
                   o.created_date as orderDate
        """;
        StringBuilder whereConditions = new StringBuilder(" FROM orders o " + SqlUtils.WHERE_DEFAULT);
        Map<String, Object> params = new HashMap<>();
        SqlUtils.addEqualCondition(whereConditions, params, "o.code", requestDTO.getOrderCode());
        SqlUtils.addEqualCondition(whereConditions, params, "o.customer_id", requestDTO.getUserId());
        SqlUtils.addEqualCondition(whereConditions, params, "o.status", requestDTO.getStatus());
        SqlUtils.addEqualCondition(whereConditions, params, "o.payment_status", requestDTO.getPaymentStatus());
        SqlUtils.addLikeCondition(whereConditions, params, requestDTO.getKeyword(), "o.customer_name");
        SqlUtils.addDateTimeCondition(whereConditions, params, requestDTO, "o.created_date");
        SqlUtils.setOrderByDecreasing(whereConditions, "o.id");
        return SqlUtils.queryBuilder(entityManager)
                .querySql(querySql + whereConditions)
                .countQuerySql(countSql + whereConditions)
                .pageable(requestDTO.getPageable())
                .params(params)
                .getResultsWithPaging("orderGetWithPaging");
    }
}
