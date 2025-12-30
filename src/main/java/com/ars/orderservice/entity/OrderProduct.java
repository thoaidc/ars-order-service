package com.ars.orderservice.entity;

import com.dct.config.entity.AbstractAuditingEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "order_product")
@SuppressWarnings("unused")
public class OrderProduct extends AbstractAuditingEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_order_id", nullable = false)
    @JsonIgnore
    private SubOrder subOrder;

    @Column(name = "product_code", nullable = false)
    private String productCode;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "product_thumbnail", nullable = false)
    private String productThumbnail;

    @Column(name = "product_id", nullable = false)
    private Integer productId;

    @Column(name = "order_id", nullable = false)
    private Integer orderId;

    @Column(name = "shop_id", nullable = false)
    private Integer shopId;

    @Column(name = "note", length = 500)
    private String note;

    @Column(name = "data")
    private String data;

    @Column(name = "total_amount", precision = 21, scale = 6, nullable = false)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @PrePersist
    public void prePersist() {
        if (subOrder != null && subOrder.getOrder() != null && subOrder.getOrder().getId() != null) {
            this.orderId = subOrder.getOrder().getId();
        }
    }

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

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public SubOrder getSubOrder() {
        return subOrder;
    }

    public void setSubOrder(SubOrder subOrder) {
        this.subOrder = subOrder;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public String getProductThumbnail() {
        return productThumbnail;
    }

    public void setProductThumbnail(String productThumbnail) {
        this.productThumbnail = productThumbnail;
    }
}
