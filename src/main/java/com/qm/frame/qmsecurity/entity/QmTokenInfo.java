package com.qm.frame.qmsecurity.entity;

import java.util.Map;

/**
 * Copyright © 2018浅梦工作室}. All rights reserved.
 *
 * @author 浅梦
 * @date 2018/12/22 19:58
 * @Description 用于token签名的实体类
 */
public class QmTokenInfo {

    private String identify; // 身份校验唯一标识
    private int roleId; // 角色id
    private String requestIp; // 请求ip地址
    private Map<String, String> infoMap; // 用户基本信息
    private long expireTime = 60 * 60; // 有效期 默认1小时
    private long tokenActiveTime = 60 * 30; // 默认半小时活跃期

    public long getTokenActiveTime() {
        return tokenActiveTime;
    }

    public void setTokenActiveTime(long tokenActiveTime) {
        this.tokenActiveTime = tokenActiveTime;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public Map<String, String> getInfoMap() {
        return infoMap;
    }

    public void setInfoMap(Map<String, String> infoMap) {
        this.infoMap = infoMap;
    }

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRequestIp() {
        return requestIp;
    }

    public void setRequestIp(String requestIp) {
        this.requestIp = requestIp;
    }
}
