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
     * 提供给调度者的授权方法。
     * 该函数只有当系统缓存中没有该角色才会进行调用。
     * 每调用一次将会把相关权限列表刷新到缓存中。
     *
     * @param qmUserInfo
     * @return
     */
    List<String> authorizationPermissions(QmUserInfo qmUserInfo);


    /**
     * 提供给调度者的检测用户是否合法函数
     * 当用户每次请求时进入安全监测时会调用该函数。
     * 每调用一次将会把该用户对象刷新到缓存中。
     * 注意：返回null表示本次检测不通过，框架自动进行拦截。
     *
     * @param qmUserInfo
     * @return
     */
    QmUserInfo authorizationUserInfo(QmUserInfo qmUserInfo);

    /**
     * 当安全检测不通过时回调该函数
     * 回调该函数最好的处理方式是处理相关业务并推送错误信息。
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @param type     1=找不到用户信息,登录超时 | 2=检测不到token拒绝访问 | 3=Token失效或已过期 | 4=请求ip校验失败 | 5=权限不足,拒绝访问
     */
    void noPassCallBack(HttpServletRequest request, HttpServletResponse response, int type) throws Exception;

}
