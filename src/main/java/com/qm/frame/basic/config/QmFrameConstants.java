package com.qm.frame.basic.config;

import com.qm.frame.basic.exception.QmFrameException;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Copyright © 2019浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2019/1/30 19:36
 * @Description 框架配置常量表
 */
public class QmFrameConstants {

    private static final Properties PRO = getProperties();
    /**
     * 是否启用AES对称加密传输
     */
    public static final boolean AES_START =
            getProBoolean("aes.start", false);
    /**
     * AES秘钥
     */
    public static final String AES_KEY =
            getProString("aes.key", "20190101000000QmBootFrame");
    /**
     * 统一使用的编码方式
     */
    public static final String AES_ENCODING =
            getProString("aes.encoding", "UTF-8");
    /**
     * 加密次数
     */
    public static final int AES_NUMBER =
            getProInt("aes.number", 1);
    /**
     * 请求数据时，根据该key名解析数据(rest风格)
     */
    public static final String REQUEST_DATA_KEY =
            getProString("body.request.key", "value");
    /**
     * 返回数据时，使用的最外层key名(rest风格)
     */
    public static final String RESPONSE_DATA_KEY =
            getProString("body.response.key", "value");

    public static final String RESPONSE_MESSAGE_LANG =
            getProString("body.response.message.lang", "en");
    /**
     * 特殊请求不进行解析(包括版本控制和解析json等)
     */
    public static final List<String> REQUEST_SPECIAL_URI = getRequestSpecialUri();
    /**
     * 是否开启版本控制(ture时,每个请求需在header带上version参数,参数值version)
     */
    public static final boolean VERSION_START =
            getProBoolean("version.start", false);
    /**
     * 系统目前版本编号
     */
    public static final String VERSION_NOW =
            getProString("version.now", "0.0.1");

    /**
     * 系统容忍请求版本编号(默认允许当前版本)
     */
    public static final List<String> VERSION_ALLOWS = getVersionAllows();
    /**
     * 记录日志类路径
     */
    public static final String LOGGER_AOP_EXTEND_CLASS =
            getProString("controller.aop.extend.class", null);

    /**
     * 获取特殊请求
     *
     * @return
     */
    private static final List<String> getRequestSpecialUri() {
        List<String> specialUriList = new ArrayList<>();
        boolean is = true;
        int num = 0;
        while (is) {
            String tempVersion = PRO.getProperty("request.special.uri-[" + num + "]", null);
            if (tempVersion != null) {
                specialUriList.add(tempVersion);
            } else {
                is = false;
            }
            num++;
        }
        return specialUriList;
    }


    public static final boolean getProBoolean(String key) {
        return Boolean.parseBoolean(PRO.getProperty(key, "false"));
    }

    public static final boolean getProBoolean(String key, boolean defaultVal) {
        return Boolean.parseBoolean(PRO.getProperty(key, String.valueOf(defaultVal)));
    }

    public static final int getProInt(String key) {
        return Integer.parseInt(PRO.getProperty(key, "0"));
    }

    public static final int getProInt(String key, int defaultVal) {
        return Integer.parseInt(PRO.getProperty(key, String.valueOf(defaultVal)));
    }

    public static final String getProString(String key) {
        return PRO.getProperty(key);
    }

    public static final String getProString(String key, String defaultVal) {
        return PRO.getProperty(key, defaultVal);
    }

    /**
     * 获取允许版本号
     *
     * @return
     */
    private static final List<String> getVersionAllows() {
        List<String> allows = new ArrayList<>();
        boolean is = true;
        int num = 0;
        while (is) {
            String tempVersion = PRO.getProperty("version.allows-[" + num + "]", null);
            if (tempVersion != null) {
                allows.add(tempVersion);
            } else {
                is = false;
            }
            num++;
        }
        return allows;
    }

    private static final Properties getProperties() {
        try {
            Properties properties = new Properties();
            // 读取properties文件,使用InputStreamReader字符流防止文件中出现中文导致乱码
            InputStreamReader inStream = new InputStreamReader
                    (QmFrameConstants.class.getClassLoader().getResourceAsStream("qm-frame.properties"),
                            "UTF-8");
            properties.load(inStream);
            inStream.close();
            return properties;
        } catch (Exception e) {
            throw new QmFrameException("读取qm-frame.properties发生了异常！", e);
        }
    }
}
