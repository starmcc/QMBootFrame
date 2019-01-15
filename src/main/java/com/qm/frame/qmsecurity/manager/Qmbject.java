package com.qm.frame.qmsecurity.manager;

import com.qm.frame.qmsecurity.entity.QmPermissions;
import com.qm.frame.qmsecurity.entity.QmTokenInfo;

import java.util.List;

/**
 * Copyright © 2018浅梦工作室}. All rights reserved.
 *
 * @author 浅梦
 * @date 2018/12/22 18:08
 * @Description QM安全管理器，主体控制类
 */
public interface Qmbject {



    /**
     * 登录
     * @param qmTokenInfo 参与签名的信息对象
     * @param expireTime  有效时长(秒单位)
     * @return
     */
    String login(final QmTokenInfo qmTokenInfo, final long expireTime);


    /**
     * 动态获取角色权限
     * isNew=false：
     * 则缓存中如果不存在的情况下，通过自定义的realm的authorizationPermissions方法获取最新信息。
     * 如果存在则直接获取该对象。
     *
     * isNew=true：
     * 则无论如何都从自定义的realm的authorizationPermissions方法获取最新信息，并更新到缓存中。
     * @param roleId 角色id
     * @param isNew 是否一定获取最新信息
     * @return QmPermissions 返回角色权限对象
     */
    QmPermissions extractQmPermissions(final int roleId, final boolean isNew);


    /**
     * 获取缓存中的所有角色权限信息
     * @return
     */
    List<QmPermissions> getAllQmPermissions();


    /**
     * 获取通过校验的token信息
     * @return
     */
    QmTokenInfo getTokenInfo();

}
