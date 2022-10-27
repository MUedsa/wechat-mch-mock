package com.muedsa.mock.wechat.mch.util;

import org.springframework.util.Base64Utils;

import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;

public class PemUtil {

    public static final String BEGIN_BOUNDARY = "-----BEGIN ";
    public static final String END_BOUNDARY   = "-----END ";
    public static final String FINISH_BOUNDARY = "-----";

    public static final String PRIVATE_KEY = "PRIVATE KEY";
    public static final String EC_PRIVATE_KEY = "EC PRIVATE KEY";
    public static final String ENCRYPTED_PRIVATE_KEY = "ENCRYPTED PRIVATE KEY";
    public static final String RSA_PRIVATE_KEY = "RSA PRIVATE KEY";
    public static final String CERTIFICATE = "CERTIFICATE";
    public static final String X509_CERTIFICATE = "X509 CERTIFICATE";

    public static String toPEM(X509Certificate certificate) throws CertificateEncodingException {
        return BEGIN_BOUNDARY + CERTIFICATE + FINISH_BOUNDARY +
                System.lineSeparator() +
                Base64Utils.encodeToString(certificate.getEncoded()) +
                END_BOUNDARY + CERTIFICATE + FINISH_BOUNDARY;
    }
}
