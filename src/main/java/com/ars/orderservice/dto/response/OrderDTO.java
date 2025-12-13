package com.ars.orderservice.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

public class OrderDTO {
    private Integer id;
    private Integer shopId;
    private Integer orderId;
    private String code;
    private String customerName;
    private Integer customerId;
    private Integer quantity;
    private BigDecimal totalAmount;
    private String status;
    private String paymentStatus;
    private String paymentMethod;
    private Instant orderDate;

    public OrderDTO(
        Integer id,
        String code,
        String customerName,
        Integer customerId,
        Integer quantity,
        BigDecimal totalAmount,
        String status,
        String paymentStatus,
        String paymentMethod,
        Instant orderDate
    ) {
        this.id = id;
        this.code = code;
        this.customerName = customerName;
        this.customerId = customerId;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.status = status;
        this.paymentStatus = paymentStatus;
        this.paymentMethod = paymentMethod;
        this.orderDate = orderDate;
    }

    public OrderDTO(
        Integer id,
        Integer shopId,
        Integer orderId,
        String code,
        String customerName,
        Integer customerId,
        Integer quantity,
        BigDecimal totalAmount,
        String status,
        String paymentStatus,
        String paymentMethod,
        Instant orderDate
    ) {
        this.id = id;
        this.shopId = shopId;
        this.orderId = orderId;
        this.code = code;
        this.customerName = customerName;
        this.customerId = customerId;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.status = status;
        this.paymentStatus = paymentStatus;
        this.paymentMethod = paymentMethod;
        this.orderDate = orderDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Instant getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
    }
}
