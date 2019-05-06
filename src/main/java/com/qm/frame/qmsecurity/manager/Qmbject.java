package com.qm.frame.qmsecurity.manager;

import com.qm.frame.qmsecurity.entity.QmPermissions;
import com.qm.frame.qmsecurity.entity.QmSessionInfo;
import com.qm.frame.qmsecurity.entity.QmTokenInfo;
import com.qm.frame.qmsecurity.exception.QmSecurityLoginErrorException;

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
     *
     * @param qmTokenInfo 参与签名的信息对象
     * @param expireTime  有效时长(秒单位)
     * @return
     */
    String login(QmTokenInfo qmTokenInfo) throws QmSecurityLoginErrorException;

    /**
     * session模式登录
     *
     * @param qmSessionInfo
     * @param roleId
     * @param expireTime
     */
    void loginForSession(final QmSessionInfo qmSessionInfo, final int expireTime);

    /**
     * session模式获取用户对象
     *
     * @return
     */
    QmSessionInfo getSessionInfo();

    /**
     * session模式设置用户对象
     *
     * @param qmSessionInfo
     */
    void setSessionInfo(final QmSessionInfo qmSessionInfo);

    /**
     * 动态获取角色权限
     * isNew=false：
     * 则缓存中如果不存在的情况下，通过自定义的realm的authorizationPermissions方法获取最新信息。
     * 如果存在则直接获取该对象。
     * <p>
     * isNew=true：
     * 则无论如何都从自定义的realm的authorizationPermissions方法获取最新信息，并更新到缓存中。
     *
     * @param roleId 角色id
     * @param isNew  是否一定获取最新信息
     * @return QmPermissions 返回角色权限对象
     */
    QmPermissions extractQmPermissions(int roleId, boolean isNew);

    /**
     * 获取缓存中的所有角色权限信息
     *
     * @return
     */
    List<QmPermissions> getAllQmPermissions();


    /**
     * 获取通过校验的token信息
     *
     * @return
     */
    QmTokenInfo getTokenInfo();

}
