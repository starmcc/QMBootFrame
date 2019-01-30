package com.qm.code.sercurity;

import com.qm.code.entity.Permissions;
import com.qm.code.service.RoleService;
import com.qm.code.service.UserService;
import com.qm.frame.qmsecurity.basic.QmSecurityRealm;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright © 2018浅梦工作室}. All rights reserved.
 *
 * @author 浅梦
 * @date 2018/12/23 2:20
 * @Description 自定义realm
 */
public class MyRealm implements QmSecurityRealm {

    @Autowired
    private RoleService roleService;


    @Override
    public List<String> authorizationPermissions(int roleId) {
        // 获取该角色的权限集合
        List<Permissions> permissionsList = roleService.getPermissions(roleId);
        // 把权限拆箱出来，返回给框架
        List<String> matchUrls = new ArrayList<>();
        for (Permissions permission : permissionsList) {
            matchUrls.add(permission.getMatchUrl());
        }
        return matchUrls;
    }
}
