package com.qm.code.controller;

import com.qm.frame.basic.controller.QmController;
import com.qm.frame.qmsecurity.note.QmPass;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

/**
 * Copyright © 2019浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2019/1/4 14:52
 * @Description springBoot + thymeleaf 测试控制器
 */
@Controller
@RequestMapping("/mvc")
public class MvcController extends QmController {

    @QmPass
    @GetMapping("/index")
    public String index(){
        return "/index";
    }
}
