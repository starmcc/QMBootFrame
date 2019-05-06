package com.qm.frame.qmsecurity.basic;

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
     * 提供给调用者的授权方法
     * 该方法只有当系统缓存中没有该角色才会进行调用
     * 每调用一次就会将相关信息保存到缓存中
     *
     * @param token
     * @return
     */
    List<String> authorizationPermissions(int roleId);

    /**
     * 当安全校验不通过时回调该方法
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param type 1=找不到用户信息,登录超时,2=检测不到token拒绝访问,3=Token失效或已过期,4=请求ip校验失败,5=权限不足,拒绝访问
     */
    void noPassCallBack(HttpServletRequest request, HttpServletResponse response, int type);

}
