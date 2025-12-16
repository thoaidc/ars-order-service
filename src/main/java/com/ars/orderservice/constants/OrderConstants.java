package com.ars.orderservice.constants;

import java.math.BigDecimal;

public interface OrderConstants {
    interface Status {
        String PENDING = "PENDING";
        String COMPLETED = "COMPLETED";
        String FAILED = "FAILED";
    }

    interface PaymentStatus {
        String UNPAID = "UNPAID";
        String PAID = "PAID";
    }

    BigDecimal PLATFORM_FEE_FACTOR = new BigDecimal("0.05"); // 5% order total amount
    int SCALE_NUMBER = 0; // Round to zero decimal places
}
