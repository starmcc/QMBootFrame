package com.qm.frame.qmsecurity.qmbject;

import com.qm.frame.qmsecurity.entity.QmUserInfo;
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
     * 登录接口
     * QmSecurity安全机制
     * 1、登录成功后将用户的唯一id作为k存储到缓存里面并把用户对象缓存到v中。
     * 2、当该用户在次请求时，通过安全框架层层校验。
     * 3、当该用户这次请求解析token对应用户信息还在缓存内，则会通过重新PUT的方式储存到缓存中，延长失效时间。(动态活跃机制)
     * 4、当该用户这次请求解析token已经过期，但用户对象还在缓存中，进行重新签发token，并在response的headers返回新token值。
     * 5、当该用户这次请求的token值已经过期，并在缓存中对应的用户信息不存在，判断为登录失效。
     * 6、注：当前端检测到Response的Header中有token字段时需替换旧的token内容，该次签发只会存在于一次返回。
     *
     * @param qmUserInfo 参与签名的信息对象
     * @return
     * @throws QmSecurityQmUserInfoException
     * @throws QmSecurityCreateTokenException
     */
    String login(QmUserInfo qmUserInfo)
            throws QmSecurityQmUserInfoException, QmSecurityCreateTokenException;

    /**
     * 获取当前登录用户对象
     *
     * @return
     */
    QmUserInfo getUserInfo();

    /**
     * 根据唯一表示获取登录用户对象
     *
     * @param identify
     * @return
     */
    QmUserInfo getUserInfo(String identify);

    /**
     * 更新当前登录用户对象
     *
     * @param qmUserInfo
     */
    void setUserInfo(QmUserInfo qmUserInfo);

    /**
     * 动态获取当前用户角色权限
     * isNew=false：
     * 则缓存中如果不存在的情况下，通过自定义的realm的authorizationMatchingURI方法获取最新数据。
     * 如果存在则直接获取该对象。
     * isNew=true：
     * 则无论如何都从自定义的realm的authorizationMatchingURI中获取最新数据，并更新到缓存中。
     *
     * @param isNew 是否一定获取最新信息
     * @return List<String> 返回角色权限集合
     */
    List<String> extractMatchingURI(boolean isNew);


    /**
     * 动态获取角色权限
     * isNew=false：
     * 则缓存中如果不存在的情况下，通过自定义的realm的authorizationMatchingURI方法获取最新数据。
     * 如果存在则直接获取该对象。
     * isNew=true：
     * 则无论如何都从自定义的realm的authorizationMatchingURI中获取最新数据，并更新到缓存中。
     *
     * @param identify 用户唯一标识
     * @param isNew 是否一定获取最新信息
     * @return List<String> 返回角色权限集合
     */
    List<String> extractMatchingURI(String identify,boolean isNew);

    /**
     * 注销用户
     *
     * @param identify
     * @return
     */
    boolean logout(String identify);

}
