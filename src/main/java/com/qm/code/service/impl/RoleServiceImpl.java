package com.qm.code.service.impl;

import com.qm.code.entity.Permissions;
import com.qm.code.entity.Role;
import com.qm.code.service.RoleService;
import com.qm.frame.mybatis.base.QmBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * Copyright © 2018浅梦工作室}. All rights reserved.
 *
 * @author 浅梦
 * @date 2018/12/9 20:19
 * @Description 角色权限实现类
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private QmBase qmBase;

    @Override
    public List<Permissions> getPermissions(int roleId) {
        Permissions permissions = new Permissions();
        permissions.setRoleId(roleId);
        return qmBase.autoSelectList(permissions,Permissions.class);
    }
}
