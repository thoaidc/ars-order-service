package com.ars.orderservice.repository.impl;

import com.ars.orderservice.constants.OrderConstants;
import com.ars.orderservice.dto.request.RevenueReportFilter;
import com.ars.orderservice.dto.request.SearchOrderRequestDTO;
import com.ars.orderservice.dto.response.OrderDTO;
import com.ars.orderservice.dto.response.RevenueReportDTO;
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

    @Override
    public Page<RevenueReportDTO> getRevenueReport(RevenueReportFilter requestDTO) {
        String countSql = """
            SELECT COUNT(*) FROM (
                SELECT op.product_id
                FROM order_product op
                JOIN sub_order so ON op.sub_order_id = so.id
        """;
        String querySql = """
            SELECT
                op.product_id as productId,
                op.product_code as productCode,
                op.product_name as productName,
                SUM(op.total_amount) AS grossRevenue,
                SUM(IF(so.amount = 0, 0, op.total_amount - (op.total_amount / so.amount * so.discount))) AS netRevenue,
                COUNT(op.id) AS totalSales
            FROM
                order_product op JOIN sub_order so ON op.sub_order_id = so.id
        """;

        StringBuilder whereConditions = new StringBuilder(SqlUtils.WHERE_DEFAULT);
        Map<String, Object> params = new HashMap<>();
        SqlUtils.addEqualCondition(whereConditions, params, "so.shop_id", requestDTO.getShopId());
        SqlUtils.addEqualCondition(whereConditions, params, "so.status", OrderConstants.Status.COMPLETED);
        SqlUtils.addDateTimeCondition(whereConditions, params, requestDTO, "so.created_date");
        SqlUtils.addGroupByClause(whereConditions, "op.product_id, op.product_code, op.product_name");
        countSql += whereConditions + ") AS total_products";
        SqlUtils.setOrderByDecreasing(whereConditions, "netRevenue");
        return SqlUtils.queryBuilder(entityManager)
                .querySql(querySql + whereConditions)
                .countQuerySql(countSql)
                .pageable(requestDTO.getPageable())
                .params(params)
                .getResultsWithPaging("revenueReportGetWithPaging");
    }
}
