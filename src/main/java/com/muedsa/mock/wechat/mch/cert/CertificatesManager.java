package com.muedsa.mock.wechat.mch.cert;

import java.security.cert.X509Certificate;
import java.util.Collection;

public interface CertificatesManager {
    X509Certificate getCertificate(String mchId, String serialNo);

    Collection<X509Certificate> getCertificates(String mchId);

    void loadCertificates();
}
