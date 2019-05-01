package com.qm.frame.basic.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

/**
 * Copyright © 2018浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2018年11月24日 上午2:11:03
 * @Description Spring工具管理器
 */
@Component
public class QmSpringManager implements ApplicationContextAware {

    private static final Logger LOG = LoggerFactory.getLogger(QmSpringManager.class);
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (QmSpringManager.applicationContext == null) {
            QmSpringManager.applicationContext = applicationContext;
            LOG.info("※※※※※※QmSpringManager设置ApplicationContext成功※※※※※※");
        }
    }

    /**
     * @return
     * @Title getApplicationContext
     * @Description 获取applicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * @param name
     * @return
     * @Title getBean
     * @Description 通过name获取 Bean.
     */
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    /**
     * @param clazz
     * @return
     * @Title getBean
     * @Description 通过class获取Bean.
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    /**
     * @param name
     * @param clazz
     * @return
     * @Title getBean
     * @Description 通过name, 以及Clazz返回指定的Bean
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

    /**
     * Spring提供的模糊路径匹配算法
     *
     * @param matchingUrl 匹配路径
     * @param requestUrl  请求地址
     * @return
     */
    public static boolean verifyMatchURI(String matchingUrl, String requestUrl) {
        return new AntPathMatcher().match(matchingUrl, requestUrl);
    }
}