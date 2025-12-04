package com.ars.orderservice.entity;

import com.dct.config.entity.AbstractAuditingEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "cart_product")
@SuppressWarnings("unused")
public class CartProduct extends AbstractAuditingEntity {
    @Column(name = "cart_id", nullable = false)
    private Integer cartId;

    @Column(name = "product_id", nullable = false)
    private Integer productId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "thumbnail", nullable = false)
    private String thumbnail;

    @Column(name = "price", precision = 21, scale = 6, nullable = false)
    private BigDecimal price;

    @Column(name = "data")
    private String data;

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
