package com.qm.code.sercurity;


import com.qm.code.entity.Role;
import com.qm.code.service.RoleService;
import com.qm.frame.qmsecurity.manager.QmSecurityManager;
import com.qm.frame.qmsecurity.entity.QmSecInfo;
import com.qm.frame.qmsecurity.entity.QmSecRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


/**
 * Copyright © 2018浅梦工作室. All rights reserved.
 * @author: 浅梦
 * @date: 2018年11月26日 上午1:31:48
 * @Description: 实现权限框架的QmSecurityManager
 * 使用权限框架必须继承该抽象类。
 * 该类提供的两个方法在权限框架鉴权过程会直接调用。
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
        // 此处因为是示例，所以很只关注结果，并不关注过程。
        // 实际开发中应将这复杂的查询过程运用SQL语句实现。可有效提高接口访问速度。
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
