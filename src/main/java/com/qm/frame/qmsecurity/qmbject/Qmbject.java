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
     * 1、登录成功后生成token返回。
     * 2、当该用户在次请求时，通过安全框架层层校验。
     * 3、当该用户这次请求的token值已经过期，程序会重新签发token。
     * 4、当该用户这次请求的token值已经过期，authorizationUserInfo返回null，则直接拦截。
     * 5、当用户请求接口时遇到第3种情况时，会给Response的Header中设置token字段，该token映射的v为新的有效token。
     * 6、注：当前端检测到Response的Header中有token字段时需替换旧的token内容。
     *
     * @param qmUserInfo 参与签名的信息对象
     * @return
     * @throws QmSecurityQmUserInfoException
     * @throws QmSecurityCreateTokenException
     */
    String login(QmUserInfo qmUserInfo) throws QmSecurityQmUserInfoException, QmSecurityCreateTokenException;

    /**
     * 当头部带有`token`的字符串被校验成功并授权进入接口后，可以调用此接口获取当前用户的具体信息。
     *
     * @return
     */
    QmUserInfo getUserInfo();

    /**
     * 动态获取当前用户角色权限
     *
     * @return List<String> 返回角色权限集合
     */
    List<String> extractMatchingURI();
}
