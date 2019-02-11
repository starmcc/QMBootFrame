package com.qm.code.sercurity;

import com.qm.frame.qmsecurity.config.QmSecurityContent;
import com.qm.frame.qmsecurity.entity.QmErrorRedirectUrl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Copyright © 2018浅梦工作室}. All rights reserved.
 *
 * @author 浅梦
 * @date 2018/12/23 3:40
 * @Description 实现配置QmSecurity示例
 */
@Configuration
public class SecurityConfig {

    @Bean
    public MyRealm settingRealm(){
        return new MyRealm();
    }

    @Bean
    public QmSecurityContent setQmSecurityBasic(MyRealm myRealm) {
        // 创建一个QmSecurityContent初始配置
        QmSecurityContent qmSecurityContent = new QmSecurityContent();
        // 设置使用哪种校验机制 session or token
        qmSecurityContent.setSessionOrToken("session");
        // setTokenSecret 设置token加密秘钥
        qmSecurityContent.setTokenSecret("key2018s2312tarmcc");
        // setQmSecurityRealm 设置自定义的Realm
        qmSecurityContent.setQmSecurityRealm(myRealm);
        // 设置加密次数
        qmSecurityContent.setEncryptNumber(1);

        // ========下列为选择性设置==========
        // 设置是否使用重定向返回
        qmSecurityContent.setUseRedirect(true);
        // 如果设置了重定向返回则需设置重定向路径
        QmErrorRedirectUrl qmErrorRedirectUrl = new QmErrorRedirectUrl();
        // 当登录超时重定向的路径
        qmErrorRedirectUrl.setNotLoginRedirectUrl("/mvc/index");
        // 当访问权限不足时重定向返回的路径
        qmErrorRedirectUrl.setPermissionDeniedRedirectUrl("/mvc/index");
        // 把对象设置到配置中
        qmSecurityContent.setQmErrorRedirectUrl(qmErrorRedirectUrl);
        // ========End======================
        // 返回该对象交由Spring容器
        return qmSecurityContent;
    }

}
