package com.qm.frame.qmsecurity.config;

import com.qm.frame.qmsecurity.basic.QmSecurityRealm;
import com.qm.frame.qmsecurity.manager.QmSecuritySessionEvent;

/**
 * Copyright © 2018浅梦工作室}. All rights reserved.
 *
 * @author 浅梦
 * @date 2018/12/23 18:26
 * @Description 提供给调用者注入使用
 */
public class QmSecurityContent {

    private String sessionOrToken = "token";

    private String tokenSecret = "tokenSecret";

    private String headerTokenKeyName = "token";

    private boolean startRedis = false;

    private int encryptNumber = 2;

    private String[] excludePathPatterns;

    private QmSecurityRealm qmSecurityRealm;

    private QmSecuritySessionEvent qmSecuritySessionEvent;

    public boolean isStartRedis() {
        return startRedis;
    }

    public QmSecuritySessionEvent getQmSecuritySessionEvent() {
        return qmSecuritySessionEvent;
    }

    public String getTokenSecret() {
        return tokenSecret;
    }

    public String getHeaderTokenKeyName() {
        return headerTokenKeyName;
    }

    public int getEncryptNumber() {
        return encryptNumber;
    }

    public QmSecurityRealm getQmSecurityRealm() {
        return qmSecurityRealm;
    }

    public String getSessionOrToken() {
        return sessionOrToken;
    }

    public String[] getExcludePathPatterns() {
        return excludePathPatterns;
    }

    /**
     * 静态资源排除拦截路径
     *
     * @param excludePathPatterns
     */
    public void setExcludePathPatterns(String... excludePathPatterns) {
        this.excludePathPatterns = excludePathPatterns;
    }

    /**
     * 设置session监听器的回调
     *
     * @param qmSecuritySessionEvent
     */
    public void setQmSecuritySessionEvent(QmSecuritySessionEvent qmSecuritySessionEvent) {
        this.qmSecuritySessionEvent = qmSecuritySessionEvent;
    }

    /**
     * 校验机制
     *
     * @param sessionOrToken
     */
    public void setSessionOrToken(String sessionOrToken) {
        this.sessionOrToken = sessionOrToken;
    }

    /**
     * 设置token加密次数，底层调用AES对称加密算法
     *
     * @param encryptNumber
     */
    public void setEncryptNumber(int encryptNumber) {
        this.encryptNumber = encryptNumber;
    }

    /**
     * 设置token加密秘钥
     *
     * @param secret 秘钥
     */
    public void setTokenSecret(String secret) {
        this.tokenSecret = secret;
    }

    /**
     * 设置请求头中携带的token
     *
     * @param headerTokenKeyName
     */
    public void setHeaderTokenKeyName(String headerTokenKeyName) {
        this.headerTokenKeyName = headerTokenKeyName;
    }

    /**
     * 设置自定义的realm
     *
     * @param qmSecurityRealm
     */
    public void setQmSecurityRealm(QmSecurityRealm qmSecurityRealm) {
        this.qmSecurityRealm = qmSecurityRealm;
    }


    /**
     * 设置是否开启Redis管理权限
     * @param startRedis
     */
    public void setStartRedis(boolean startRedis) {
        this.startRedis = startRedis;
    }

}
