package com.ars.orderservice.dto.response;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CheckOrderInfoResponseDTO {
    private List<VoucherDTO> vouchers = new ArrayList<>();
    private List<ProductDTO> products = new ArrayList<>();

    public static class VoucherDTO {
        private Integer id;
        private Integer shopId;
        private String code;
        private Integer type;
        private Integer status;
        private Integer dateStarted;
        private Integer dateExpired;
        private BigDecimal value = BigDecimal.ZERO;

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

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public Integer getDateStarted() {
            return dateStarted;
        }

        public void setDateStarted(Integer dateStarted) {
            this.dateStarted = dateStarted;
        }

        public Integer getDateExpired() {
            return dateExpired;
        }

        public void setDateExpired(Integer dateExpired) {
            this.dateExpired = dateExpired;
        }

        public BigDecimal getValue() {
            return value;
        }

        public void setValue(BigDecimal value) {
            this.value = value;
        }
    }

    public static class ProductDTO {
        private Integer id;
        private Integer shopId;
        private String name;
        private String code;
        private BigDecimal price;
        private String status;
        private String thumbnailUrl;

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getThumbnailUrl() {
            return thumbnailUrl;
        }

        public void setThumbnailUrl(String thumbnailUrl) {
            this.thumbnailUrl = thumbnailUrl;
        }
    }

    public List<VoucherDTO> getVouchers() {
        return vouchers;
    }

    public void setVouchers(List<VoucherDTO> vouchers) {
        this.vouchers = vouchers;
    }

    public List<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDTO> products) {
        this.products = products;
    }
}
