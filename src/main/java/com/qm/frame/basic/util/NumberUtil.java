package com.qm.frame.basic.util;

import java.util.Random;
import java.util.UUID;

/**
 * Copyright © 2018浅梦工作室. All rights reserved.
 * @author 浅梦
 * @date 2018年11月24日 上午2:06:55
 * @Description 这是随机生成工具类
 */
public class NumberUtil {

	/**
	 * @Title createUUID
	 * @param prefix 前缀，如果不需要请传递null
	 * @return
	 * @Description 生成UUID
	 */
	public static String createUUID(String prefix) {
		UUID uuid = UUID.randomUUID();
		String str = uuid.toString();
		// 去掉"-"符号
		String temp = str.substring(0, 8) + str.substring(9, 13) + str.substring(14, 18) + str.substring(19, 23)
				+ str.substring(24);
		if (prefix != null && !prefix.trim().equals("")) {
			temp = prefix + temp;
		}
		return temp;
	}
	
	/**
	 * @Title createRandom
	 * @param size
	 * @return
	 * @Description 获取随机数字符串
	 */
	public static String createRandom(int size) {
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i <= size; i++) {
			sb.append(String.valueOf(random.nextInt(9)));
		}
		return sb.toString();
	}
	
	
}
