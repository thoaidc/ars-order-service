package com.ars.orderservice.dto.request;

import com.dct.model.dto.request.BaseRequestDTO;

public class SearchOrderForUserRequestDTO extends BaseRequestDTO {
    private Integer userId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
