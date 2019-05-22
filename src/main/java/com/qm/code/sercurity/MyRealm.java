package com.qm.code.sercurity;

import com.alibaba.fastjson.JSON;
import com.qm.code.entity.User;
import com.qm.code.service.RoleService;
import com.qm.frame.basic.controller.QmCode;
import com.qm.frame.basic.controller.QmController;
import com.qm.frame.qmsecurity.entity.QmUserInfo;
import com.qm.frame.qmsecurity.realm.QmSecurityRealm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Copyright © 2018浅梦工作室}. All rights reserved.
 *
 * @author 浅梦
 * @date 2018/12/23 2:20
 * @Description 自定义realm
 */
@Component
public class MyRealm extends QmController implements QmSecurityRealm {

    @Autowired
    private RoleService roleService;


    @Override
    public void noPassCallBack(int type, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.getWriter().print(super.sendJSON(QmCode._103));
    }

    @Override
    public List<String> authorizationMatchingURI(QmUserInfo qmUserInfo) {
        // 获取用户对象
        User user = JSON.parseObject(qmUserInfo.getUser(),User.class);
        // 获取角色id
        int roleId = user.getRoleId();
        // 获取该角色的权限集合
        List<String> matchUri = roleService.getAuthListByRoleId(roleId);
        return matchUri;
    }

    @Override
    public QmUserInfo authorizationUserInfo(QmUserInfo qmUserInfo, HttpServletRequest request, HttpServletResponse response) {
        return qmUserInfo;
    }
}
