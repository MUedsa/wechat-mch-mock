package com.muedsa.mock.wechat.mch.filter;

import com.muedsa.mock.wechat.mch.cert.MerchantManager;
import com.muedsa.mock.wechat.mch.container.AuthorizationContainer;
import com.muedsa.mock.wechat.mch.container.ErrorContainer;
import com.muedsa.mock.wechat.mch.exception.WechatMchException;
import com.muedsa.mock.wechat.mch.util.AuthorizationUtil;
import com.muedsa.mock.wechat.mch.util.ValidateUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.util.Base64Utils;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.X509Certificate;
import java.util.Map;

@Slf4j
public class AuthorizationFilter extends OncePerRequestFilter {

    private final MerchantManager merchantManager;

    public AuthorizationFilter(MerchantManager merchantManager) {
        this.merchantManager = merchantManager;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader(AuthorizationContainer.HTTP_HEAD_KEY);
        ValidateUtil.state(StringUtils.hasText(authorization), ErrorContainer.CODE_SIGN_ERROR,
                ErrorContainer.MESSAGE_SIGN_FORMAT_ERROR, ErrorContainer.HTTP_STATUS_SING_ERROR);
        Map<String, String> paramMap = AuthorizationUtil.resolveAuthorization(authorization,
                AuthorizationContainer.SUCCESS_FLAG);
        ValidateUtil.state(AuthorizationContainer.SUCCESS_FLAG.equals(paramMap.get(AuthorizationContainer.SUCCESS_FLAG)),
                ErrorContainer.CODE_SIGN_ERROR, ErrorContainer.MESSAGE_SIGN_FORMAT_ERROR, ErrorContainer.HTTP_STATUS_SING_ERROR);
        String mchId = paramMap.get(AuthorizationContainer.FIELD_MCH_ID);
        String serialNo = paramMap.get(AuthorizationContainer.FIELD_SERIAL_NO);
        String nonceStr = paramMap.get(AuthorizationContainer.FIELD_NONCE_STR);
        String timestampStr = paramMap.get(AuthorizationContainer.FIELD_TIMESTAMP);
        String signature = paramMap.get(AuthorizationContainer.FIELD_SIGNATURE);
        ValidateUtil.hasLength(mchId, ErrorContainer.CODE_SIGN_ERROR, ErrorContainer.MESSAGE_SIGN_ERROR, ErrorContainer.HTTP_STATUS_SING_ERROR);
        ValidateUtil.hasLength(serialNo, ErrorContainer.CODE_SIGN_ERROR, ErrorContainer.MESSAGE_SIGN_ERROR, ErrorContainer.HTTP_STATUS_SING_ERROR);
        ValidateUtil.hasLength(nonceStr, ErrorContainer.CODE_SIGN_ERROR, ErrorContainer.MESSAGE_SIGN_ERROR, ErrorContainer.HTTP_STATUS_SING_ERROR);
        ValidateUtil.hasLength(timestampStr, ErrorContainer.CODE_SIGN_ERROR, ErrorContainer.MESSAGE_SIGN_ERROR, ErrorContainer.HTTP_STATUS_SING_ERROR);
        ValidateUtil.hasLength(signature, ErrorContainer.CODE_SIGN_ERROR, ErrorContainer.MESSAGE_SIGN_ERROR, ErrorContainer.HTTP_STATUS_SING_ERROR);
        MerchantManager.Merchant mch = merchantManager.getMch(mchId);
        ValidateUtil.notNull(mch, ErrorContainer.CODE_SIGN_ERROR, ErrorContainer.MESSAGE_SIGN_INFO_ERROR, ErrorContainer.HTTP_STATUS_SING_ERROR);
        X509Certificate certificate = mch.getCertificate(serialNo);
        ValidateUtil.notNull(certificate, ErrorContainer.CODE_SIGN_ERROR, ErrorContainer.MESSAGE_SIGN_ERROR, ErrorContainer.HTTP_STATUS_SING_ERROR);
        CachedBodyHttpServletRequest cachedBodyHttpServletRequest;
        if(request instanceof CachedBodyHttpServletRequest) {
            cachedBodyHttpServletRequest = (CachedBodyHttpServletRequest) request;
        }else{
            cachedBodyHttpServletRequest = new CachedBodyHttpServletRequest(request);
        }
        String method = request.getMethod();
        String url = request.getRequestURI();
        try {
            Signature sign = Signature.getInstance(certificate.getSigAlgName());
            sign.initVerify(certificate);
            sign.update(buildSignatureMessage(method, url, nonceStr, timestampStr, cachedBodyHttpServletRequest.getCachedBody()));
            boolean success = sign.verify(Base64Utils.decodeFromString(signature));
            ValidateUtil.state(success, ErrorContainer.CODE_SIGN_ERROR, ErrorContainer.MESSAGE_SIGN_ERROR, ErrorContainer.HTTP_STATUS_SING_ERROR);
            cachedBodyHttpServletRequest.setAttribute(AuthorizationContainer.REQUEST_ATTR_MCH, mch);
            MDC.put(AuthorizationContainer.FIELD_MCH_ID, mchId);
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            log.error("Authorization verify fail", e);
            throw new WechatMchException(ErrorContainer.CODE_SIGN_ERROR, ErrorContainer.MESSAGE_SIGN_ERROR, ErrorContainer.HTTP_STATUS_SING_ERROR);
        }
        filterChain.doFilter(cachedBodyHttpServletRequest, response);
    }

    public byte[] buildSignatureMessage(String method, String url, String nonceStr, String timestampStr, byte[] body) {
        String message = method + "\n"
                + url + "\n"
                + timestampStr + "\n"
                + nonceStr + "\n";
        byte[] messageWithoutBodyBytes = message.getBytes(StandardCharsets.UTF_8);
        byte[] messageBytes = new byte[messageWithoutBodyBytes.length + body.length + 1];
        System.arraycopy(messageWithoutBodyBytes, 0, messageBytes, 0, messageWithoutBodyBytes.length);
        System.arraycopy(body, 0, messageBytes, messageWithoutBodyBytes.length, body.length);
        messageBytes[messageBytes.length - 1] = '\n';
        return messageBytes;
    }


    public static class CachedBodyHttpServletRequest extends HttpServletRequestWrapper {
        private final byte[] cachedBody;

        public CachedBodyHttpServletRequest(HttpServletRequest request) throws IOException {
            super(request);
            InputStream requestInputStream = request.getInputStream();
            this.cachedBody = StreamUtils.copyToByteArray(requestInputStream);
        }

        @Override
        public ServletInputStream getInputStream() {
            return new CachedBodyServletInputStream(this.cachedBody);
        }

        @Override
        public BufferedReader getReader() {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.cachedBody);
            return new BufferedReader(new InputStreamReader(byteArrayInputStream));
        }

        public byte[] getCachedBody() {
            return cachedBody;
        }

        public static class CachedBodyServletInputStream extends ServletInputStream {

            private InputStream cachedBodyInputStream;

            public CachedBodyServletInputStream(byte[] cachedBody) {
                this.cachedBodyInputStream = new ByteArrayInputStream(cachedBody);
            }

            @Override
            public boolean isFinished() {
                boolean flag;
                try {
                    flag =  cachedBodyInputStream.available() == 0;
                } catch (IOException e) {
                    flag = true;
                    log.error("CachedBodyServletInputStream.isFinished()", e);
                }
                return flag;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener listener) {

            }

            @Override
            public int read() throws IOException {
                return cachedBodyInputStream.read();
            }
        }

    }


}
