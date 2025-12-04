package com.ars.orderservice.constants;

public class OrderConstants {
    interface Status {
        String PENDING = "";
        String COMPLETED = "";
        String FAILED = "";
    }

    interface PaymentStatus {
        String UNPAID = "UNPAID";
        String PAID = "PAID";
    }
}
