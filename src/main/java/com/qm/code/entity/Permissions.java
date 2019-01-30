package com.qm.code.entity;

import com.qm.frame.mybatis.note.QmId;
import com.qm.frame.mybatis.note.QmTable;

import java.util.Date;

/**
 * Copyright © 2019浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2019/1/30 23:36
 * @Description 权限表
 */
@QmTable(name = "qm_permissions")
public class Permissions {
    @QmId
    private String permissionId;
    private String matchUrl;
    private Integer roleId;

    public String getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }

    public String getMatchUrl() {
        return matchUrl;
    }

    public void setMatchUrl(String matchUrl) {
        this.matchUrl = matchUrl;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
