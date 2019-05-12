package com.qm.code.service;

import java.util.List;

/**
 * Copyright © 2018浅梦工作室}. All rights reserved.
 *
 * @author 浅梦
 * @date 2018/12/9 19:47
 * @Description 权限业务接口
 */
public interface RoleService {

    /**
     * 获取该角色id的授权集合
     * @param roleId
     * @return
     */
    List<String> getAuthListByRoleId(int roleId);
}
