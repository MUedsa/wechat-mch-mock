package com.muedsa.mock.wechat.mch.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ContextUtil implements ApplicationContextAware {

    private static ApplicationContext CONTEXT;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        CONTEXT = applicationContext;
    }

    public static <T> T getBean(Class<T> clazz) {
        T bean = null;
        if(Objects.nonNull(CONTEXT)) {
            bean = CONTEXT.getBean(clazz);
        }
        return bean;
    }
}
