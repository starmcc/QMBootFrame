package com.qm.frame.qmsecurity.config;

import com.qm.frame.qmsecurity.basic.QmSecurityRealm;
import com.qm.frame.qmsecurity.entity.QmErrorRedirectUrl;

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

    private int encryptNumber = 2;

    private boolean useRedirect = false;

    private QmErrorRedirectUrl qmErrorRedirectUrl;

    private QmSecurityRealm qmSecurityRealm;

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

    public QmErrorRedirectUrl getQmErrorRedirectUrl() {
        return qmErrorRedirectUrl;
    }

    public boolean isUseRedirect() {
        return useRedirect;
    }

    /**
     * 当校验失败或鉴权失败时，是否以视图名作为返回。默认为false
     * @param useRedirect
     */
    public void setUseRedirect(boolean useRedirect) {
        this.useRedirect = useRedirect;
    }

    /**
     * 当校验失败或鉴权失败时，返回视图名
     * @param qmErrorRedirectUrl
     */
    public void setQmErrorRedirectUrl(QmErrorRedirectUrl qmErrorRedirectUrl) {
        this.qmErrorRedirectUrl = qmErrorRedirectUrl;
    }

    /**
     * 校验机制
     * @param sessionOrToken
     */
    public void setSessionOrToken(String sessionOrToken) {
        this.sessionOrToken = sessionOrToken;
    }
    /**
     * 设置token加密次数，底层调用AES对称加密算法
     * @param encryptNumber
     */
    public void setEncryptNumber(int encryptNumber) {
        this.encryptNumber = encryptNumber;
    }

    /**
     * 设置token加密秘钥
     * @param secret 秘钥
     */
    public void setTokenSecret(String secret) {
        this.tokenSecret = secret;
    }

    /**
     * 设置请求头中携带的token
     * @param headerTokenKeyName
     */
    public void setHeaderTokenKeyName(String headerTokenKeyName) {
        this.headerTokenKeyName = headerTokenKeyName;
    }

    /**
     * 设置自定义的realm
     * @param qmSecurityRealm
     */
    public void setQmSecurityRealm(QmSecurityRealm qmSecurityRealm){
        this.qmSecurityRealm = qmSecurityRealm;
    }

}
