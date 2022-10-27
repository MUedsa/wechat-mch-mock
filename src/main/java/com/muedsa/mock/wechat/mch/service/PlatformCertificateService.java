package com.muedsa.mock.wechat.mch.service;

import com.muedsa.mock.wechat.mch.dto.CertificateInfo;

import java.util.List;

public interface PlatformCertificateService {
    List<CertificateInfo> queryMchPlatformCertificates(String mchId, String apiV3Key);
}
