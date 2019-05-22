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
     * 底层加解密key
     */
    public static final String BASIC_KEY = "hfsdnvfjdmfkl";
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
