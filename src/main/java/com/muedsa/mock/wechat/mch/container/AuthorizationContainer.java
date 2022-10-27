package com.muedsa.mock.wechat.mch.container;

public class AuthorizationContainer {
    public static final String HTTP_HEAD_KEY = "Authorization";
    public static final String SUCCESS_FLAG = "success";

    public static final String FIELD_MCH_ID = "mchid";
    public static final String FIELD_SERIAL_NO = "serial_no";
    public static final String FIELD_NONCE_STR = "nonce_str";
    public static final String FIELD_TIMESTAMP = "timestamp";
    public static final String FIELD_SIGNATURE = "signature";

    public static final String REQUEST_ATTR_MCH = "mch";


    public static final String HTTP_HEAD_KEY_SERIAL = "Wechatpay-Serial";
    public static final String HTTP_HEAD_KEY_SIGNATURE = "Wechatpay-Signature";
    public static final String HTTP_HEAD_KEY_TIMESTAMP = "Wechatpay-Timestamp";
    public static final String HTTP_HEAD_KEY_NONCE = "Wechatpay-Nonce";
    public static final String HTTP_HEAD_KEY_REQUEST_ID = "Request-ID";
}
