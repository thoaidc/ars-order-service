package com.ars.orderservice.dto.request;

import com.dct.model.dto.request.BaseRequestDTO;

public class SearchOrderRequestDTO extends BaseRequestDTO {
    private Integer shopId;
    private String customerName;
    private String paymentStatus;

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
