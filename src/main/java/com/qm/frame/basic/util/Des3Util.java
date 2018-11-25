package com.qm.frame.basic.util;

import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

import com.qm.frame.basic.Constant.QmConstant;

/**
 * Copyright © 2018浅梦工作室. All rights reserved.
 * @author: 浅梦
 * @date: 2018年11月24日 上午2:00:06
 * @Description: 3DES工具类
 */
public class Des3Util {
	
	/**
	 * @Title: encode
	 * @param plainText 普通文本
	 * @return
	 * @throws Exception
	 * @Description: 加密方法
	 */
	public static String encode(String plainText) throws Exception {
		Key deskey = null;
		QmConstant config = QmConstant.getQmConstantByContext();
		DESedeKeySpec spec = new DESedeKeySpec(config.getDes3Constant().getKey().getBytes());
		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
		deskey = keyfactory.generateSecret(spec);

		Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
		IvParameterSpec ips = new IvParameterSpec(config.getDes3Constant().getIv().getBytes());
		cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
		byte[] encryptData = cipher.doFinal(plainText.getBytes(config.getDes3Constant().getEncoding()));
		//18.6.23--QM解决后台加密前端不能解密问题,删除空格解决。
		return Base64.encode(encryptData).replace(" ", "");
	}

	/**
	 * @Title: decode
	 * @param encryptText 加密文本
	 * @return
	 * @throws Exception
	 * @Description: 解密方法
	 */
	public static String decode(String encryptText) throws Exception {
		Key deskey = null;
		QmConstant config = QmConstant.getQmConstantByContext();
		DESedeKeySpec spec = new DESedeKeySpec(config.getDes3Constant().getKey().getBytes());
		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
		deskey = keyfactory.generateSecret(spec);
		Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
		IvParameterSpec ips = new IvParameterSpec(config.getDes3Constant().getIv().getBytes());
		cipher.init(Cipher.DECRYPT_MODE, deskey, ips);

		byte[] decryptData = cipher.doFinal(Base64.decode(encryptText));

		return new String(decryptData, config.getDes3Constant().getEncoding());
	}
	
	
	/**
	 * @Title: get3DESKey
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @Description: 获取des密钥
	 */
    public static String get3DESKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("DESede");
        keyGenerator.init(168);
        Key key = keyGenerator.generateKey();
        String result = new String(Base64.encode(key.getEncoded()));
        return result;
    }
}
