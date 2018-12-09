package com.qm.code.service;

import com.qm.code.entity.Role;

import java.util.List;

/**
 * Copyright © 2018浅梦工作室}. All rights reserved.
 *
 * @author 浅梦
 * @date 2018/12/9 19:47
 * @Description: 权限业务接口
 */
public interface RoleService {
    Role getRole(int roleId);

    List<String> getPowers(int roleId);


}
