package com.qm.code.entity;

import com.qm.frame.mybatis.note.QmId;
import com.qm.frame.mybatis.note.QmStyle;
import com.qm.frame.mybatis.note.QmTable;

import java.util.Date;

/**
 * Copyright © 2018浅梦工作室}. All rights reserved.
 *
 * @author 浅梦
 * @date 2018/12/9 20:14
 * @Description 角色实体
 */
@QmTable(name = "qm_role")
public class Role {
    @QmId
    private Integer roleId;
    private String roleName;
    private Date createTime;
    private Date updateTime;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
