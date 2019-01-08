package com.qm.code.sercurity;

import com.qm.frame.qmsecurity.basic.QmSecurityBasic;
import com.qm.frame.qmsecurity.basic.QmSercurityContent;
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
    public QmSercurityContent setQmSecurityBasic(MyRealm myRealm) {
        // 创建一个QmSecurityContent初始配置
        QmSercurityContent qmSercurityContent = new QmSercurityContent();
        // setTokenSecret 设置token加密秘钥
        qmSercurityContent.setTokenSecret("key2018s2312tarmcc");
        // setQmSecurityRealm 设置自定义的Realm
        qmSercurityContent.setQmSecurityRealm(myRealm);
        // 设置加密次数
        qmSercurityContent.setEncryptNumber(1);
        // 返回该对象交由Spring容器
        return qmSercurityContent;
    }

}
