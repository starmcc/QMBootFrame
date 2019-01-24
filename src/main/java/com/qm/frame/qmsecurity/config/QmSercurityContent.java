package com.qm.frame.qmsecurity.config;

import com.qm.frame.qmsecurity.basic.QmSecurityRealm;

/**
 * Copyright © 2018浅梦工作室}. All rights reserved.
 *
 * @author 浅梦
 * @date 2018/12/23 18:26
 * @Description 提供给调用者注入使用
 */
public class QmSercurityContent {

    private static String TOKEN_SECRET = "token_secret";

    private static String HEADER_TOKEN_KEY_NAME = "token";

    private static int ENCRYPT_NUMBER = 2;

    private static QmSecurityRealm qmSecurityRealm;

    public static String getTokenSecret() {
        return TOKEN_SECRET;
    }

    public static String getHeaderTokenKeyName() {
        return HEADER_TOKEN_KEY_NAME;
    }

    public static int getEncryptNumber() {
        return ENCRYPT_NUMBER;
    }

    public static QmSecurityRealm getQmSecurityRealm() {
        return qmSecurityRealm;
    }

    /**
     * 设置token加密次数，底层调用AES对称加密算法
     * @param encryptNumber
     */
    public void setEncryptNumber(int encryptNumber) {
        ENCRYPT_NUMBER = encryptNumber;
    }

    /**
     * 设置token加密秘钥
     * @param secret 秘钥
     */
    public void setTokenSecret(String secret) {
        this.TOKEN_SECRET = secret;
    }

    /**
     * 设置请求头中携带的token
     * @param headerTokenKeyName
     */
    public void setHeaderTokenKeyName(String headerTokenKeyName) {
        this.HEADER_TOKEN_KEY_NAME = headerTokenKeyName;
    }

    /**
     * 设置自定义的realm
     * @param qmSecurityRealm
     */
    public void setQmSecurityRealm(QmSecurityRealm qmSecurityRealm){
        this.qmSecurityRealm = qmSecurityRealm;
    }

}
