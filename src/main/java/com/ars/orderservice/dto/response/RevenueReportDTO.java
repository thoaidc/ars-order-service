package com.ars.orderservice.dto.response;

import java.math.BigDecimal;

public class RevenueReportDTO {
    private Integer productId;
    private String productCode;
    private String productName;
    private BigDecimal grossRevenue;
    private BigDecimal netRevenue;
    private Integer totalSales;

    public RevenueReportDTO(
        Integer productId,
        String productCode,
        String productName,
        BigDecimal grossRevenue,
        BigDecimal netRevenue,
        Integer totalSales
    ) {
        this.productId = productId;
        this.productCode = productCode;
        this.productName = productName;
        this.grossRevenue = grossRevenue;
        this.netRevenue = netRevenue;
        this.totalSales = totalSales;
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

    public BigDecimal getGrossRevenue() {
        return grossRevenue;
    }

    public void setGrossRevenue(BigDecimal grossRevenue) {
        this.grossRevenue = grossRevenue;
    }

    public BigDecimal getNetRevenue() {
        return netRevenue;
    }

    public void setNetRevenue(BigDecimal netRevenue) {
        this.netRevenue = netRevenue;
    }

    public Integer getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(Integer totalSales) {
        this.totalSales = totalSales;
    }
}
