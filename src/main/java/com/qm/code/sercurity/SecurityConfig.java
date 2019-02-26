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
        // 需要排除的静态资源路径
        qmSecurityContent.setExcludePathPatterns("/views/**");
        return qmSecurityContent;
    }

}
