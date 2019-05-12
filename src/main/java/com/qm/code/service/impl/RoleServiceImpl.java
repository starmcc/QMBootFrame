package com.qm.code.service.impl;

import com.qm.code.service.RoleService;
import com.qm.frame.mybatis.base.QmBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright © 2018浅梦工作室}. All rights reserved.
 *
 * @author 浅梦
 * @date 2018/12/9 20:19
 * @Description 角色权限实现类
 */
@Service
public class RoleServiceImpl implements RoleService {

    /**
     * mapper的命名空间，请按照约定格式书写，相对应的是sql包中的Mapper的namescapse
     */
    private static final String PAGE_NAME = "UserManager-RoleServiceImpl-0-Mapper";
    @Autowired
    private QmBase qmBase;

    @Override
    public List<String> getAuthListByRoleId(int roleId) {
        Map<String, Object> params = new HashMap<>();
        params.put("roleId", roleId);
        return qmBase.selectList(PAGE_NAME + "getAuthListByRoleId", params);
    }
}
