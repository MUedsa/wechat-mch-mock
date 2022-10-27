package com.muedsa.mock.wechat.mch.util;

import com.muedsa.mock.wechat.mch.exception.WechatMchException;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;

public class ValidateUtil {

    public static void state(boolean expression, String code, String message) {
        if (!expression) {
            throw new WechatMchException(code, message);
        }
    }

    public static void state(boolean expression, String code, String message, HttpStatus httpStatus) {
        if (!expression) {
            throw new WechatMchException(code, message, httpStatus);
        }
    }

    public static void isTrue(boolean expression, String code, String message) {
        state(expression, code, message);
    }

    public static void isTrue(boolean expression, String code, String message, HttpStatus httpStatus) {
        state(expression, code, message, httpStatus);
    }

    public static void isNull(@Nullable Object object, String code, String message) {
        if (object != null) {
            throw new WechatMchException(code, message);
        }
    }

    public static void isNull(@Nullable Object object, String code, String message, HttpStatus httpStatus) {
        if (object != null) {
            throw new WechatMchException(code, message, httpStatus);
        }
    }

    public static void notNull(@Nullable Object object, String code, String message) {
        if (object == null) {
            throw new WechatMchException(code, message);
        }
    }

    public static void notNull(@Nullable Object object, String code, String message, HttpStatus httpStatus) {
        if (object == null) {
            throw new WechatMchException(code, message, httpStatus);
        }
    }

    public static void hasLength(@Nullable String text, String code, String message) {
        if (!StringUtils.hasLength(text)) {
            throw new WechatMchException(code, message);
        }
    }

    public static void hasLength(@Nullable String text, String code, String message, HttpStatus httpStatus) {
        if (!StringUtils.hasLength(text)) {
            throw new WechatMchException(code, message, httpStatus);
        }
    }

    public static void notEmpty(@Nullable Collection<?> collection, String code, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new WechatMchException(code, message);
        }
    }

    public static void notEmpty(@Nullable Collection<?> collection, String code, String message, HttpStatus httpStatus) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new WechatMchException(code, message, httpStatus);
        }
    }
}
