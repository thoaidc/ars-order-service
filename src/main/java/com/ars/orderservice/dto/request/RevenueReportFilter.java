package com.ars.orderservice.dto.request;

import com.dct.model.dto.request.BaseRequestDTO;

public class RevenueReportFilter extends BaseRequestDTO {
    private Integer shopId;

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }
}
