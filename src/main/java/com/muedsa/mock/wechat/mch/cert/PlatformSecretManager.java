package com.muedsa.mock.wechat.mch.cert;

import com.muedsa.mock.wechat.mch.util.ResUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.*;

@Component(value = PlatformSecretManager.NAME)
@Slf4j
public class PlatformSecretManager implements CertificatesManager, PlatformPrivateManager {
    public static final String NAME = "PlatformSecretManager";

    private static final String CERT_FILE_PREFIX = "mock_server_";
    private static final String CERT_FILE_SUFFIX = ".cer";

    public static final String CERT_FILE_MATCH_PATH = "secret/" + CERT_FILE_PREFIX + "*" + CERT_FILE_SUFFIX;

    private final Map<String, Map<String, X509Certificate>> certMAP = new HashMap<>();
    private static final Map<String, X509Certificate> EMPTY_CERT_MAP = Collections.emptyMap();

    private final CertificateFactory factory;

    private static final String PRIVATE_KEY_FILE_NAME = "mock_server.p12";
    private static final char[] PRIVATE_KEY_STORE_PASSWORD = "123456".toCharArray();
    private static final char[] PRIVATE_KEY_PASSWORD = "123456".toCharArray();
    private Map<String, PrivateKey> keyMap = new HashMap<>();


    public PlatformSecretManager() throws CertificateException {
        factory = CertificateFactory.getInstance("X.509");
        loadCertificates();
        loadPrivateKey();
    }

    @Override
    public X509Certificate getCertificate(String mchId, String serialNo) {
        return certMAP.getOrDefault(mchId, EMPTY_CERT_MAP).get(serialNo);
    }

    @Override
    public Collection<X509Certificate> getCertificates(String mchId) {
        return certMAP.getOrDefault(mchId, EMPTY_CERT_MAP).values();
    }

    public void loadCertificates() {
        log.info("[{}] Loading certificates", NAME);
        try {
            Map<String, InputStream> fileMap = new HashMap<>();
            ResUtil.findResourceInputStreams(CERT_FILE_MATCH_PATH, fileMap);
            if(fileMap.size() > 0) {
                Map<String, Map<String, X509Certificate>> temp = new HashMap<>();
                fileMap.forEach((filename, inputStream) -> {
                    try {
                        Certificate certificate = factory.generateCertificate(inputStream);
                        if(certificate instanceof X509Certificate x509Certificate) {
                            String mchId = filename.substring(CERT_FILE_PREFIX.length());
                            mchId = mchId.substring(0, mchId.length() - CERT_FILE_SUFFIX.length());
                            Map<String, X509Certificate> certificateMap = temp.computeIfAbsent(mchId, k -> new HashMap<>());
                            String serialNumber = x509Certificate.getSerialNumber().toString(16);
                            certificateMap.put(serialNumber, x509Certificate);
                            log.info("[{}] Loaded certificate, mchId:{}, serialNumber:{}, filename:{}", NAME, mchId, serialNumber, filename);
                        } else {
                            log.info("[{}] Loaded certificate is not X509Certificate, filename:{}", NAME, filename);
                        }
                    } catch (CertificateException e) {
                        log.error("[{}] Load certificate error, filename:{}", NAME, filename, e);
                    }
                });
                certMAP.clear();
                certMAP.putAll(temp);
                if(certMAP.isEmpty()){
                    log.warn("[{}] Load certificates empty!", NAME);
                }
            }
        } catch (IOException e) {
            log.error("[{}] Load certificates error", NAME, e);
            throw new IllegalStateException(e);
        }
    }

    @Override
    public PrivateKey getPrivateKey(String mchId) {
        return keyMap.get(mchId);
    }

    @Override
    public void loadPrivateKey() {
        log.info("[{}] Loading private key", NAME);
        try {
            InputStream fileInputStream = ResUtil.getResourceInputStream("secret" + "/" + PRIVATE_KEY_FILE_NAME);
            if(Objects.nonNull(fileInputStream)) {
                KeyStore keyStore = KeyStore.getInstance("PKCS12");
                keyStore.load(fileInputStream, PRIVATE_KEY_STORE_PASSWORD);
                certMAP.forEach((k, v) -> {
                    try {
                        if(keyStore.getKey("mch-" + k, PRIVATE_KEY_PASSWORD) instanceof PrivateKey privateKey){
                            keyMap.put(k, privateKey);
                            log.info("[{}] Loaded privateKey mchId:{}", NAME, k);
                        } else {
                            log.info("[{}] Loaded privateKey failure mchId:{}", NAME, k);
                        }
                    } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
                        throw new RuntimeException(e);
                    }
                });
            } else {
                log.info("[{}] Fail loading private key", NAME);
                throw new IllegalStateException("Fail loading private key");
            }
        } catch (NoSuchAlgorithmException | CertificateException | KeyStoreException | IOException e) {
            throw new IllegalStateException("Fail loading private key", e);
        }

    }
}
