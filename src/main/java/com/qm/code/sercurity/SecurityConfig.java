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
 * @Description: 实现配置QmSecurity示例
 */
@Configuration
public class SecurityConfig {

    @Bean
    public QmSercurityContent setQmSecurityBasic() {
        QmSercurityContent qmSercurityContent = new QmSercurityContent();
        qmSercurityContent.setTokenSecret("test");
        qmSercurityContent.setQmSecurityRealm(new MyRealm());
        qmSercurityContent.setTokenSecret("ashdiowaoi");
        // x
        qmSercurityContent.setEncryptNumber(1);
        return qmSercurityContent;
    }

}
