package com.qm.code.entity;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Copyright © 2018浅梦工作室. All rights reserved.
 *
 * @author: 浅梦
 * @date: 2018年11月26日 上午1:36:32
 * @Description: 测试user实体类
 */
@Table(name = "qm_user")
public class User {
    @Id
    private Integer id;
    private String userName;
    private String password;
    private Integer roleId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", userName=" + userName + ", password=" + password + ", roleId=" + roleId + "]";
    }

}
