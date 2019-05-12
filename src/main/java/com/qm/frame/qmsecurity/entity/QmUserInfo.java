package com.qm.frame.qmsecurity.entity;

import java.util.Date;

/**
 * Copyright © 2018浅梦工作室}. All rights reserved.
 *
 * @author 浅梦
 * @date 2018/12/22 19:58
 * @Description 框架用户对象
 */
public class QmUserInfo {

    private String identify; // 身份校验唯一标识
    private Object user; // 用户对象
    private Date signTime; // 签发token时间
    private long loginExpireTime; // 登录缓存多久后销毁 (秒)
    private long tokenExpireTime; // token多久后失效 (秒) 0无限
    private String token; // 签发token保存

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }

    public Object getUser() {
        return user;
    }

    public void setUser(Object user) {
        this.user = user;
    }

    public long getLoginExpireTime() {
        return loginExpireTime;
    }

    public void setLoginExpireTime(long loginExpireTime) {
        this.loginExpireTime = loginExpireTime;
    }

    public long getTokenExpireTime() {
        return tokenExpireTime;
    }

    public void setTokenExpireTime(long tokenExpireTime) {
        this.tokenExpireTime = tokenExpireTime;
    }

    public Date getSignTime() {
        return signTime;
    }

    public void setSignTime(Date signTime) {
        this.signTime = signTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
