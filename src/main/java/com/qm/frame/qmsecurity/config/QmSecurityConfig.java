package com.qm.frame.qmsecurity.config;

import com.qm.frame.qmsecurity.basic.QmSecurityInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Copyright © 2018浅梦工作室}. All rights reserved.
 *
 * @author 浅梦
 * @date 2018/12/23 2:42
 * @Description QmSecurity基础设置
 */
@Configuration
public class QmSecurityConfig implements WebMvcConfigurer {

    // 不确定调用者是否启用该安全框架，如果启用的话，则会进行配置，这里就能够获取到Spring容器中的对象了。
    @Autowired(required = false)
    private QmSecurityContent qmSecurityContent;

    // 重写WebMvcConfigurer的addInterceptors方法
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 首先判断是否注入了该框架，如果注入了，则继续，否则不进行操作。
        if (qmSecurityContent == null) return;
        // 把框架添加到拦截器队列中,设置接管所有访问路径。
        QmSecurityInterceptor qmSecurityInterceptor = new QmSecurityInterceptor(qmSecurityContent);
        // 添加拦截器
        InterceptorRegistration interceptor = registry.addInterceptor(qmSecurityInterceptor);
        // 添加拦截路径
        interceptor.addPathPatterns("/**");
        // 检索静态路径
        String[] excludePathPatterns = qmSecurityContent.getExcludePathPatterns();
        // 添加静态路径排除
        if (excludePathPatterns != null && excludePathPatterns.length != 0) {
            interceptor.excludePathPatterns(excludePathPatterns);
        }
    }


}
