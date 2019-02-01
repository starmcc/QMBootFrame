package com.qm.frame.basic.config;

import com.qm.frame.basic.exception.QmFrameException;
import com.qm.frame.basic.util.PropertiesUtil;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Copyright © 2019浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2019/1/30 19:36
 * @Description
 */
public class QmFrameConcent {

    private final static Properties PRO = getProperties();
    /**
     * 白名单
     */
    public final static String DRUID_ALLOW = PRO.getProperty("druid.allow","127.0.0.1");
    /**
     * 黑名单
     */
    public final static String DRUID_DENY = PRO.getProperty("druid.deny","");
    /**
     * 账号
     */
    public final static String DRUID_LOGIN_USERNAME = PRO.getProperty("druid.login.username","admin");
    /**
     * 密码
     */
    public final static String DRUID_LOGIN_PASSWORD = PRO.getProperty("druid.login.password","admin");
    /**
     * 是否可重置数据
     */
    public final static String DRUID_RESET_ENABLE = PRO.getProperty("druid.reset.enable","false");
    /**
     * 是否启用AES对称加密传输
     */
    public final static boolean AES_START = Boolean.parseBoolean(PRO.getProperty("aes.start","false"));
    /**
     * AES秘钥
     */
    public final static String AES_KEY = PRO.getProperty("aes.key","20190101000000QmBootFrame");
    /**
     * 统一使用的编码方式
     */
    public final static String AES_ENCODING = PRO.getProperty("aes.encoding","UTF-8");
    /**
     * 加密次数
     */
    public final static int AES_NUMBER = Integer.parseInt(PRO.getProperty("aes.number","1"));
    /**
     * 请求数据时，根据该key名解析数据(rest风格)
     */
    public final static String REQUEST_DATA_KEY = PRO.getProperty("body.request.key","value");
    /**
     * 返回数据时，使用的最外层key名(rest风格)
     */
    public final static String RESPONSE_DATA_KEY = PRO.getProperty("body.response.key","value");
    /**
     * 是否开启版本控制(ture时,每个请求需在header带上version参数,参数值version)
     */
    public final static Boolean VERSION_START = Boolean.parseBoolean(PRO.getProperty("version.start","false"));
    /**
     * 系统目前版本编号
     */
    public final static String VERSION_NOW = PRO.getProperty("version.now","0.0.1");

    /**
     * 系统容忍请求版本编号(默认允许当前版本)
     */
    public final static List<String> VERSION_ALLOWS = getVersionAllows();
    /**
     * 记录日志类路径
     */
    public final static String LOGGER_AOP_EXTEND_CLASS = PRO.getProperty("controller.aop.extend.class",null);

    /**
     * 获取允许版本号
     * @return
     */
    private final static List<String> getVersionAllows(){
        List<String> allows = new ArrayList<>();
        boolean is = true;
        int num = 0;
        while (is){
            String tempVersion = PRO.getProperty("version.allows-[" + num + "]",null);
            if (tempVersion != null) {
                allows.add(tempVersion);
            }else{
                is = false;
            }
            num++;
        }
        return allows;
    }

    private final static Properties getProperties(){
        try {
            Properties properties = new Properties();
            // 读取properties文件,使用InputStreamReader字符流防止文件中出现中文导致乱码
            InputStreamReader inStream = new InputStreamReader
                    (QmFrameConcent.class.getClassLoader().getResourceAsStream("qm-frame.properties"),
                            "UTF-8");
            properties.load(inStream);
            inStream.close();
            return properties;
        } catch (Exception e) {
            throw new QmFrameException("读取qm-frame.properties发生了异常！",e);
        }
    }


}
