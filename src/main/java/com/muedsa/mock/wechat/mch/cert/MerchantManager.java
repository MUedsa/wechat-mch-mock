package com.muedsa.mock.wechat.mch.cert;

import java.io.FileNotFoundException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public interface MerchantManager {

    Merchant getMch(String mchId);

    void loadMerchants() throws FileNotFoundException, CertificateException;

    class Merchant {
        private final String mchId;
        private final String apiV3Key;
        private final Map<String, X509Certificate> map = new HashMap<>();

        public Merchant(String mchId, String apiV3Key) {
            this.mchId = mchId;
            this.apiV3Key = apiV3Key;
        }

        public String getMchId() {
            return mchId;
        }

        public String getApiV3Key() {
            return apiV3Key;
        }

        void addCertificate(String serialNo, X509Certificate certificate) {
            map.put(serialNo, certificate);
        }

        public X509Certificate getCertificate(String serialNo) {
            return map.get(serialNo);
        }

        public Collection<X509Certificate> getCertificates() {
            return map.values();
        }
    }
}
