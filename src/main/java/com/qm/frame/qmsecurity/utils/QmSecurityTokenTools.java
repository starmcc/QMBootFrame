package com.qm.frame.qmsecurity.utils;

import com.alibaba.fastjson.JSON;
import com.qm.frame.basic.config.QmFrameContent;
import com.qm.frame.qmsecurity.config.QmSecurityContent;
import com.qm.frame.qmsecurity.entity.QmUserInfo;
import com.qm.frame.qmsecurity.exception.QmSecurityAnalysisTokenException;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Date;

/**
 * AES对称加密技术
 * @author 浅梦
 */
public class QmSecurityTokenTools {

    private static final Logger LOG = LoggerFactory.getLogger(QmSecurityTokenTools.class);

    private static final String BASIC_KEY = "hfsdnvfjdmfkl";

    private static final String ENCODING = "UTF-8";
    /**
     * 加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    private static String basicEncryptAES(String data, String key) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(key.getBytes(ENCODING));
        kgen.init(128, secureRandom);
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encryptedData = cipher.doFinal(data.getBytes(ENCODING));
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
    private static String basicDecryptAES(String data, String key) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(key.getBytes(ENCODING));
        kgen.init(128, secureRandom);
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decryptedData = cipher.doFinal(Base64.decodeBase64(data));
        String respStr = new String(decryptedData, ENCODING);
        return respStr;
    }

    /**
     * token签发方法
     *
     * @param qmUserInfo
     * @return
     * @throws Exception
     */
    public static String createToken(QmUserInfo qmUserInfo) throws Exception {
        Date date = new Date();
        StringBuffer stringBuffer = new StringBuffer();
        String identity = qmUserInfo.getIdentify();
        String userJson = JSON.toJSONString(qmUserInfo.getUser());
        String exp = "shdhwaxnlxhueicn";
        if (qmUserInfo.getTokenExpireTime() != 0) {
            exp = String.valueOf(qmUserInfo.getTokenExpireTime() * 1000);
        }
        qmUserInfo.setSignTime(new Date());
        String signTime = String.valueOf(qmUserInfo.getSignTime().getTime());
        stringBuffer.append(basicEncryptAES(identity, BASIC_KEY));
        stringBuffer.append("%@header@%");
        stringBuffer.append(basicEncryptAES(userJson, BASIC_KEY));
        stringBuffer.append("%@body@%");
        stringBuffer.append(basicEncryptAES(qmUserInfo.getUser().getClass().getTypeName(), BASIC_KEY));
        stringBuffer.append("%@className@%");
        stringBuffer.append(basicEncryptAES(exp, BASIC_KEY));
        stringBuffer.append("%@exp@%");
        stringBuffer.append(basicEncryptAES(signTime, BASIC_KEY));
        stringBuffer.append("%@signTime@%");
        String sign = signMD5(stringBuffer.toString());
        stringBuffer.append(sign);
        LOG.info("※※※Token签发耗时：" + (System.currentTimeMillis() - date.getTime()) + "/ms※※※");
        return encryptAES(stringBuffer.toString()).split("==")[0];
    }


    /**
     * token时效校验
     *
     * @param exp
     * @param signTime
     * @return
     */
    public static boolean verifyTokenExp(long exp, long signTime) {
        // 校验token是否失效
        if (exp != 0) {
            Date tokenExp = new Date(signTime + (exp * 1000));
            if (tokenExp.getTime() <= System.currentTimeMillis()) {
                // 失效了。
                return false;
            }
        }
        return true;
    }


    /**
     * 解析token
     *
     * @param token
     * @return
     * @throws Exception
     */
    public static QmUserInfo analysisToken(String token) throws QmSecurityAnalysisTokenException {
        try {
            // 解密
            token = decryptAES(token + "==");
            String tokenTemp = token;
            String[] temp = null;
            temp = tokenTemp.split("%@header@%");
            String identify = basicDecryptAES(temp[0], BASIC_KEY);
            tokenTemp = temp[1];
            temp = tokenTemp.split("%@body@%");
            String userJson = basicDecryptAES(temp[0], BASIC_KEY);
            tokenTemp = temp[1];
            temp = tokenTemp.split("%@className@%");
            String className = basicDecryptAES(temp[0], BASIC_KEY);
            tokenTemp = temp[1];
            temp = tokenTemp.split("%@exp@%");
            String exp = basicDecryptAES(temp[0], BASIC_KEY);
            tokenTemp = temp[1];
            temp = tokenTemp.split("%@signTime@%");
            String signTime = basicDecryptAES(temp[0], BASIC_KEY);
            // 组装对象
            QmUserInfo qmUserInfo = new QmUserInfo();
            qmUserInfo.setIdentify(identify);
            qmUserInfo.setUser(JSON.parseObject(userJson, Class.forName(className)));
            long expTemp = 0;
            String expInfinite = "shdhwaxnlxhueicn";
            if (!expInfinite.equals(exp)) {
                expTemp = Integer.parseInt(exp) / 1000;
            }
            qmUserInfo.setTokenExpireTime(expTemp);

            qmUserInfo.setSignTime(new Date(Long.parseLong(signTime)));
            String sign = token.split("%@signTime@%")[1];
            String tokenReadSign = token.split("%@signTime@%")[0] + "%@signTime@%";
            if (verifySign(sign, tokenReadSign) == false) {
                throw new QmSecurityAnalysisTokenException("token签名错误!");
            }
            return qmUserInfo;
        } catch (Exception e) {
            throw new QmSecurityAnalysisTokenException("token校验失败!", e);
        }
    }

    /**
     * 校验token签名
     *
     * @param sign
     * @param token
     * @param qmUserInfo
     * @return
     */
    private static boolean verifySign(String sign, String token) {
        String signTemp = signMD5(token);
        if (sign.trim().equals(signTemp)) {
            return true;
        }
        return false;
    }

    /**
     * 签名
     *
     * @param str
     * @return
     */
    private static String signMD5(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'U', 'C', 'K', 'Z', 'H', 'G'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(str.getBytes(ENCODING));
            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] buf = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 加密封装
     *
     * @param data
     * @return
     * @throws Exception
     */
    private static String encryptAES(String data) throws Exception {
        String str = data;
        for (int i = 0; i < QmSecurityContent.encryptNumber; i++) {
            str = basicEncryptAES(str, QmSecurityContent.tokenSecret);
        }
        return str;
    }

    /**
     * 解密封装
     *
     * @param data
     * @return
     * @throws Exception
     */
    private static String decryptAES(String data) throws Exception {
        String str = data;
        for (int i = 0; i < QmSecurityContent.encryptNumber; i++) {
            str = basicDecryptAES(str, QmSecurityContent.tokenSecret);
        }
        return str;
    }


}
