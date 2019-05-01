package com.qm.frame.qmsecurity.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Copyright © 2018浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2018年11月24日 上午2:11:03
 * @Description Spring工具管理器
 */
@Component
public class QmSecuritySpringMapnager implements ApplicationContextAware {

    private static final Logger LOG = LoggerFactory.getLogger(QmSecuritySpringMapnager.class);

    private static ApplicationContext applicationContext;

    /**
     * @return
     * @Title getApplicationContext
     * @Description 获取applicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (QmSecuritySpringMapnager.applicationContext == null) {
            QmSecuritySpringMapnager.applicationContext = applicationContext;
            LOG.info("※※※※※※QmSpringSecurity设置ApplicationContext成功※※※※※※");
        }
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

}