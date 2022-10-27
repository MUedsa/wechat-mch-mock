package com.muedsa.mock.wechat.mch.controller;

import com.muedsa.mock.wechat.mch.cert.MerchantManager;
import com.muedsa.mock.wechat.mch.container.AuthorizationContainer;
import com.muedsa.mock.wechat.mch.dto.CertificateInfo;
import com.muedsa.mock.wechat.mch.dto.SimpleListResponse;
import com.muedsa.mock.wechat.mch.service.PlatformCertificateService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/certificates")
@Slf4j
public class CertificatesController {

    private final PlatformCertificateService platformCertificateServiceImpl;

    @GetMapping
    public ResponseEntity<SimpleListResponse<CertificateInfo>> certificates(HttpServletRequest httpServletRequest) {
        MerchantManager.Merchant mch = (MerchantManager.Merchant) httpServletRequest.getAttribute(AuthorizationContainer.REQUEST_ATTR_MCH);
        List<CertificateInfo> list = platformCertificateServiceImpl.queryMchPlatformCertificates(mch.getMchId(), mch.getApiV3Key());
        return ResponseEntity.ok(new SimpleListResponse<>(list));
    }

    public CertificatesController(PlatformCertificateService platformCertificateServiceImpl) {
        this.platformCertificateServiceImpl = platformCertificateServiceImpl;
    }
}
