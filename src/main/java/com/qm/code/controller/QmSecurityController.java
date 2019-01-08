package com.qm.code.controller;

import com.qm.frame.basic.controller.QmCode;
import com.qm.frame.basic.controller.QmController;
import com.qm.frame.qmsecurity.basic.QmSecurityUtils;
import com.qm.frame.qmsecurity.entity.QmTokenInfo;
import com.qm.frame.qmsecurity.manager.Qmbject;
import com.qm.frame.qmsecurity.note.QmPass;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Copyright © 2018浅梦工作室}. All rights reserved.
 *
 * @author 浅梦
 * @date 2018/12/23 3:00
 * @Description QmSecurity权限框架测试用例
 */
@RestController
@RequestMapping("/system")
public class QmSecurityController extends QmController {

    @QmPass
    @GetMapping("/login")
    public String login() {
        // 利用QmSecurityUtils获取qmbject实例。
        Qmbject qmbject = QmSecurityUtils.getQmbject();
        // 创建token签名信息QmTokenInfo，并配置该对象的信息
        QmTokenInfo qmTokenInfo = new QmTokenInfo();
        // userName为必须字段，用户唯一识别
        qmTokenInfo.setUserName("username");
        // 角色id为必须字段，用户角色唯一id
        qmTokenInfo.setRoleId(1);
        // 调用login方法，并设置他的过期时间，生成token
        String token = qmbject.login(qmTokenInfo, 60 * 60);
        // 将token返回给前端
        return super.sendJSON(QmCode._1, token);
    }

    @QmPass(needLogin = true)
    @GetMapping("/hello")
    public String hello() {
        return super.sendJSON(QmCode._1, "hello");
    }

    @GetMapping("/world")
    public String world() {
        return super.sendJSON(QmCode._1, "world");
    }

}
