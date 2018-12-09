package com.qm.code.sercurity;


import com.qm.code.entity.Role;
import com.qm.code.service.RoleService;
import com.qm.frame.qmsecurity.connector.QmSecurityManager;
import com.qm.frame.qmsecurity.entity.QmSecInfo;
import com.qm.frame.qmsecurity.entity.QmSecRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * Copyright © 2018浅梦工作室. All rights reserved.
 * @author: 浅梦
 * @date: 2018年11月26日 上午1:31:48
 * @Description: 实现权限框架的QmSecurityService
 * 使用权限框架必须实现该类，否则启动报错。
 * 该类提供的两个接口在系统启动时调用。实现缓存鉴权的过程。
 */
@Component
public class SecurityOnline extends QmSecurityManager {

    @Autowired
    private RoleService roleService;

    @Override
    public boolean verifyInfo(QmSecInfo qmSecInfo) {
        return true;
    }

    @Override
    public QmSecRole getQmSecRole(int roleId) {
        QmSecRole qmSecRole = new QmSecRole();
        Role role = roleService.getRole(roleId);
        if (role == null) {
            return null;
        }
        qmSecRole.setRoleId(role.getRoleId());
        qmSecRole.setRoleInfo(role);
        List<String> powers = roleService.getPowers(roleId);
        if (powers == null) {
            powers = new ArrayList<>();
        }
        qmSecRole.setRolePowers(powers);
        return qmSecRole;
    }
}
