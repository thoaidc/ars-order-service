package com.ars.orderservice.dto.mapping;

import java.math.BigDecimal;
import java.time.Instant;

public interface OrderResponse {
    Integer getId();
    String getCode();
    BigDecimal getAmount();
    BigDecimal getDiscount();
    BigDecimal getTotalAmount();
    String getStatus();
    String getPaymentStatus();
    String getPaymentMethod();
    Instant getOrderDate();
}
