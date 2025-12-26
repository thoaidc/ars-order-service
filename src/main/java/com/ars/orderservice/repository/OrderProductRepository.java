package com.ars.orderservice.repository;

import com.ars.orderservice.dto.mapping.OrderProductResponse;
import com.ars.orderservice.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, Integer> {
    @Query(value = """
            SELECT id, note, data,
                   total_amount as totalAmount,
                   sub_order_id as subOrderId,
                   order_id as orderId,
                   shop_id as shopId,
                   product_id as productId,
                   product_name as productName,
                   product_code as productCode,
                   product_thumbnail as productThumbnail
            FROM order_product WHERE order_id = ? ORDER BY id DESC
        """,
        nativeQuery = true
    )
    List<OrderProductResponse> findAllOrderProductByOrderId(Integer orderId);

    @Query(value = """
            SELECT id, note, data,
                   total_amount as totalAmount,
                   sub_order_id as subOrderId,
                   order_id as orderId,
                   shop_id as shopId,
                   product_id as productId,
                   product_name as productName,
                   product_code as productCode,
                   product_thumbnail as productThumbnail
            FROM order_product WHERE sub_order_id = ? ORDER BY id DESC
        """,
        nativeQuery = true
    )
    List<OrderProductResponse> findAllOrderProductBySubOrderId(Integer subOrderId);
}
