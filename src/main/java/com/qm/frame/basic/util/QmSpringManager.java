package com.qm.frame.basic.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Copyright © 2018浅梦工作室. All rights reserved.
 * @author 浅梦
 * @date 2018年11月24日 上午2:11:03
 * @Description Spring工具管理器
 */
public class QmSpringManager implements ApplicationContextAware {

	private static final Logger LOG = LoggerFactory.getLogger(QmSpringManager.class);
	
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if(QmSpringManager.applicationContext == null) {
        	QmSpringManager.applicationContext = applicationContext;
        }
        LOG.info("※※※※※※QmSpringManager设置ApplicationContext成功※※※※※※");
    }

    /**
     * @Title getApplicationContext
     * @return
     * @Description 获取applicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * @Title getBean
     * @param name
     * @return
     * @Description 通过name获取 Bean.
     */
    public static Object getBean(String name){
        return getApplicationContext().getBean(name);
    }

    /**
     * @Title getBean
     * @param clazz
     * @return
     * @Description 通过class获取Bean.
     */
    public static <T> T getBean(Class<T> clazz){
        return getApplicationContext().getBean(clazz);
    }

    /**
     * @Title getBean
     * @param name
     * @param clazz
     * @return
     * @Description 通过name,以及Clazz返回指定的Bean
     */
    public static <T> T getBean(String name,Class<T> clazz){
        return getApplicationContext().getBean(name, clazz);
    }

}