package com.ars.orderservice.dto.request;

import java.util.HashSet;
import java.util.Set;

public class CheckOrderInfoRequestDTO {
    private Set<Integer> productIds = new HashSet<>();
    private Set<Integer> voucherIds = new HashSet<>();

    public Set<Integer> getProductIds() {
        return productIds;
    }

    public void setProductIds(Set<Integer> productIds) {
        this.productIds = productIds;
    }

    public Set<Integer> getVoucherIds() {
        return voucherIds;
    }

    public void setVoucherIds(Set<Integer> voucherIds) {
        this.voucherIds = voucherIds;
    }
}
