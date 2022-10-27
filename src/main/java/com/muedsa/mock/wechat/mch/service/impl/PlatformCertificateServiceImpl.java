package com.muedsa.mock.wechat.mch.service.impl;

import com.muedsa.mock.wechat.mch.cert.PlatformSecretManager;
import com.muedsa.mock.wechat.mch.dto.Base64EncryptData;
import com.muedsa.mock.wechat.mch.dto.CertificateInfo;
import com.muedsa.mock.wechat.mch.service.PlatformCertificateService;
import com.muedsa.mock.wechat.mch.util.AesUtil;
import com.muedsa.mock.wechat.mch.util.PemUtil;
import com.muedsa.mock.wechat.mch.util.ValidateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class PlatformCertificateServiceImpl implements PlatformCertificateService {

    private final PlatformSecretManager platformSecretManager;

    @Override
    public List<CertificateInfo> queryMchPlatformCertificates(String mchId, String apiV3Key) {
        Collection<X509Certificate> certificates = platformSecretManager.getCertificates(mchId);
        ValidateUtil.notEmpty(certificates, "certificates_is_empty", "certificates is empty!");
        List<CertificateInfo> list = new ArrayList<>(certificates.size());
        for (X509Certificate certificate : certificates) {
            CertificateInfo certInfo = new CertificateInfo();
            certInfo.setSerialNo(certificate.getSerialNumber().toString(16));
            certInfo.setEffectiveTime(certificate.getNotBefore());
            certInfo.setExpireTime(certificate.getNotAfter());
            Base64EncryptData encryptCert = new Base64EncryptData();
            encryptCert.setAlgorithm(AesUtil.ALGORITHM);
            String nonce = RandomStringUtils.randomAlphanumeric(12);
            String associatedData = "certificate";
            encryptCert.setNonce(nonce);
            encryptCert.setAssociatedData(associatedData);
            try {
                encryptCert.setCiphertext(AesUtil.encryptToBase64(apiV3Key, associatedData, nonce, PemUtil.toPEM(certificate)));
            } catch (BadPaddingException | IllegalBlockSizeException | CertificateEncodingException e) {
                throw new IllegalStateException(e);
            }
            certInfo.setEncryptCertificate(encryptCert);
            list.add(certInfo);
        }
        return list;
    }

    public PlatformCertificateServiceImpl(PlatformSecretManager platformSecretManager) {
        this.platformSecretManager = platformSecretManager;
    }
}
