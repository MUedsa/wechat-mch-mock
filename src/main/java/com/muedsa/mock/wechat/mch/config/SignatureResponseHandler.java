package com.muedsa.mock.wechat.mch.config;

import com.muedsa.mock.wechat.mch.cert.MerchantManager;
import com.muedsa.mock.wechat.mch.cert.PlatformSecretManager;
import com.muedsa.mock.wechat.mch.container.AuthorizationContainer;
import com.muedsa.mock.wechat.mch.util.ByteArraySupplier;
import com.muedsa.mock.wechat.mch.util.ContextUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.*;

@ControllerAdvice
@Slf4j
public class SignatureResponseHandler implements ResponseBodyAdvice<Object> {

    private final List<HttpMessageConverter<Object>> supportConverterList = new ArrayList<>();

    @Override
    @SuppressWarnings("unchecked")
    public boolean supports(MethodParameter returnType, Class converterType) {
        return supportConverterList.stream().anyMatch(converter -> converterType.isAssignableFrom(converter.getClass()));
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if(request instanceof ServletServerHttpRequest servletServerHttpRequest
                && response instanceof ServletServerHttpResponse servletServerHttpResponse) {
            HttpServletRequest httpServletRequest = servletServerHttpRequest.getServletRequest();
            HttpServletResponse httpServletResponse = servletServerHttpResponse.getServletResponse();
            trySignResponse(httpServletRequest, httpServletResponse, () -> {
                OnlyBodyHttpOutputMessage outputMessage = new OnlyBodyHttpOutputMessage();
                if(Objects.nonNull(body)) {
                    supportConverterList.stream()
                            .filter(converter -> selectedConverterType.isAssignableFrom(converter.getClass()))
                            .findAny()
                            .ifPresent(converter -> {
                                try {
                                    converter.write(body, selectedContentType, outputMessage);
                                } catch (IOException e) {
                                    log.error("sign response write body error", e);
                                }
                            });
                }
                return outputMessage.bodyToByteArray();
            });
        }
        return body;
    }

    public SignatureResponseHandler(MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter) {
        supportConverterList.add(mappingJackson2HttpMessageConverter);
    }

    static class OnlyBodyHttpOutputMessage implements HttpOutputMessage {

        private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        private HttpHeaders httpHeaders = new HttpHeaders();

        @Override
        public OutputStream getBody() {
            return outputStream;
        }

        @Override
        public HttpHeaders getHeaders() {
            return httpHeaders;
        }

        public byte[] bodyToByteArray() {
            return outputStream.toByteArray();
        }
    }

    public static void trySignResponse(HttpServletRequest request, HttpServletResponse response, ByteArraySupplier bodyHandler) {
        MerchantManager.Merchant mch = (MerchantManager.Merchant) request.getAttribute(AuthorizationContainer.REQUEST_ATTR_MCH);
        if(Objects.nonNull(mch)) {
            PlatformSecretManager platformSecretManager = ContextUtil.getBean(PlatformSecretManager.class);
            if(Objects.nonNull(platformSecretManager)) {
                PrivateKey privateKey = platformSecretManager.getPrivateKey(mch.getMchId());
                Optional<X509Certificate> certOptional = platformSecretManager.getCertificates(mch.getMchId()).stream().max(Comparator.comparingLong(c -> c.getNotAfter().getTime()));
                if(Objects.nonNull(privateKey) && certOptional.isPresent()) {
                    long timestamp = System.currentTimeMillis() / 1000;
                    String timestampStr = String.valueOf(timestamp);
                    String nonceStr = RandomStringUtils.randomAlphanumeric(32);
                    byte[] bodyByteArray = bodyHandler.get();
                    try {
                        Signature sign = Signature.getInstance("SHA256withRSA");
                        sign.initSign(privateKey);
                        byte[] dataWithoutBody = (timestampStr + "\n"
                                + nonceStr + "\n").getBytes(StandardCharsets.UTF_8);
                        byte[] finalData = new byte[dataWithoutBody.length + bodyByteArray.length + 1];
                        System.arraycopy(dataWithoutBody, 0, finalData, 0, dataWithoutBody.length);
                        System.arraycopy(bodyByteArray, 0, finalData, dataWithoutBody.length, bodyByteArray.length);
                        finalData[dataWithoutBody.length + bodyByteArray.length] = '\n';
                        sign.update(finalData);
                        String signStr = Base64Utils.encodeToString(sign.sign());
                        response.setHeader(AuthorizationContainer.HTTP_HEAD_KEY_SERIAL, certOptional.get().getSerialNumber().toString(16));
                        response.setHeader(AuthorizationContainer.HTTP_HEAD_KEY_TIMESTAMP, timestampStr);
                        response.setHeader(AuthorizationContainer.HTTP_HEAD_KEY_NONCE, nonceStr);
                        response.setHeader(AuthorizationContainer.HTTP_HEAD_KEY_SIGNATURE, signStr);
                        response.setHeader(AuthorizationContainer.HTTP_HEAD_KEY_REQUEST_ID, UUID.randomUUID().toString());
                    } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
                        log.error("try sign response error", e);
                    }
                }
            }
        }
    }
}
