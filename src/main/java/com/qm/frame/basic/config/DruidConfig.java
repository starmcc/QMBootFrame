package com.qm.frame.basic.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.qm.frame.basic.config.QmFrameConcent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Copyright © 2018浅梦工作室. All rights reserved.
 * @author 浅梦
 * @date 2018年11月24日 上午2:14:29
 * @Description Druid连接池配置类
 */
public @Configuration class DruidConfig {
	
	/**
	 * 注入该DruidDataSource
	 * @return DataSource
	 */
    @Bean(destroyMethod = "close", initMethod = "init")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource druidDataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        return druidDataSource;
    }

    /**
     * 注册一个StatViewServlet，Druid依赖
     * @return ServletRegistrationBean<StatViewServlet>
     */
    @Bean
    public ServletRegistrationBean<StatViewServlet> druidStatViewServlet(){
        //org.springframework.boot.context.embedded.ServletRegistrationBean提供类的进行注册.
        ServletRegistrationBean<StatViewServlet> servletRegistrationBean = new ServletRegistrationBean<StatViewServlet>(new StatViewServlet(),"/druid/*");

        //添加初始化参数：initParams
        String allow = QmFrameConcent.DRUID_ALLOW;
        String deny = QmFrameConcent.DRUID_DENY;
        String loginUsername = QmFrameConcent.DRUID_LOGIN_USERNAME;
        String loginPassword = QmFrameConcent.DRUID_LOGIN_PASSWORD;
        //白名单：
        servletRegistrationBean.addInitParameter("allow",allow);
        //IP黑名单 (存在共同时，deny优先于allow) : 如果满足deny的话提示:Sorry, you are not permitted to views this page.
        servletRegistrationBean.addInitParameter("deny",deny);
        //登录查看信息的账号密码.
        servletRegistrationBean.addInitParameter("loginUsername",loginUsername);
        servletRegistrationBean.addInitParameter("loginPassword",loginPassword);
        //是否能够重置数据.
        servletRegistrationBean.addInitParameter("resetEnable", QmFrameConcent.DRUID_RESET_ENABLE);
        return servletRegistrationBean;
    }

    /**
     * 注册一个FilterRegistrationBean，Druid依赖
     * @return  FilterRegistrationBean<WebStatFilter>
     */
    @Bean
    public FilterRegistrationBean<WebStatFilter> druidStatFilter(){
        FilterRegistrationBean<WebStatFilter> filterRegistrationBean = new FilterRegistrationBean<WebStatFilter>(new WebStatFilter());
        //添加过滤规则.
        filterRegistrationBean.addUrlPatterns("/*");
        //添加不需要忽略的格式信息.
        filterRegistrationBean.addInitParameter("exclusions","*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }
}
