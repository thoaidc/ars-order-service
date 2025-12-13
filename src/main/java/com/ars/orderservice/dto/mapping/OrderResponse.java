package com.ars.orderservice.dto.mapping;

import java.math.BigDecimal;

public interface OrderResponse {
    Integer getId();
    String getCode();
    BigDecimal getAmount();
    BigDecimal getDiscount();
    BigDecimal getTotalAmount();
    String getStatus();
    String getPaymentStatus();
    String getPaymentMethod();
    String getOrderDate();
}
