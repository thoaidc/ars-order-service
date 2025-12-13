package com.ars.orderservice.dto.response;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SubOrderDetailDTO extends OrderDTO {
    private BigDecimal discount;
    private BigDecimal amount;
    private List<OrderProductDTO> products = new ArrayList<>();

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public List<OrderProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<OrderProductDTO> products) {
        this.products = products;
    }
}
