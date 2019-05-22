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
    private String user;
    /**
     * 签发token时间
     */
    private Date signTime;
    /**
     * token多久后失效 (秒) 0无限
     */
    private long tokenExpireTime = 60L * 30;


    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Date getSignTime() {
        return signTime;
    }

    public void setSignTime(Date signTime) {
        this.signTime = signTime;
    }

    public long getTokenExpireTime() {
        return tokenExpireTime;
    }

    public void setTokenExpireTime(long tokenExpireTime) {
        this.tokenExpireTime = tokenExpireTime;
    }
}
