package com.ars.orderservice.dto.response;

import java.math.BigDecimal;

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
    private String orderDate;

    public OrderDTO() {}

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
        String orderDate
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
        String orderDate
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

    public static class OrderProductDTO {
        private Integer id;
        private Integer subOrderId;
        private Integer orderId;
        private Integer shopId;
        private Integer productId;
        private String productCode;
        private String productName;
        private String productThumbnail;
        private String note;
        private String data;
        private OrderProductDataDTO metadata;
        private BigDecimal totalAmount;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getSubOrderId() {
            return subOrderId;
        }

        public void setSubOrderId(Integer subOrderId) {
            this.subOrderId = subOrderId;
        }

        public Integer getOrderId() {
            return orderId;
        }

        public void setOrderId(Integer orderId) {
            this.orderId = orderId;
        }

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

        public String getProductThumbnail() {
            return productThumbnail;
        }

        public void setProductThumbnail(String productThumbnail) {
            this.productThumbnail = productThumbnail;
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

        public OrderProductDataDTO getMetadata() {
            return metadata;
        }

        public void setMetadata(OrderProductDataDTO metadata) {
            this.metadata = metadata;
        }
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

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
}
