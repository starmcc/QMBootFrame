package com.qm.code.service.impl;

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
 * @Description: 角色权限实现类
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private QmBase qmBase;

    private static final String NAME_SPE = "Role-RoleServiceImpl-0-Mapper";

    @Override
    public Role getRole(int roleId) {
        Role role = new Role();
        role.setRoleId(roleId);
        List<Role> roleList = qmBase.autoSelectList(role,Role.class);
        if (roleList == null || roleList.size() == 0) {
            return null;
        }
        return roleList.get(0);
    }

    @Override
    public List<String> getPowers(int roleId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("roleId",roleId);
        List<String> powers = qmBase.selectList(NAME_SPE + "getPowers",params);
        return powers;
    }
}
