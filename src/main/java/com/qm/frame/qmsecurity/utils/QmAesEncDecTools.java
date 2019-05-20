package com.qm.frame.qmsecurity.utils;

import com.qm.frame.qmsecurity.config.QmSecurityConstants;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

/**
 * Copyright © 2019浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2019/5/18 15:06
 * @Description Aes底层数据安全工具
 */
public class QmAesEncDecTools {

    /**
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String encrypt(String data, String key) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance(QmSecurityConstants.ENC_DEC_TYPE);
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(key.getBytes(QmSecurityConstants.ENC_DEC_ENCODING));
        kgen.init(128, secureRandom);
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, QmSecurityConstants.ENC_DEC_TYPE);
        Cipher cipher = Cipher.getInstance(QmSecurityConstants.ENC_DEC_TYPE);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encryptedData = cipher.doFinal(data.getBytes(QmSecurityConstants.ENC_DEC_ENCODING));
        return Base64.encodeBase64String(encryptedData);
    }


    /**
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String decrypt(String data, String key) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance(QmSecurityConstants.ENC_DEC_TYPE);
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(key.getBytes(QmSecurityConstants.ENC_DEC_ENCODING));
        kgen.init(128, secureRandom);
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, QmSecurityConstants.ENC_DEC_TYPE);
        Cipher cipher = Cipher.getInstance(QmSecurityConstants.ENC_DEC_TYPE);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decryptedData = cipher.doFinal(Base64.decodeBase64(data));
        return new String(decryptedData, QmSecurityConstants.ENC_DEC_ENCODING);
    }
}
