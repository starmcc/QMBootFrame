package com.qm.frame.basic.exception;


import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.qm.frame.basic.controller.QmCode;
import com.qm.frame.basic.controller.QmController;

/**
 * Copyright © 2018浅梦工作室. All rights reserved.
 * @author: 浅梦
 * @date: 2018年11月24日 上午1:30:20
 * @Description: 异常处理类、将程序中可能出现的错误进行捕获，然后返回JSON格式的错误信息
 */
@ControllerAdvice
@RestController
@RequestMapping( value = "/error")
public class QmExceptionHandler extends QmController{
 
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public String httpRequestMethodNotSupportedException(HttpServletResponse response,
    		Exception e) {
    	response.setStatus(200);
    	 return super.sendJSON(QmCode._405);
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public String IllegalArgumentException(HttpServletResponse response,
    		Exception e) {
    	response.setStatus(200);
    	 return super.sendJSON(QmCode._100);
    }
    
    @ExceptionHandler(InstantiationException.class)
    public String InstantiationException(HttpServletResponse response,
    		Exception e) {
    	response.setStatus(200);
    	 return super.sendJSON(QmCode._100);
    }
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public String httpMediaTypeNotSupportedException(HttpServletResponse response,
    		Exception e) {
        response.setStatus(200);
        return super.sendJSON(QmCode._415);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(value=HttpStatus.NOT_FOUND)
    public String notFoundPage404(HttpServletResponse response,
    		Exception e) {
        response.setStatus(200);
        return super.sendJSON(QmCode._404);
    }
    
    @ExceptionHandler(Exception.class)
    public String defaultException(HttpServletResponse response,Exception e) {
    	e.printStackTrace();
        response.setStatus(200);
        return super.sendJSON(QmCode._500);
    }

}