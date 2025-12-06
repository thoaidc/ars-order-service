package com.ars.orderservice.constants;

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
}
