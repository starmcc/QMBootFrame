package com.qm.frame.qmsecurity.manager;

import com.qm.frame.qmsecurity.entity.QmPermissions;
import com.qm.frame.qmsecurity.entity.QmSessionInfo;
import com.qm.frame.qmsecurity.entity.QmTokenInfo;
import com.qm.frame.qmsecurity.exception.QmSecuritySignTokenException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
     * token登录机制
     * 1、登录成功后将用户的信息生成的Token作为k存储到缓存里面。
     * 2、当该用户在次请求时，通过安全框架层层校验。
     * 3、当该用户这次请求token值还在生命周期内，则会通过重新PUT的方式储存到缓存中，该键值对生命周期时间重新计算
     * 4、当该用户这次请求的token值已经过期，但该token对应的缓存信息存在，则表示该用户一直在操作只是token失效了，程序会重新生成token并将新的token放置到缓存中。
     * 5、当该用户这次请求的token值已经过期，并在缓存中不存在对应的信息，则直接拦截，判断为登录失效。
     * 6、当用户请求接口时遇到第四种情况时，会给Response的Header中设置token字段，该token映射的v为新的有效token。
     * 7、注：当前端检测到Response的Header中有token字段时，替换旧的token。
     * @param qmTokenInfo 参与签名的信息对象
     * @return
     */
    String login(QmTokenInfo qmTokenInfo) throws QmSecuritySignTokenException;

    /**
     * session模式登录
     *
     * @param qmSessionInfo
     */
    void loginForSession(QmSessionInfo qmSessionInfo) throws QmSecuritySignTokenException;

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
    void setSessionInfo(QmSessionInfo qmSessionInfo);

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

    /**
     * 校验token是否合法
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param isPerssions 是否校验用户权限
     * @return
     */
    boolean verifyToken(HttpServletRequest request,
                        HttpServletResponse response,
                        boolean isPerssions);

}
