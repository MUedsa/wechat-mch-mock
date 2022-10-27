package com.muedsa.mock.wechat.mch.cert;

import com.muedsa.mock.wechat.mch.util.ResUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component(value = MerchantManagerImpl.NAME)
@Slf4j
public class MerchantManagerImpl implements MerchantManager, CertificatesManager {
    public static final String NAME = "MerchantManager";

    public static final String CERT_FILE_PREFIX = "mock_client_";
    public static final String CERT_FILE_SUFFIX = ".cer";

    public static final String CERT_FILE_MATCH_PATH = "secret/" + CERT_FILE_PREFIX + "*_*" + CERT_FILE_SUFFIX;

    private static final Map<String, Merchant> map = new HashMap<>();
    private static final Merchant EMPTY_MERCHANT = new Merchant(null, null);

    private final CertificateFactory factory;

    private static final String PARAM_MATCH_GROUP_MCH_ID = "mchId";
    private static final String PARAM_MATCH_GROUP_API_V3_KEY = "apiV3Key";
    private static final Pattern FILE_NAME_PATTERN = Pattern.compile("^mock_client_(?<"
            + PARAM_MATCH_GROUP_MCH_ID
            + ">\\d+)_(?<"
            + PARAM_MATCH_GROUP_API_V3_KEY
            + ">[A-Za-z0-9]+)\\.cer$", Pattern.CASE_INSENSITIVE);

    public MerchantManagerImpl() throws CertificateException {
        factory = CertificateFactory.getInstance("X.509");
        load();
    }

    @Override
    public Merchant getMch(String mchId) {
        return map.get(mchId);
    }

    @Override
    public void loadMerchants() {
        load();
    }

    @Override
    public X509Certificate getCertificate(String mchId, String serialNo) {
        return map.getOrDefault(mchId, EMPTY_MERCHANT).getCertificate(serialNo);
    }

    @Override
    public Collection<X509Certificate> getCertificates(String mchId) {
        return map.getOrDefault(mchId, EMPTY_MERCHANT).getCertificates();
    }

    @Override
    public void loadCertificates() {
        load();
    }

    private void load() {
        log.info("[{}] Load merchants", NAME);
        try {
            Map<String, InputStream> fileMap = new HashMap<>();
            ResUtil.findResourceInputStreams(CERT_FILE_MATCH_PATH, fileMap);
            if(fileMap.size() > 0) {
                Map<String, Merchant> temp = new HashMap<>();
                fileMap.forEach((filename, inputStream) -> {
                    try {
                        Certificate certificate = factory.generateCertificate(inputStream);
                        if(certificate instanceof X509Certificate x509Certificate) {
                            Matcher matcher = FILE_NAME_PATTERN.matcher(filename);
                            if(matcher.matches()){
                                String mchId = matcher.group(PARAM_MATCH_GROUP_MCH_ID);
                                String apiV3Key = matcher.group(PARAM_MATCH_GROUP_API_V3_KEY);
                                Merchant mch = temp.computeIfAbsent(mchId, k -> new Merchant(mchId, apiV3Key));
                                if(!Objects.equals(apiV3Key, mch.getApiV3Key())) {
                                    log.warn("[{}] Load apiV3Key is inconsistent, mchId:{} \noldApiV3Key:{} \nnewApiV3Key:{} \nfilename:{}", NAME, mchId, mch.getApiV3Key(), apiV3Key, filename);
                                }
                                String serialNumber = x509Certificate.getSerialNumber().toString(16);
                                mch.addCertificate(serialNumber, x509Certificate);
                                log.info("[{}] Load merchant certificate, mchId:{}, apiV3Key:{}, serialNumber:{}, filename:{}", NAME, mchId, mch.getApiV3Key(), serialNumber, filename);
                            } else {
                                log.info("[{}] Load merchant certificate is not X509Certificate, filename:{}", NAME, filename);
                            }
                        }
                    } catch (CertificateException e) {
                        log.error("[{}] Load merchant certificate error, filename:{}", NAME, filename);
                    }
                });
                map.clear();
                map.putAll(temp);
                if(map.isEmpty()){
                    log.warn("[{}] Load merchants is empty!", NAME);
                }
            }
        } catch (IOException e) {
            log.error("[{}] Load merchants error", NAME, e);
            throw new IllegalStateException(e);
        }
    }
}
