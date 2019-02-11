package com.qm.frame.qmsecurity.entity;

/**
 * Copyright © 2019浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2019/2/11 19:35
 * @Description 当登录校验失败，鉴权失败等重定向的路径
 */
public class QmErrorRedirectUrl {

    private String notLoginURI = "/error";
    private String permissionDeniedURI = "/error";

    public String getNotLoginURI() {
        return notLoginURI;
    }

    public void setNotLoginURI(String notLoginURI) {
        this.notLoginURI = notLoginURI;
    }

    public String getPermissionDeniedURI() {
        return permissionDeniedURI;
    }

    public void setPermissionDeniedURI(String permissionDeniedURI) {
        this.permissionDeniedURI = permissionDeniedURI;
    }
}
