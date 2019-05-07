package com.qm.code.controller;

import com.alibaba.fastjson.JSON;
import com.qm.code.entity.User;
import com.qm.code.service.UserService;
import com.qm.frame.basic.controller.QmCode;
import com.qm.frame.basic.controller.QmController;
import com.qm.frame.qmsecurity.entity.QmSessionInfo;
import com.qm.frame.qmsecurity.entity.QmTokenInfo;
import com.qm.frame.qmsecurity.exception.QmSecuritySignTokenException;
import com.qm.frame.qmsecurity.basic.QmSecurityManager;
import com.qm.frame.qmsecurity.manager.Qmbject;
import com.qm.frame.qmsecurity.note.QmPass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

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

    @Autowired
    private UserService userService;


    @QmPass
    @GetMapping("/login")
    public String login() {
        User user = userService.login("admin","123");
        if (user == null) {
            // 登录失败
            return super.sendJSON(QmCode._2);
        }
        // 利用QmSecurityManager获取qmbject实例。
        Qmbject qmbject = QmSecurityManager.getQmbject();
        // 创建token签名信息QmTokenInfo，并配置该对象的信息
        QmTokenInfo qmTokenInfo = new QmTokenInfo();
        // identify为必须字段，用户唯一识别
        qmTokenInfo.setIdentify(user.getUserName());
        // 角色id为必须字段，用户角色唯一id
        qmTokenInfo.setRoleId(user.getRoleId());
        // 设置其他信息，这里直接把整个user转json储存起来，实际开发中请勿这样操作。
        Map<String,String> infoMap = new HashMap<>();
        infoMap.put("userJson", JSON.toJSONString(user));
        qmTokenInfo.setInfoMap(infoMap);
        qmTokenInfo.setExpireTime(60 * 60);
        // 调用login方法，并设置他的过期时间，生成token
        String token = null;
        try {
            token = qmbject.login(qmTokenInfo);
        } catch (QmSecuritySignTokenException e) {
            e.printStackTrace();
            System.out.println("签发token错误哦");
        }
        // 将token返回给前端
        return super.sendJSON(QmCode._1, token);
    }

    @QmPass
    @GetMapping("/loginForSession")
    public String loginForSession() {
        // 利用QmSecurityManager获取qmbject实例。
        Qmbject qmbject = QmSecurityManager.getQmbject();
        QmSessionInfo qmSessionInfo = new QmSessionInfo();
        qmSessionInfo.setUser("admin");
        qmSessionInfo.setRoleId(1);
        qmbject.loginForSession(qmSessionInfo,100);
        return super.sendJSON(QmCode._1);
    }

    @GetMapping("/test")
    public String test() {
        // 利用QmSecurityManager获取qmbject实例。
        Qmbject qmbject = QmSecurityManager.getQmbject();
        return super.sendJSON(QmCode._1,qmbject.getSessionInfo());
    }


    /**
     * 如果只需要登录，不需要授权，
     * 则可以在此设置QmPass的属性needLogin = true
     * @return
     */
    @QmPass(needLogin = true)
    @GetMapping("/hello")
    public String hello() {
        return super.sendJSON(QmCode._1, "helloWorld");
    }

    /**
     * 没有加QmPass则会进入登录校验和权限校验，
     * 只有当用户的角色拥有该路径权限时才允许访问。
     * @return
     */
    @GetMapping("/getUserInfo")
    public String getUserInfo() {
        // 可获取登录时的用户信息
        QmTokenInfo qmTokenInfo = QmSecurityManager.getQmbject().getTokenInfo();
        // 登录时设置的userCode
        String userCode = qmTokenInfo.getIdentify();
        // 登陆时设置的userCode
        Integer roleId = qmTokenInfo.getRoleId();
        // 登陆时设置的infoMap
        // 可获取登录时的其他信息
        Map<String,String> infoMap = qmTokenInfo.getInfoMap();
        // 转出封装
        Map<String,Object> resMap = new HashMap<>();
        resMap.put("userCode",userCode);
        resMap.put("roleId",roleId);
        resMap.put("infoMap",infoMap);
        return super.sendJSON(QmCode._1, resMap);
    }

}
