package com.qm.frame.qmsecurity.config;

import com.qm.frame.qmsecurity.realm.QmSecurityRealm;
import com.qm.frame.qmsecurity.realm.QmSecurityRealmTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright © 2018浅梦工作室}. All rights reserved.
 *
 * @author 浅梦
 * @date 2019年5月6日23:03:15
 * @Description 提供给调用者配置使用
 */
public class QmSecurityContent {

    private static Map<String,Object> cacheMap;

    static {
        cacheMap = new HashMap<>(16);
        cacheMap.put("tokenSecret","tokenSecret");
        cacheMap.put("headerTokenKeyName","token");
        cacheMap.put("encryptNumber",2);
        List<Object> passUris = new ArrayList<>();
        cacheMap.put("passUris",passUris);
        cacheMap.put("realm",new QmSecurityRealmTemplate());
    }

    /**
     * 设置token加密秘钥
     * @param secret
     */
    public static void setTokenSecret(String secret){
        cacheMap.put("tokenSecret",secret);
    }

    /**
     * 获取token加密秘钥
     * @return
     */
    public static String getTokenSecret(){
        return (String) cacheMap.get("tokenSecret");
    }

    /**
     * 设置请求头和响应头中携带token的字段名
     * @param secret
     */
    public static void setHeaderTokenKeyName(String key){
        cacheMap.put("headerTokenKeyName",key);
    }

    /**
     * 获取请求头中携带token的字段名
     * @return
     */
    public static String getHeaderTokenKeyName(){
        return (String) cacheMap.get("headerTokenKeyName");
    }

    /**
     * 设置token加密次数，底层调用AES对称加密算法
     * @param secret
     */
    public static void setEncryptNumber(int num){
        cacheMap.put("encryptNumber",num);
    }

    /**
     * 获取token加密次数，底层调用AES对称加密算法
     * @return
     */
    public static int getEncryptNumber(){
        return Integer.parseInt(cacheMap.get("encryptNumber").toString());
    }

    /**
     * 设置排除URI校验集合
     * @param secret
     */
    public static void setPassUris(List<String> passUris){
        cacheMap.put("passUris",passUris);
    }

    /**
     * 获取排除URI校验集合
     * @return
     */
    public static List<String> getPassUris(){
        return (List<String>) cacheMap.get("passUris");
    }

    /**
     * 设置realm
     * @param realm
     */
    public static void setRealm(QmSecurityRealm realm){
        cacheMap.put("realm",new QmSecurityRealmTemplate());
    }

    /**
     * 设置realm
     * @param realm
     */
    public static QmSecurityRealm getRealm(){
        return (QmSecurityRealm) cacheMap.get("realm");
    }

}
