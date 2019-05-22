package com.qm.code.sercurity;

import com.qm.frame.qmsecurity.config.QmSecurityContent;
import com.qm.frame.qmsecurity.interceptor.QmSecurityInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright © 2018浅梦工作室}. All rights reserved.
 *
 * @author 浅梦
 * @date 2018/12/23 3:40
 * @Description 实现配置QmSecurity示例
 */
@Configuration
public class SecurityConfig implements WebMvcConfigurer {

    @Autowired
    private MyRealm myRealm;

    // 初始化QmSecurity安全框架
    // 重写WebMvcConfigurer的addInterceptors方法
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // setTokenSecret 设置token加密秘钥
        QmSecurityContent.setTokenSecret("shdioadnscoi21s90nbjio");
        // 设置请求头和响应头中携带token的字段名 默认为token
        QmSecurityContent.setHeaderTokenKeyName("token");
        // 设置加密次数 默认2次
        QmSecurityContent.setEncryptNumber(2);
        // 设置自定义的realm (这里需要注意的是,自定义的realm如果需要spring注入内容请交由Spring管理)
        QmSecurityContent.setRealm(myRealm);
        // 设置排除URI校验集合
        List<String> passUris = new ArrayList<>();
        //passUris.add("/**");
        QmSecurityContent.setPassUris(passUris);
        // 把框架添加到拦截器队列中,设置接管所有访问路径。
        QmSecurityInterceptor qmSecurityInterceptor = new QmSecurityInterceptor();
        // 添加拦截器
        InterceptorRegistration interceptor = registry.addInterceptor(qmSecurityInterceptor);
        // 添加拦截路径
        interceptor.addPathPatterns("/**");
        // 设置拦截器的优先级
        interceptor.order(2);
        // 添加静态路径排除
        interceptor.excludePathPatterns("/views/**");
    }

}
