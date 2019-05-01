package com.qm.frame.basic.exception;


import com.qm.frame.basic.controller.QmCode;
import com.qm.frame.basic.controller.QmController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOG = LoggerFactory.getLogger(QmExceptionHandler.class);

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public String httpRequestMethodNotSupportedException(HttpServletResponse response,
                                                         Exception e) {
        LOG.info("请求方式错误,请核实请求方式 `GET` and `POST`");
        response.setStatus(200);
        return super.sendJSON(QmCode._405);
    }

    @ExceptionHandler(QmParamNullException.class)
    @ResponseBody
    public String QmParamNullException(HttpServletResponse response,
                                       Exception e) {
        LOG.info("缺少某些请求参数,请核实请求参数是否正确!");
        e.printStackTrace();
        response.setStatus(200);
        return super.sendJSON(QmCode._100);
    }

    @ExceptionHandler(QmParamErrorException.class)
    @ResponseBody
    public String QmParamErrorException(HttpServletResponse response,
                                        Exception e) {
        LOG.info("请求参数错误,请核实请求参数是否正确!");
        response.setStatus(200);
        return super.sendJSON(QmCode._101);
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
        LOG.info("请求地址错误,请核实请求地址是否正确!");
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
        LOG.info("服务器遇到了错误,请检查相关问题!");
        e.printStackTrace();
        response.setStatus(200);
        return super.sendJSON(QmCode._500);
    }

}