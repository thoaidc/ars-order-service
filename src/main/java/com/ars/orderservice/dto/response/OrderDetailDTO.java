package com.ars.orderservice.dto.response;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailDTO extends OrderDTO {
    private BigDecimal discount;
    private BigDecimal amount;
    private List<SubOrderDetailDTO> subOrders = new ArrayList<>();
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

    public List<SubOrderDetailDTO> getSubOrders() {
        return subOrders;
    }

    public void setSubOrders(List<SubOrderDetailDTO> subOrders) {
        this.subOrders = subOrders;
    }

    public List<OrderProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<OrderProductDTO> products) {
        this.products = products;
    }
}
