package com.ars.orderservice.entity;

import com.ars.orderservice.dto.response.OrderDTO;
import com.dct.config.entity.AbstractAuditingEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.SqlResultSetMappings;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@SqlResultSetMappings(
    {
        @SqlResultSetMapping(
            name = "orderGetWithPaging",
            classes = {
                @ConstructorResult(
                    targetClass = OrderDTO.class,
                    columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "code", type = String.class),
                        @ColumnResult(name = "customerName", type = String.class),
                        @ColumnResult(name = "customerId", type = Integer.class),
                        @ColumnResult(name = "quantity", type = Integer.class),
                        @ColumnResult(name = "totalAmount", type = BigDecimal.class),
                        @ColumnResult(name = "status", type = String.class),
                        @ColumnResult(name = "paymentStatus", type = String.class),
                        @ColumnResult(name = "paymentMethod", type = String.class),
                        @ColumnResult(name = "orderDate", type = String.class)
                    }
                )
            }
        )
    }
)
@SuppressWarnings("unused")
public class Order extends AbstractAuditingEntity {
    @Column(name = "code", length = 50, nullable = false)
    private String code;

    @Column(name = "customer_id", nullable = false)
    private Integer customerId;

    @Column(name = "customer_name", length = 50, nullable = false)
    private String customerName;

    @Column(name = "discount", precision = 21, scale = 6, nullable = false)
    private BigDecimal discount = BigDecimal.ZERO;

    @Column(name = "amount", precision = 21, scale = 6, nullable = false)
    private BigDecimal amount = BigDecimal.ZERO;

    @Column(name = "total_amount", precision = 21, scale = 6, nullable = false)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(name = "status", length = 50, nullable = false)
    private String status;

    @Column(name = "quantity", nullable = false)
    private Integer quantity = 0;

    @Column(name = "payment_method", length = 50)
    private String paymentMethod;

    @Column(name = "payment_status", length = 50, nullable = false)
    private String paymentStatus;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<SubOrder> subOrders = new ArrayList<>();

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public List<SubOrder> getSubOrders() {
        return subOrders;
    }

    public void setSubOrders(List<SubOrder> subOrders) {
        this.subOrders = subOrders;
    }
}
