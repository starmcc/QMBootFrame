package com.qm.frame.qmsecurity.config;

/**
 * Copyright © 2019浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2019/5/20 22:00
 * @Description QmSecurity常量储存类
 */
public class QmSecurityConstants {

    /**
     * 框架缓存用户前缀key
     */
    public static final String USER_KEY = "Qmbject_";
    /**
     * 框架缓存权限前缀key
     */
    public static final String MATCHING_KEY = "matchingUrls_";
    /**
     * 底层加解密key
     */
    public static final String BASIC_KEY = "hfsdnvfjdmfkl";
    /**
     * token无限期伪装字符串
     */
    public static final String TOKEN_EXP_INFINITE = "shdhwaxnlxhueicn";
    /**
     * 权限缓存无限时间伪装字符串
     */
    public static final String MATCHURI_EXP_INFINITE = "asdhiuesnd31270ccb";
    /**
     * 重新签发token缓冲储存key
     */
    public static final String RESTART_SIGN_KEY = "TokenContainer_";
    /**
     * 重新签发token缓冲时间(秒)
     */
    public static final long RESTART_SIGN_TIME = 60;
    /**
     * 序列化编码格式 此处只能是ISO-8859-1,但是不会影响中文使用
     */
    public static final String SERIALIZE_CHARSET = "ISO-8859-1";
    /**
     * 底层加解密编码格式
     */
    public static final String ENC_DEC_ENCODING = "UTF-8";
    /**
     * 加解密类型
     */
    public static final String ENC_DEC_TYPE = "AES";
    /**
     * 签名字符集
     */
    public static final char[] SIGN_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'U', 'C', 'K', 'Z', 'H', 'G'};
    /**
     * 签名类型
     */
    public static final String SIGN_TYPE = "MD5";

}
