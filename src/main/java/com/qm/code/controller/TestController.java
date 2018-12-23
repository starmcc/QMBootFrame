package com.qm.code.controller;

import com.qm.frame.basic.controller.QmCode;
import com.qm.frame.basic.controller.QmController;
import com.qm.frame.qmsecurity.note.QmPass;
import com.qm.frame.qmsecurity.basic.QmSecurityUtils;
import com.qm.frame.qmsecurity.entity.QmTokenInfo;
import com.qm.frame.qmsecurity.manager.Qmbject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Copyright © 2018浅梦工作室}. All rights reserved.
 *
 * @author 浅梦
 * @date 2018/12/23 3:00
 * @Description: QmSecurity权限框架测试用例
 */
@RestController
@RequestMapping("/system")
public class TestController extends QmController {

    @GetMapping("/login")
    @QmPass
    public String login() {
        Qmbject qmbject = QmSecurityUtils.getQmbject();
        QmTokenInfo qmTokenInfo = new QmTokenInfo();
        qmTokenInfo.setUserName("username");
        qmTokenInfo.setRoleId(1);
        String token = qmbject.login(qmTokenInfo, 100);
        return super.sendJSON(QmCode._1, token);
    }

    @GetMapping("/hello")
    public String hello() {
        return super.sendJSON(QmCode._1, "hello");
    }


}
