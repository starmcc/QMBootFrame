package com.qm.frame.basic.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * Copyright © 2018浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2019年5月5日11:38:33
 * @Description 信息正则验证工具类
 */
public class QmVerifyUtil {

    /**
     * properties读取对象
     */
    private final static Properties PRO = getProperties();
    /**
     * 打印日志工具
     */
    private final static Logger LOG = LoggerFactory.getLogger(QmVerifyUtil.class);

    private static Properties getProperties() {
        Properties properties = new Properties();
        InputStreamReader inStream = null;
        try {
            // 读取properties文件,使用InputStreamReader字符流防止文件中出现中文导致乱码
            inStream = new InputStreamReader
                    (PropertiesUtil.class.getClassLoader().getResourceAsStream("verify.properties"),
                            "UTF-8");
            properties.load(inStream);
            return properties;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 校验内容是否匹配正则表达式
     * 使用该方法时，请确保resource目录下存在verify.properties文件
     * 根据verify.properties文件的节点检索正则表达式。
     *
     * @param node  properties的节点名称
     * @param value 需要校验的值
     * @return 返回布尔类型
     */
    public static boolean isRegex(String node, String value) {
        if (StringUtils.isEmpty(value)) {
            return false;
        }
        try {
            String regex = PRO.getProperty(node);
            if (regex == null) {
                LOG.error(String.format("%s节点读取失败！请检查properties的节点是否一致。", node));
                return false;
            }
            return Pattern.matches(regex, value);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("请检查正则表达式的格式是否有误！");
            return false;
        }
    }

    /**
     * 获取对应节点的错误信息
     * @param node
     * @return
     */
    public static String getErrorMsg(String node){
        try {
            String msg = PRO.getProperty(node + ".errorMsg");
            if (msg == null) {
                return node + "校验失败!";
            }
            return msg;
        } catch (Exception e) {
            return node + "校验失败!";
        }
    }

}