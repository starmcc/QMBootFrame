package com.qm.frame.qmsecurity.basic;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.qm.frame.qmsecurity.config.QmSecurityContent;
import com.qm.frame.qmsecurity.entity.QmTokenInfo;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright © 2018浅梦工作室}. All rights reserved.
 *
 * @author 浅梦
 * @date 2018/12/23 1:14
 * @Description QmSecurity安全框架底层
 */
public class QmSecurityBasic {

    /**
     * 匹配角色授权url
     *
     * @param path
     * @param matchingUrls
     * @return
     */
    protected boolean verifyPermissions(String path, List<String> matchingUrls) {
        for (String matchUrl : matchingUrls) {
            if (verifyMatchURI(matchUrl, path)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 校验token
     *
     * @param token
     * @return
     */
    protected QmTokenInfo verifyToken(String token) {
        try {
            // AES解密token
            token = QmSecurityAESUtil.decryptAES(token);
            // 获取token信息,期间已经进行了一次校验机制。如果过期返回的是空。
            return getTokenInfo(token);
        } catch (Exception e) {
            new Exception("Token校验失败！").printStackTrace();
            return null;
        }
    }

    /**
     * 获取token信息
     *
     * @param token
     * @return
     * @throws Exception
     */
    protected QmTokenInfo getTokenInfo(String token) throws Exception {
        DecodedJWT jwt;
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(QmSecurityContent.TOKEN_SECRET)).build();
            // jwt解密token
            jwt = verifier.verify(token);
        } catch (Exception e) {
            throw new Exception("Token已过期！");
        }
        //这里是一个坑，jwt.getClaims()的不是HashMap,调用remove方法报错,直接给他新的HashMap;
        //下面是还原一个QmTokenInfo的内容
        Map<String, Claim> claimMap = new HashMap<>(jwt.getClaims());
        QmTokenInfo qmTokenInfo = new QmTokenInfo();
        String userName = claimMap.get("qm_security_userName").asString();
        claimMap.remove("qm_security_userName");
        String requestIp = claimMap.get("qm_security_requestIp").asString();
        claimMap.remove("qm_security_requestIp");
        Integer roleId = claimMap.get("qm_security_roleId").asInt();
        claimMap.remove("qm_security_roleId");
        long expireTime = claimMap.get("qm_security_expireTime").asLong();
        claimMap.remove("qm_security_expireTime");
        // 删除敏感的登录和失效时间
        claimMap.remove("exp");
        claimMap.remove("iat");
        qmTokenInfo.setIdentify(userName);
        qmTokenInfo.setRequestIp(requestIp);
        qmTokenInfo.setRoleId(roleId);
        qmTokenInfo.setExpireTime(expireTime);
        Map<String, String> infoMap = new HashMap<String, String>();
        for (String key : claimMap.keySet()) {
            infoMap.put(key, claimMap.get(key).asString());
        }
        qmTokenInfo.setInfoMap(infoMap);
        return qmTokenInfo;
    }


    /**
     * Spring提供的模糊路径匹配算法
     *
     * @param matchingUrl 匹配路径
     * @param requestUrl  请求地址
     * @return
     */
    protected boolean verifyMatchURI(String matchingUrl, String requestUrl) {
        PathMatcher matcher = new AntPathMatcher();
        return matcher.match(matchingUrl, requestUrl);
    }
}
