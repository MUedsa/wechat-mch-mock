package com.muedsa.mock.wechat.mch.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class WechatMchException extends RuntimeException {
    protected String code;
    protected HttpStatus httpStatus;

    public WechatMchException(String code, String message) {
        this(code, message, HttpStatus.INTERNAL_SERVER_ERROR, null);
    }

    public WechatMchException(String code, String message, HttpStatus httpStatus) {
        this(code, message, httpStatus, null);
    }

    public WechatMchException(String code, String message, Throwable cause) {
        this(code, message, HttpStatus.INTERNAL_SERVER_ERROR, cause);
    }

    public WechatMchException(String code, String message, HttpStatus httpStatus, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.httpStatus = httpStatus;
    }
}
