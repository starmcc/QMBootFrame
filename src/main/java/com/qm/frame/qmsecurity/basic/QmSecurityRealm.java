package com.qm.frame.qmsecurity.basic;

import java.util.List;

/**
 * Copyright © 2018浅梦工作室}. All rights reserved.
 *
 * @author 浅梦
 * @date 2018/12/22 17:06
 * @Description QmSecurity授权接口
 */
public interface QmSecurityRealm {

    /**
     * 提供给调用者的授权方法
     * 该方法只有当系统缓存中没有该角色才会进行调用
     * 每调用一次就会将相关信息保存到缓存中
     * @param token
     * @return
     */
    List<String> authorizationPermissions(int roleId);
}
