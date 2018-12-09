package com.qm.code.controller;

import com.qm.code.entity.User;
import com.qm.code.service.UserService;
import com.qm.frame.basic.body.QmBody;
import com.qm.frame.basic.controller.QmCode;
import com.qm.frame.basic.controller.QmController;
import com.qm.frame.qmsecurity.entity.QmSecInfo;
import com.qm.frame.qmsecurity.manager.QmSecurity;
import com.qm.frame.qmsecurity.note.QmSecurityAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright © 2018浅梦工作室}. All rights reserved.
 * @author 浅梦
 * @date 2018/12/9 19:39
 * @Description: 权限控制示例controller
 */
@RestController
@RequestMapping("/security")
public class SecurityController extends QmController {

    /**
     * 注入权限框架
     */
    @Autowired
    private QmSecurity qmSecurity;
    /**
     * 注入登录时调动的UserService
     */
    @Autowired
    private UserService userService;

    /**
     * 登录方法
     * 利用权限框架登录
     * @param userName
     * @param password
     * @return
     */
    @PostMapping("/login")
    public String login(@QmBody String userName,
                        @QmBody String password){
        // 数据库登录操作
        User user = userService.login(userName,password);
        if (user == null) {
            return super.sendJSON(QmCode._104);
        }
        // 创建一个QmSecInfo用户对象
        QmSecInfo qmSecInfo = new QmSecInfo();
        // 用户登录成功的主要检索id
        qmSecInfo.setLoginCode(String.valueOf(user.getId()));
        // 用户登录成功后设置角色id，往后会通过角色id查找对应的权限。
        qmSecInfo.setRoleId(user.getRoleId());
        // 默认角色id为1的，是超级管理员角色
        if (user.getRoleId() == 1) {
            // 设置该用户为超级管理员 则该用户所有权限通过。
            qmSecInfo.setSuperUser(true);
        }else {
            // 需要权限校验用户则设为false
            qmSecInfo.setSuperUser(false);
        }
        // 如果需要在token中封装其他用户信息，则可以创建infoMap,将信息存放进去
        // 之后可以通过QmSecInfo对象获取到这个infoMap
        Map<String, String> infoMap = new HashMap<String, String>();
        infoMap.put("userName",user.getUserName());
        qmSecInfo.setInfoMap(infoMap);
        // 最后一步将设置好的QmSecInfo用户对象传递给qmSecurity的login方法。
        // 后面2个参数为时间类型和具体时间数。为token设置失效时间。
        qmSecInfo = qmSecurity.login(qmSecInfo,5,1);
        if (qmSecInfo == null) {
            // 如果token生成失败，则会返回null，或参数提供不足，或签名错误。
            return super.sendJSON(QmCode._500);
        }
        // 成功后可以直接返回token到前端。
        return super.sendJSON(QmCode._1,qmSecInfo.getToken());
    }


    /**
     * 该方法为测试权限示例。
     * 首先定义了一个QmSecurityAPI的注解
     * 如果方法上提供该注解，则需要前端在header传递用户登录成功后的token
     * 如果校验token失败直接返回错误json信息,不会进入方法体。
     * 如果校验成功则会根据注解上的key值进行鉴权处理。
     * 如果该用户的角色存在该权限则放行通过。
     * 如果鉴权失败则直接返回错误josn信息。
     * 如果key是空的默认为只需要校验token成功即可进入方法体。
     * @return
     */
    @GetMapping("/show")
    @QmSecurityAPI(key="show")
    public String show(){
        return super.sendJSON(QmCode._1,"ok",null);
    }
}
