package com.ars.orderservice.entity;

import com.dct.config.entity.AbstractAuditingEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "cart")
@SuppressWarnings("unused")
public class Cart extends AbstractAuditingEntity {
    @Column(name = "user_id", nullable = false)
    private Integer userId;
    private Integer quantity;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
