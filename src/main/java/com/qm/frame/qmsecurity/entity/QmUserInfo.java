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

    /**
     * 身份校验唯一标识
     */
    private String identify;
    /**
     * 用户对象
     */
    private Object user;
    /**
     * 签发token时间
     */
    private Date signTime;
    /**
     * 登录缓存多久后销毁 (秒)
     */
    private long loginExpireTime;
    /**
     * token多久后失效 (秒) 0无限
     */
    private long tokenExpireTime;
    /**
     * 签发token保存
     */
    private String token;

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
        return new Date(this.signTime.getTime());
    }

    public void setSignTime(Date signTime) {
        this.signTime = new Date(signTime.getTime());
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
