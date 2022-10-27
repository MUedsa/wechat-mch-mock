package com.muedsa.mock.wechat.mch.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class SimpleListResponse<T> {
    @JsonProperty(value = "data")
    private List<T> data;

    public SimpleListResponse(List<T> data) {
        this.data = data;
    }
}
