package com.muedsa.mock.wechat.mch.container;

import org.springframework.http.HttpStatus;

public class ErrorContainer {
    public static final String CODE_SIGN_ERROR = "SIGN_ERROR";
    public static final String MESSAGE_SIGN_ERROR = "错误的签名，验签失败";
    public static final String MESSAGE_SIGN_FORMAT_ERROR = "Http头Authorization值格式错误，请参考《微信支付商户REST API签名规则》";
    public static final String MESSAGE_SIGN_INFO_ERROR = "签名信息错误，验签失败";

    public static final HttpStatus HTTP_STATUS_SING_ERROR = HttpStatus.UNAUTHORIZED;
}
