package com.ars.orderservice.entity;

import com.dct.config.entity.AbstractAuditingEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "order_product")
@SuppressWarnings("unused")
public class OrderProduct extends AbstractAuditingEntity {
    @Column(name = "product_code", nullable = false)
    private String productCode;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "product_id", nullable = false)
    private Integer productId;

    @Column(name = "order_id", nullable = false)
    private Integer orderId;

    @Column(name = "sub_order_id", nullable = false)
    private Integer subOrderId;

    @Column(name = "shop_id", nullable = false)
    private Integer shopId;

    @Column(name = "note", length = 500)
    private String note;

    @Column(name = "data", length = 2000)
    private String data;

    @Column(name = "discount", precision = 21, scale = 6, nullable = false)
    private BigDecimal discount;

    @Column(name = "amount", precision = 21, scale = 6, nullable = false)
    private BigDecimal amount;

    @Column(name = "total_amount", precision = 21, scale = 6, nullable = false)
    private BigDecimal totalAmount;

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
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

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getSubOrderId() {
        return subOrderId;
    }

    public void setSubOrderId(Integer subOrderId) {
        this.subOrderId = subOrderId;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }
}
