package com.muedsa.mock.wechat.mch.config;

import com.muedsa.mock.wechat.mch.container.ErrorCodeContainer;
import com.muedsa.mock.wechat.mch.dto.ErrorResponse;
import com.muedsa.mock.wechat.mch.exception.WechatMchException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(WechatMchException.class)
    public ResponseEntity<ErrorResponse> handleWechatMchException(WechatMchException e) {
        log.error(log.getName(), e);
        return new ResponseEntity<>(buildErrorResponse(e.getCode(), e.getMessage()), e.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleOtherException(Exception e) {
        log.error(log.getName(), e);
        return new ResponseEntity<>(buildErrorResponse(ErrorCodeContainer.ERROR, "内部错误"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static ErrorResponse buildErrorResponse(String code, String message) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(code);
        errorResponse.setMessage(message);
        return errorResponse;
    }
}
