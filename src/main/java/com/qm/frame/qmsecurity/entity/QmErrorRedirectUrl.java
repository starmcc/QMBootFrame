package com.qm.frame.qmsecurity.entity;

/**
 * Copyright © 2019浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2019/2/11 19:35
 * @Description 当登录校验失败，鉴权失败等重定向的路径
 */
public class QmErrorRedirectUrl {

    private String notLoginRedirectUrl;
    private String permissionDeniedRedirectUrl;

    public String getNotLoginRedirectUrl() {
        return notLoginRedirectUrl;
    }

    public void setNotLoginRedirectUrl(String notLoginRedirectUrl) {
        this.notLoginRedirectUrl = notLoginRedirectUrl;
    }

    public String getPermissionDeniedRedirectUrl() {
        return permissionDeniedRedirectUrl;
    }

    public void setPermissionDeniedRedirectUrl(String permissionDeniedRedirectUrl) {
        this.permissionDeniedRedirectUrl = permissionDeniedRedirectUrl;
    }
}
