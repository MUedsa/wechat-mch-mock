package com.muedsa.mock.wechat.mch.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.muedsa.mock.wechat.mch.config.GlobalExceptionHandler;
import com.muedsa.mock.wechat.mch.config.SignatureResponseHandler;
import com.muedsa.mock.wechat.mch.dto.ErrorResponse;
import com.muedsa.mock.wechat.mch.exception.WechatMchException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@Slf4j
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (WechatMchException e) {
            log.error("filter error", e);
            resolveWechatMchException(e, request, response);
        }
    }

    private void resolveWechatMchException(WechatMchException e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ErrorResponse errorResponse = GlobalExceptionHandler.buildErrorResponse(e.getCode(), e.getMessage());
        response.setStatus(e.getHttpStatus().value());
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(errorResponse);
        writer.write(body);
        SignatureResponseHandler.trySignResponse(request, response, () -> body.getBytes(StandardCharsets.UTF_8));
        writer.flush();
    }
}
