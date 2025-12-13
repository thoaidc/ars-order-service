package com.ars.orderservice.dto.mapping;

import java.math.BigDecimal;

public interface OrderProductResponse {
    Integer getId();
    Integer getSubOrderId();
    Integer getOrderId();
    Integer getShopId();
    Integer getProductId();
    String getProductCode();
    String getProductName();
    String getProductThumbnail();
    String getNote();
    String getData();
    BigDecimal getTotalAmount();
}
