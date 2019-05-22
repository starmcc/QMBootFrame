package com.qm.frame.qmsecurity.realm;

import com.qm.frame.qmsecurity.entity.QmUserInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
     * 提供给调度者的授权接口。
     * 该接口只有当系统缓存中没有该用户URI信息才会进行调用。
     * 每调用一次将会把相关该用户URI放置到缓存中。
     *
     * @param qmUserInfo 用户对象
     * @return
     */
    List<String> authorizationMatchingURI(QmUserInfo qmUserInfo);


    /**
     * 提供给调度者的检测用户是否合法的接口
     * 当用户每次请求时进入安全监测时会调用该接口。
     * 每调用一次将会把该用户对象刷新到缓存中。
     * 注意：返回null表示本次检测不通过，框架自动进行拦截。
     *
     * @param qmUserInfo 用户对象
     * @param request    HttpServletRequest
     * @param response   HttpServletResponse
     * @return
     */
    QmUserInfo authorizationUserInfo(QmUserInfo qmUserInfo, HttpServletRequest request, HttpServletResponse response);

    /**
     * 当安全检测不通过时回调该接口
     * 回调该接口最好的处理方式是处理相关业务并推送错误信息。
     *
     * @param type     1=检测不到token拒绝访问 | 2=非法token,token提取失败 | 3=授权验证拦截 | 4=权限不足,拒绝访问
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @throws Exception
     */
    void noPassCallBack(int type, HttpServletRequest request, HttpServletResponse response) throws Exception;

}
