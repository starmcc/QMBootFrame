package com.qm.code.entity;

import com.qm.frame.mybatis.note.QmId;
import com.qm.frame.mybatis.note.QmTable;

/**
 * Copyright © 2018浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2018年11月26日 上午1:36:32
 * @Description 测试user实体类
 */
@QmTable(name = "qm_user")
public class User {
    // uuid = true 则启用框架中uuid策略自动生成uuid
    @QmId(uuid = true)
    private String userCode;
    private String userName;
    private String password;
    private Integer roleId;

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
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
        return "User [userCode=" + userCode + ", userName=" + userName + ", password=" + password + ", roleId=" + roleId + "]";
    }

}
