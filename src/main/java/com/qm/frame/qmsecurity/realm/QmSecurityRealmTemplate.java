package com.qm.frame.qmsecurity.realm;

import com.qm.frame.qmsecurity.entity.QmUserInfo;

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
    public List<String> authorizationMatchingURI(QmUserInfo qmUserInfo) {
        List<String> list = new ArrayList<>();
        list.add("/**");
        return list;
    }

    @Override
    public QmUserInfo authorizationUserInfo(QmUserInfo qmUserInfo, HttpServletRequest request, HttpServletResponse response) {
        return qmUserInfo;
    }

    @Override
    public void noPassCallBack(int type, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.getWriter().print("安全检测不通过!");
    }
}
