package com.ars.orderservice.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OrderRequestDTO {
    @NotNull
    private Integer customerId;
    @NotBlank
    private String customerName;
    @NotBlank
    private String paymentMethod;
    private Set<Integer> voucherIds = new HashSet<>();
    @NotEmpty
    private List<@Valid OrderProduct> products = new ArrayList<>();

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Set<Integer> getVoucherIds() {
        return voucherIds;
    }

    public void setVoucherIds(Set<Integer> voucherIds) {
        this.voucherIds = voucherIds;
    }

    public List<OrderProduct> getProducts() {
        return products;
    }

    public void setProducts(List<OrderProduct> products) {
        this.products = products;
    }

    public static class OrderProduct {
        @NotNull
        private Integer shopId;
        @NotNull
        private Integer productId;
        @NotBlank
        private String note;
        @NotBlank
        private String data;

        public Integer getShopId() {
            return shopId;
        }

        public void setShopId(Integer shopId) {
            this.shopId = shopId;
        }

        public Integer getProductId() {
            return productId;
        }

        public void setProductId(Integer productId) {
            this.productId = productId;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }
}
