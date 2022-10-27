package com.muedsa.demo.wechat.mch;

import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.exception.HttpCodeException;
import com.wechat.pay.contrib.apache.httpclient.exception.NotFoundException;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;


public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    private static String API_HOST = "http://api.mch.weixin.qq.com";

    private static String MCH_ID = "1900006000";
    private static String MCH_API_V3_KEY = "w1eYeoNrCF3oYpWtrAbDg2TAfV2vjZ6x";

    private static String MCH_SERIAL_NUMBER = "3b6b459e";

    private static String PRIVATE_KEY_BASE64 = "-----BEGIN PRIVATE KEY-----\n" +
            "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCyl2frP275g4qD\n" +
            "tm4zzdSj5bS3g15nZLE5fOiDIdL/Jzv0wApWGoguEwHlT9sCV+4PeOSvqGu48sma\n" +
            "2XqhMOpYsxDh13be5aL7S+y8uNz3lysccQaO1qa7cKr6Y8LmQgQppMt8OZFjFPKz\n" +
            "gzRMfvz8mJ9WtviroG2eS0VotNFVMBxAZqovZiW9YtUQ8D24BNoqWGBQb9R9suD+\n" +
            "V4kU/wJ+1obL98Ch5p+/KGeX+pGAobkq8xLFLaYpuTgR53X3D2w8eJsalDxlRuaK\n" +
            "tZZbYqaNFVUQnOQ28OFzCNAkengy3PD13zSpfDZy7bB+BFEUyg6iXOnndnCEyM8i\n" +
            "XnwMwOUzAgMBAAECggEALdc7L2Eq5RTkmwZapyVwLVmq12f69WMdMnpjXDV0aVXy\n" +
            "x9H7VTqeNeVfGTM0LhfP3VzUpln+vIxlP0APgAyEOpxL+wxJUNF00jgc3hw9a7f7\n" +
            "suM4IbXrOjWjTX4J6vyLAP1x1cMZFGuuGHHXHMODwHqUe7efgL4/JOCBQm0XRlwS\n" +
            "nKdwC6ZDxYdBBAqDaQ7fNjrEFZ0e9szHwsoN+f+nUi0UQu6pFCqyAGI+S4wsjQ/Q\n" +
            "0SbMwJCK1u2eba6bSDdPAnbOFWAgsRzwKwJWddOmXgBISOENs8EXlexp1ee8QOQa\n" +
            "kKPTNWP190IBb2wRui0dgvLBVAafZkXM1jC/oIaDIQKBgQDuHyMn13uppC8Wj8gX\n" +
            "sZKjkXFy1gHK2I+zuXG1Y/vrXVyC/jM5YSQUiJcFPZGPpfusQpC1NQG0gQmzRYh5\n" +
            "Zwrxz0I1f1sC7HKUzPE2h3bOAvWnUFLrzx4OSIJc6F6DH7h1J+YTp1hTDgciJNsT\n" +
            "oe60OvmpRE+R2/Yac69vUGSO+QKBgQDAAA6R2XLBzQEeyQ37TJExf2WRhjiFlUDn\n" +
            "k5QX/WHo7lZlzwBlfIpTSQIzi5aRpZ96X12ebsNnfGyZWn4zF4qfeJk3IJejETME\n" +
            "hm0XS6f4Sjpd9bb8A+OTLGmKDqTTvMoI/Mc/AAW0iLl/7h/BwG0eE/zzpuVwvYBj\n" +
            "maDO7j5kiwKBgQC4l0K1fAHNPA2Uu9ktJOa8Dd+YWfYBvWeIt1VCfEOp7THuCgjD\n" +
            "jl5d3JTV5yiT5uP2YZO0JWcw+mb+pgRpuEtvsG9u8U8oMQuR9l13HAQpJ3ovPcFK\n" +
            "M8Dk6ajFRQ0fMNPZXhipW6zIlbNJ8LAzw/A7nwwy/1V/iLh7QPram8OYYQKBgDqp\n" +
            "CcqHzCbn8nqsZMgtiYK88JQ4lNZDVpQqI6n8sUhQYTczumRHW5+8g56p/DO9jW7+\n" +
            "6gR4xNy+SBPGcR49CA5W80DDhXC91X+Bbp0acVRLoW+JngF7UrFpdUlH1hYW7Qc3\n" +
            "68wUvql8yGgMock5eF0jMDpk+F6slyhXDKmMYqWnAoGAZ6vXhqmaI23zPPiMgUyw\n" +
            "Vsn6vxJ5D4xLu0rHOqDjloGd7gUtpb8jPbd3y/qcyj6A54TlKaXHEQEapHrUAJoH\n" +
            "wL5aHU+GthjSLiCUUV7JXFCGRKD46IfKK0P3/dw/DC76J8YpfCTWs8xKfzF1PhzN\n" +
            "+AJux8cg0zflMDe/anmZg/k=\n" +
            "-----END PRIVATE KEY-----";

    private static PrivateKey MCH_PRIVATE_KEY;

    public static void main(String[] args) throws Exception {
        initPrivateKey();
        initCertificatesManager();
        CloseableHttpClient httpClient = getClient();

        URIBuilder uriBuilder = new URIBuilder("http://api.mch.weixin.qq.com/v3/certificates");
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        httpGet.addHeader("Accept", "application/json");
        CloseableHttpResponse response = httpClient.execute(httpGet);
        String bodyAsString = EntityUtils.toString(response.getEntity());
        log.info("test response:{}", bodyAsString);
    }

    private static void initPrivateKey() {
        MCH_PRIVATE_KEY = PemUtil.loadPrivateKey(PRIVATE_KEY_BASE64);
    }

    private static void initCertificatesManager() throws GeneralSecurityException, IOException, HttpCodeException, IllegalArgumentException {
        CertificatesManager.updateCertDownloadPath(API_HOST + "/v3/certificates");
        CertificatesManager certificatesManager = CertificatesManager.getInstance();
        certificatesManager.putMerchant(MCH_ID, new WechatPay2Credentials(MCH_ID,
                new PrivateKeySigner(MCH_SERIAL_NUMBER, MCH_PRIVATE_KEY)), MCH_API_V3_KEY.getBytes(StandardCharsets.UTF_8));
    }

    private static CloseableHttpClient getClient() throws NotFoundException {
        Verifier verifier = CertificatesManager.getInstance().getVerifier(MCH_ID);
        return WechatPayHttpClientBuilder.create()
                .withMerchant(MCH_ID, MCH_SERIAL_NUMBER, MCH_PRIVATE_KEY)
                .withValidator(new WechatPay2Validator(verifier))
                .build();
    }
}
