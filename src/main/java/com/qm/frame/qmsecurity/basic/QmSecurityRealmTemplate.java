package com.qm.frame.qmsecurity.basic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright © 2019浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2019/5/7 1:45
 * @Description Realm实现模板
 */
public class QmSecurityRealmTemplate implements QmSecurityRealm {

    @Override
    public List<String> authorizationPermissions(int roleId) {
        List<String> list = new ArrayList<>();
        list.add("/**");
        return list;
    }

    @Override
    public void noPassCallBack(HttpServletRequest request, HttpServletResponse response, int type) {

    }
}
