package com.qm.code.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.qm.code.entity.User;
import com.qm.code.service.UserService;
import com.qm.frame.basic.body.QmBody;
import com.qm.frame.basic.controller.QmCode;
import com.qm.frame.basic.controller.QmController;

/**
 * Copyright © 2018浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2018年11月24日 下午9:06:39
 * @Description Controller控制器示例
 */
@RestController
@RequestMapping("/restful")
public class ResController extends QmController {

    @Autowired
    private UserService userService;

    /**
     * REST风格以参数以URL进行传递
     *
     * @return
     */
    @GetMapping("/demo1/{id}")
    public String demo1(@PathVariable int id) {
        return super.sendJSON(QmCode._1, id);
    }


    /**
     * 该方法以POST进行请求,并以body的形式传递json参数。
     * 传递格式为：
     * {
     *      "value":{
     *          "test":"zhangsan"
     *      }
     * }
     * 返回的则是相同的格式。并且调用框架中的QmController的返回格式
     * QmBody 是RequestBody的封装，是直接解释value后的json，自动装配参数列表的类型。
     *
     * @return
     */
    @PostMapping("/demo2")
    public String demo2(@QmBody String testParams) {
        return super.sendJSON(QmCode._1, testParams);
    }

    /**
     * 调用通用查询demo
     *
     * @return
     */
    @GetMapping("/demo3")
    public String demo3() {
        List<User> userLis = userService.getAutoList(new User());
        return super.sendJSON(QmCode._1, userLis);
    }


    /**
     * 调用通用查询demo
     *
     * @return
     */
    @GetMapping("/demo4")
    public String demo4() {
        List<User> userLis = userService.getList(new User());
        return super.sendJSON(QmCode._1, userLis);
    }


}
