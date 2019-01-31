package com.qm.frame.basic.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * Copyright © 2018浅梦工作室. All rights reserved.
 * @author 浅梦
 * @date 2018年11月24日 上午2:09:11
 * @Description properties工具类，默认读取config.properties配置文件
 */
public class PropertiesUtil {

	private static Properties properties;
	
	/**
	 * 如果需要读取其他配置文件请指定fileName的值;
	 */
	public static String fileName = null;
	
	private PropertiesUtil() {}
	
	static {
		properties = new Properties();
		InputStreamReader inStream = null;
		try {
			// 读取properties文件,使用InputStreamReader字符流防止文件中出现中文导致乱码
			inStream = new InputStreamReader
					(PropertiesUtil.class.getClassLoader().getResourceAsStream("config.properties"),
							"UTF-8");
			properties.load(inStream);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				inStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * @Title getQmResponseOut
	 * @param key
	 * @return
	 * @Description 从properties文件中读取出一个key对应的value
	 */
	public static String get(String key) {
		String value = properties.getProperty(key);
		return value;
	}
	
	/**
	 * @Title getQmResponseOut
	 * @param key
	 * @param defaultValue
	 * @return
	 * @Description 从properties文件中读取出一个key对应的value,如果该value为空返回默认文本
	 */
    public static String get(String key,String defaultValue){
    	String value= properties.getProperty(key,defaultValue);
    	if(value == null || value.trim().equals("")) {
    		value = defaultValue;
    	}
    	return value;    
	}
    
    /**
     * @Title getValueByFileName
     * @param key
     * @param fileName
     * @return
     * @Description 根据文件名读取
     */
    public static String getValueByFileName(String key,String fileName) {
    	Properties pro = new Properties();
		InputStreamReader inStream = null;
		String value = null;
		try {
			// 读取properties文件,使用InputStreamReader字符流防止文件中出现中文导致乱码
			inStream = new InputStreamReader
					(PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName),
							"UTF-8");
			pro.load(inStream);
			value = pro.getProperty(key);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				inStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return value;
    }

}