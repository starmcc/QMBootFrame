package com.qm.frame.qmsecurity.entity;

import java.util.List;

/**
 * Copyright © 2018浅梦工作室}. All rights reserved.
 *
 * @author 浅梦
 * @date 2018/12/22 17:15
 * @Description 角色权限实体类
 */
public class QmPermissions {

    private int roleId;
    private List<String> matchUrls;

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public List<String> getMatchUrls() {
        return matchUrls;
    }

    public void setMatchUrls(List<String> matchUrls) {
        this.matchUrls = matchUrls;
    }
}
