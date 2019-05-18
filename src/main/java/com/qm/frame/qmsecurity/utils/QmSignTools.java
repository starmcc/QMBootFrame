package com.qm.frame.qmsecurity.utils;

import java.security.MessageDigest;

/**
 * Copyright © 2019浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2019/5/18 15:19
 * @Description 签名工具
 */
public class QmSignTools {

    /**
     * 底层加解密编码格式
     */
    private static final String ENCODING = "UTF-8";
    private static final char[] HEX_DIGITS =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'U', 'C', 'K', 'Z', 'H', 'G'};
    private static final String SIGN_TYPE = "MD5";


    /**
     * 签名
     *
     * @param str
     * @return
     */
    protected static String signMD5(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        try {
            MessageDigest mdTemp = MessageDigest.getInstance(SIGN_TYPE);
            mdTemp.update(str.getBytes(ENCODING));
            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] buf = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = HEX_DIGITS[byte0 >>> 4 & 0xf];
                buf[k++] = HEX_DIGITS[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
