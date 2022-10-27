package com.muedsa.mock.wechat.mch.config;

import com.muedsa.mock.wechat.mch.dto.ErrorResponse;
import com.muedsa.mock.wechat.mch.exception.WechatMchException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(WechatMchException.class)
    public ResponseEntity<ErrorResponse> handleWechatMchException(WechatMchException e) {
        return new ResponseEntity<>(buildErrorResponse(e), e.getHttpStatus());
    }

    public static ErrorResponse buildErrorResponse(WechatMchException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(e.getCode());
        errorResponse.setMessage(e.getMessage());
        return errorResponse;
    }
}
