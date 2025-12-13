package com.ars.orderservice.repository.impl;

import com.ars.orderservice.dto.request.SearchOrderRequestDTO;
import com.ars.orderservice.dto.response.OrderDTO;
import com.ars.orderservice.repository.SubOrderRepositoryCustom;
import com.dct.config.common.SqlUtils;

import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class SubOrderRepositoryImpl implements SubOrderRepositoryCustom {
    private final EntityManager entityManager;

    public SubOrderRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Page<OrderDTO> getSubOrderWithPaging(SearchOrderRequestDTO requestDTO) {
        String countSql = "SELECT COUNT(*)";
        String querySql = """
            SELECT so.id, so.code, so.status, so.quantity,
                   so.order_id as orderId,
                   so.shop_id as shopId,
                   so.customer_id as customerId,
                   so.customer_name as customerName,
                   so.total_amount as totalAmount,
                   so.payment_method as paymentMethod,
                   so.payment_status as paymentStatus,
                   so.created_date as orderDate
        """;
        StringBuilder whereConditions = new StringBuilder(" FROM sub_order so " + SqlUtils.WHERE_DEFAULT);
        Map<String, Object> params = new HashMap<>();
        SqlUtils.addEqualCondition(whereConditions, params, "so.shop_id", requestDTO.getShopId());
        SqlUtils.addEqualCondition(whereConditions, params, "so.code", requestDTO.getOrderCode());
        SqlUtils.addEqualCondition(whereConditions, params, "so.customer_id", requestDTO.getUserId());
        SqlUtils.addEqualCondition(whereConditions, params, "so.status", requestDTO.getStatus());
        SqlUtils.addEqualCondition(whereConditions, params, "so.payment_status", requestDTO.getPaymentStatus());
        SqlUtils.addLikeCondition(whereConditions, params, requestDTO.getKeyword(), "so.customer_name");
        SqlUtils.addDateTimeCondition(whereConditions, params, requestDTO, "so.created_date");
        SqlUtils.setOrderByDecreasing(whereConditions, "so.id");
        return SqlUtils.queryBuilder(entityManager)
                .querySql(querySql + whereConditions)
                .countQuerySql(countSql + whereConditions)
                .pageable(requestDTO.getPageable())
                .params(params)
                .getResultsWithPaging("subOrderGetWithPaging");
    }
}
