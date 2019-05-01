package com.qm.code.druid;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.qm.frame.basic.config.QmDataSourceFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Copyright © 2019浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2019/5/2 0:16
 * @Description Druid servlet and filter setting config
 */
@Configuration
public class DruidConfig {

    /**
     * 注册一个StatViewServlet，Druid依赖
     *
     * @return ServletRegistrationBean<StatViewServlet>
     */
    @Bean
    public ServletRegistrationBean<StatViewServlet> setDruidStatViewServlet() {
        return QmDataSourceFactory.getServletRegistrationBean();
    }

    /**
     * 注册一个FilterRegistrationBean，Druid依赖
     *
     * @return FilterRegistrationBean<WebStatFilter>
     */
    @Bean
    public FilterRegistrationBean<WebStatFilter> setDruidWebStatFilter() {
        return QmDataSourceFactory.getFilterFilterRegistrationBean();
    }


}