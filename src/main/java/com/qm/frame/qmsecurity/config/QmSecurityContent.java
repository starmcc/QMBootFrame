package com.qm.frame.qmsecurity.config;

import com.qm.frame.qmsecurity.manager.*;

/**
 * Copyright © 2018浅梦工作室}. All rights reserved.
 *
 * @author 浅梦
 * @date 2019年5月6日23:03:15
 * @Description 提供给调用者配置使用
 */
public class QmSecurityContent {

    /**
     * 校验机制
     */
    public static String sessionOrToken = "token";

    /**
     * 设置token加密秘钥
     */
    public static String tokenSecret = "tokenSecret";

    /**
     * 设置请求头中携带的token
     */
    public static String headerTokenKeyName = "token";

    /**
     * 设置token加密次数，底层调用AES对称加密算法
     */
    public static int encryptNumber = 1;

    /**
     * 设置自定义的realm
     */
    public static QmSecurityRealm realm = new QmSecurityRealmTemplate();
    /**
     * 设置session监听器的回调
     */
    public static QmSecuritySessionEvent sessionEvent = new QmSecuritySessionEventTemplate();

    /**
     * 设置Redis实现类
     */
    public static QmSecurityRedisClient redisclient = new QmSecurityRedisClientTemplate();
}
