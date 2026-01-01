package com.ars.orderservice.dto.mapping;

import java.math.BigDecimal;

public interface CartProductMapping {
    Integer getId();
    Integer getCartId();
    Integer getShopId();
    Integer getProductId();
    String getProductName();
    String getThumbnail();
    BigDecimal getPrice();
    String getData();
}
