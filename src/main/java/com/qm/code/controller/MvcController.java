package com.qm.code.controller;

import com.qm.frame.qmsecurity.note.QmPass;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Copyright © 2019浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2019/1/4 14:52
 * @Description TODO
 */
@Controller
@RequestMapping("/mvc")
public class MvcController {

    @GetMapping("/index")
    @QmPass
    public String index(){
        return "/index";
    }
}
