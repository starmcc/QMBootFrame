package com.qm.frame.basic.exception;


import com.qm.frame.basic.config.QmFrameContent;
import com.qm.frame.basic.controller.QmCode;
import com.qm.frame.basic.controller.QmController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Copyright © 2018浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2018年11月24日 上午1:30:20
 * @Description 异常处理类、将程序中可能出现的错误进行捕获，然后返回JSON格式的错误信息
 */
@ControllerAdvice
@Controller
@RequestMapping(value = "/error")
public class QmExceptionHandler extends QmController {

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public String httpRequestMethodNotSupportedException(HttpServletResponse response,
                                                         Exception e) {
        response.setStatus(200);
        return super.sendJSON(QmCode._405);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public String IllegalArgumentException(HttpServletResponse response,
                                           Exception e) {
        response.setStatus(200);
        return super.sendJSON(QmCode._100);
    }

    @ExceptionHandler(InstantiationException.class)
    @ResponseBody
    public String InstantiationException(HttpServletResponse response,
                                         Exception e) {
        response.setStatus(200);
        return super.sendJSON(QmCode._100);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseBody
    public String httpMediaTypeNotSupportedException(HttpServletResponse response,
                                                     Exception e) {
        response.setStatus(200);
        return super.sendJSON(QmCode._415);
    }

    /**
     * 找不到接口或视图名
     *
     * @param response
     * @param e
     * @return
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    public String notFoundPage404(HttpServletResponse response,
                                  Exception e) throws IOException {
        response.setStatus(200);
        return super.sendJSON(QmCode._404);
    }


    /**
     * 服务器错误异常拦截
     *
     * @param response
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public String defaultException(HttpServletResponse response, Exception e) throws IOException {
        e.printStackTrace();
        response.setStatus(200);
        return super.sendJSON(QmCode._500);
    }

}