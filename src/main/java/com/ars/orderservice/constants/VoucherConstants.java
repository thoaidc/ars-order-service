package com.ars.orderservice.constants;

public interface VoucherConstants {
    interface Status {
        Integer ACTIVE = 1;
        Integer INACTIVE = 0;
    }

    interface Type {
        Integer BY_VALUE = 1;
        Integer BY_PERCENTAGE = 2;
    }
}
