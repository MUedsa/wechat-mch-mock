package com.muedsa.mock.wechat.mch.config;

import com.muedsa.mock.wechat.mch.cert.MerchantManagerImpl;
import com.muedsa.mock.wechat.mch.filter.AuthorizationFilter;
import com.muedsa.mock.wechat.mch.filter.ExceptionHandlerFilter;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class FilterRegisterConfig {

    @Bean("exceptionHandlerFilterRegistrationBean")
    public FilterRegistrationBean<Filter> registerExceptionHandlerFilter() {
        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new ExceptionHandlerFilter());
        bean.setOrder(Integer.MIN_VALUE);
        bean.addUrlPatterns("/*");
        return bean;
    }

    @Bean("authorizationFilterRegistrationBean")
    @DependsOn(value = MerchantManagerImpl.NAME)
    public FilterRegistrationBean<Filter> registerAuthorizationFilter(MerchantManagerImpl mchManagerImpl) {
        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new AuthorizationFilter(mchManagerImpl));
        bean.setOrder(1);
        bean.addUrlPatterns("/*");
        return bean;
    }
}
