package com.muedsa.mock.wechat.mch.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Base64EncryptData {
    @JsonProperty(value = "algorithm")
    private String algorithm;
    @JsonProperty(value = "ciphertext")
    private String ciphertext;
    @JsonProperty(value = "nonce")
    private String nonce;
    @JsonProperty(value = "associated_data")
    private String associatedData;
}
