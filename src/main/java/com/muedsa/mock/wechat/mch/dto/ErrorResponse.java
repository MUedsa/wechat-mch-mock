package com.muedsa.mock.wechat.mch.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ErrorResponse {
    @JsonProperty(value = "code")
    private String code;
    //private ErrorDetail detail;
    @JsonProperty(value = "message")
    private String message;
}
