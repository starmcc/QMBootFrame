package com.qm.code.controller;

import com.alibaba.fastjson.JSON;
import com.qm.code.entity.User;
import com.qm.code.service.UserService;
import com.qm.frame.basic.body.QmBody;
import com.qm.frame.basic.controller.QmCode;
import com.qm.frame.basic.controller.QmController;
import com.qm.frame.qmsecurity.entity.QmUserInfo;
import com.qm.frame.qmsecurity.exception.QmSecurityCreateTokenException;
import com.qm.frame.qmsecurity.exception.QmSecurityQmUserInfoException;
import com.qm.frame.qmsecurity.note.QmPass;
import com.qm.frame.qmsecurity.qmbject.QmSecurityManager;
import com.qm.frame.qmsecurity.qmbject.Qmbject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @Autowired
    private UserService userService;


    /**
     * 登录示例
     * 增加该@QmPass注解表示该方法不校验登录和权限。
     * @param username
     * @param password
     * @return
     */
    @QmPass
    @PostMapping("/login")
    public String login(@QmBody String username, @QmBody String password) {
        User user = userService.login(username, password);
        if (user == null) {
            // 登录失败
            return super.sendJSON(QmCode._2, "登录失败！", null);
        }
        // 利用QmSecurityManager获取qmbject实例。
        Qmbject qmbject = QmSecurityManager.getQmbject();
        // 创建token签名信息QmUserInfo，并配置该对象的信息
        QmUserInfo qmUserInfo = new QmUserInfo();
        // identify为必须字段，用户唯一识别
        qmUserInfo.setIdentify(user.getUserName());
        // 设置用户缓存对象
        qmUserInfo.setUser(JSON.toJSONString(user));
        // 设置token失效时间 (秒) 10分钟token失效
        qmUserInfo.setTokenExpireTime(60 * 10);
        // 调用login方法，并设置他的过期时间，生成token
        String token = null;
        try {
            token = qmbject.login(qmUserInfo);
        } catch (QmSecurityQmUserInfoException e) {
            // qmUserInfo参数异常
            e.printStackTrace();
            return super.sendJSON(QmCode._3, "qmUserInfo参数异常!", null);
        } catch (QmSecurityCreateTokenException e) {
            // 签发token时发生了异常
            e.printStackTrace();
            return super.sendJSON(QmCode._3, "签名时发生了异常!", null);
        }
        // 将token返回给前端
        return super.sendJSON(QmCode._1, token);
    }


    /**
     * 利用QmSecurityManager获取qmbject实例。
     *
     * @return
     */
    @GetMapping("/getQmbject")
    public String getQmbject() {
        Qmbject qmbject = QmSecurityManager.getQmbject();
        return super.sendJSON(QmCode._1, qmbject.getUserInfo());
    }


    /**
     * 如果只需要登录，不需要校验权限URI
     * 则可以在此设置QmPass的属性needLogin = true
     *
     * @return
     */
    @QmPass(needLogin = true)
    @GetMapping("/passAuth")
    public String passAuth() {
        return super.sendJSON(QmCode._1, "该请求不需要校验权限URI!");
    }

    /**
     * 没有加QmPass则会进入登录校验和权限校验，
     * 只有当用户拥有该路径权限时才允许访问。
     *
     * @return
     */
    @GetMapping("/getUserInfo")
    public String getUserInfo() {
        // 可获取登录时的用户信息
        QmUserInfo qmTokenInfo = QmSecurityManager.getQmbject().getUserInfo();
        // 获取到用户对象
        return super.sendJSON(QmCode._1, qmTokenInfo.getUser());
    }

}
