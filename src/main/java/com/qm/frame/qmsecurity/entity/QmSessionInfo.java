package com.qm.frame.qmsecurity.entity;

/**
 * Copyright © 2019浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2019/2/11 18:13
 * @Description Session用户实体
 */
public class QmSessionInfo {

    private Object user;
    private Integer roleId;

    public Object getUser() {
        return user;
    }

    public void setUser(Object user) {
        this.user = user;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
