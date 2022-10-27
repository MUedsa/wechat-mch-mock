package com.muedsa.mock.wechat.mch.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class CertificateInfo {
    @JsonProperty(value = "serial_no")
    private String serialNo;
    @JsonProperty(value = "effective_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Date effectiveTime;
    @JsonProperty(value = "expire_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Date expireTime;
    @JsonProperty(value = "encrypt_certificate")
    private Base64EncryptData encryptCertificate;
}
