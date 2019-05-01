package com.qm.frame.basic.util;

import com.qm.frame.basic.config.QmFrameContent;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Date;

/**
 * AES对称加密技术
 */
public class AESUtil {
    private static final Logger LOG = LoggerFactory.getLogger(AESUtil.class);

    /**
     * 加密封装
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String encryptAES(String data) throws Exception {
        Date date = new Date();
        String str = data;
        for (int i = 0; i < QmFrameContent.AES_NUMBER; i++) {
            str = encryptAES(str, QmFrameContent.AES_KEY);
        }
        LOG.debug("加密用时：" + (new Date().getTime() - date.getTime()));
        return str;
    }

    /**
     * 解密封装
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String decryptAES(String data) throws Exception {
        Date date = new Date();
        String str = data;
        for (int i = 0; i < QmFrameContent.AES_NUMBER; i++) {
            str = decryptAES(str, QmFrameContent.AES_KEY);
        }
        LOG.debug("解密用时：" + (new Date().getTime() - date.getTime()));
        return str;
    }

    /**
     * 加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    private static String encryptAES(String data, String key) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(key.getBytes());
        kgen.init(128, secureRandom);
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encryptedData = cipher.doFinal(data.getBytes(QmFrameContent.AES_ENCODING));
        String hexStr = Base64.encodeBase64String(encryptedData);
        return hexStr;
    }


    /**
     * 解密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    private static String decryptAES(String data, String key) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        //kgen.init(128, new SecureRandom(key.getBytes()));
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(key.getBytes());
        kgen.init(128, secureRandom);
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decryptedData = cipher.doFinal(Base64.decodeBase64(data));
        String respStr = new String(decryptedData, QmFrameContent.AES_ENCODING);
        return respStr;
    }


}
