package com.qm.frame.qmsecurity.qmbject;

import com.qm.frame.qmsecurity.entity.QmUserInfo;
import com.qm.frame.qmsecurity.exception.QmSecurityCacheException;
import com.qm.frame.qmsecurity.exception.QmSecurityCreateTokenException;
import com.qm.frame.qmsecurity.exception.QmSecurityQmUserInfoException;

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
     * 1、登录成功后将用户的唯一id作为k存储到缓存里面并把用户对象缓存到v中。
     * 2、当该用户在次请求时，通过安全框架层层校验。
     * 3、当该用户这次请求token对应用户信息还在缓存内，则会通过重新PUT的方式储存到缓存中，刷新失效时间。
     * 4、当该用户这次请求的token值已经过期，但该token对应的缓存信息存在，则表示该用户一直在操作只是token失效了，程序会重新签发token，更新用户信息。
     * 5、当该用户这次请求的token值已经过期，并在缓存中对应的用户信息失效，则直接拦截，判断为登录失效。
     * 6、当用户请求接口时遇到第四种情况时，会给Response的Header中设置token字段，该token映射的v为新的有效token。
     * 7、注：当前端检测到Response的Header中有token字段时需替换旧的token内容，该次签发只会存在于一次返回。
     *
     * @param qmUserInfo 参与签名的信息对象
     * @return
     */
    String login(QmUserInfo qmUserInfo)
            throws QmSecurityQmUserInfoException, QmSecurityCreateTokenException, QmSecurityCacheException;

    /**
     * 获取通过校验的用户对象
     *
     * @return
     */
    QmUserInfo getUserInfo();

    /**
     * 设置用户对象
     *
     * @param qmUserInfo
     */
    void setUserInfo(QmUserInfo qmUserInfo);

    /**
     * 动态获取角色权限
     * isNew=false：
     * 则缓存中如果不存在的情况下，通过自定义的realm的authorizationPermissions方法获取最新信息。
     * 如果存在则直接获取该对象。
     * <p>
     * isNew=true：
     * 则无论如何都从自定义的realm的authorizationPermissions方法获取最新信息，并更新到缓存中。
     *
     * @param isNew 是否一定获取最新信息
     * @return List<String> 返回角色权限
     */
    List<String> extractMatchingUrls(boolean isNew);

    /**
     * 注销用户
     * @param identify
     * @return
     */
    boolean logout(String identify);

}
