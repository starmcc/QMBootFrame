package com.qm.code.controller;


import com.qm.code.entity.Student;
import com.qm.code.service.RestfulService;
import com.qm.frame.basic.body.QmBody;
import com.qm.frame.basic.controller.QmCode;
import com.qm.frame.basic.controller.QmController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright © 2018浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2018年11月24日 下午9:06:39
 * @Description Controller控制器示例
 */
@RestController
@RequestMapping("/restful")
public class RestfulController extends QmController {

    @Autowired
    private RestfulService restfulService;

    /**
     * 该方法以POST进行请求,并以body的形式传递json参数。
     * 以Postman为例，选择Body > raw > content-type 选择 application/json
     * 传递格式为：
     * {
     * "value":{
     * "test":"zhangsan"
     * }
     * }
     * value为外层对象key,该key可在配置文件中配置。
     *
     * @param userName
     * @param reqTime
     * @param num
     * @param ttt
     * @return 返回的则是相同的格式。并且调用框架中的QmController的返回格式
     */
    @PostMapping("/demo1")
    public String demo1(@QmBody String userName,
                        @QmBody Date reqTime,
                        @QmBody int num,
                        @QmBody(required = false) String ttt,
                        @QmBody(required = false, key = "student") Student s) {
        // @QmBody
        // 当发起请求时，解析body的json字符串中value对象内的属性为参数列表。
        // 并对数据类型进行自动装配。
        // 该注解具有RequestBody的方式，具有@RequestParam的方法，使前后端数据可进行加密处理。
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("userName", userName);
        resMap.put("reqTime", reqTime);
        resMap.put("num", num);
        resMap.put("ttt", ttt);
        resMap.put("student", s);
        return super.sendJSON(QmCode._1, resMap);
    }

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
     * 调用通用查询demo
     *
     * @return
     */
    @GetMapping("/demo2")
    public String demo2() {
        List<Student> students = restfulService.getAutoList(new Student());
        return super.sendJSON(QmCode._1, students);
    }


    /**
     * 调用通用查询demo
     *
     * @return
     */
    @GetMapping("/demo3")
    public String demo3() {
        List<Student> userLis = restfulService.getList(new Student());
        return super.sendJSON(QmCode._1, userLis);
    }


}
