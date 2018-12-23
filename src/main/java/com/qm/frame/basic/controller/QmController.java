package com.qm.frame.basic.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.qm.frame.basic.Constant.QmConstant;
import com.qm.frame.basic.util.AESUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright © 2018浅梦工作室. All rights reserved.
 *
 * @author: 浅梦
 * @date: 2018年11月24日 上午1:42:26
 * @Description: 父类Controller, 编写Controller请继承该类。
 */
public @Component
class QmController {

    private static final Logger LOG = LoggerFactory.getLogger(QmController.class);

    @Autowired
    protected HttpServletRequest request;
    @Autowired(required = false)
    protected HttpServletResponse response;
    @Autowired
    protected HttpSession session;
    @Autowired
    protected QmConstant qmConstant;

    /**
     * 接口回调方法
     *
     * @param code QmCode
     * @return
     */
    public String sendJSON(QmCode code) {
        JSONObject resultJson = new JSONObject();
        QmResponse qmResponse = new QmResponse();
        qmResponse.setCode(code.getCode());
        qmResponse.setMsg(QmCode.getMsg(code));
        qmResponse.setData(null);
        qmResponse.setResponseTime(new Date());
        //SerializerFeature.WriteMapNullValue设置后,返回Bean时字段为空时默认返回null
        String value = JSON.toJSONString(qmResponse, SerializerFeature.WriteMapNullValue);
        try {
            if (qmConstant.getAesConstant().isStart()) {
                value = AESUtil.encryptAES(value);
                resultJson.put(qmConstant.getSendConstant().getResponseDataKey(), value);
            } else {
                resultJson.put(qmConstant.getSendConstant().getResponseDataKey(), qmResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.debug("加密失败");
        }
        return resultJson.toJSONString();
    }

    /**
     * main
     */
    public static void main(String[] args) {
        //...start...
        Map<String, Object> map = new HashMap<>();
        map.put("test", "test");

    }

    /**
     * 接口回调方法
     *
     * @param code QmCode
     * @param data 传递数据
     * @return
     */
    public String sendJSON(QmCode code, Object data) {
        JSONObject resultJson = new JSONObject();
        QmResponse qmResponse = new QmResponse();
        qmResponse.setCode(code.getCode());
        qmResponse.setMsg(QmCode.getMsg(code));
        qmResponse.setData(data);
        qmResponse.setResponseTime(new Date());
        //SerializerFeature.WriteMapNullValue设置后,返回Bean时字段为空时默认返回null
        String value = JSON.toJSONString(qmResponse, SerializerFeature.WriteMapNullValue);
        try {
            if (qmConstant.getAesConstant().isStart()) {
                value = AESUtil.encryptAES(value);
                resultJson.put(qmConstant.getSendConstant().getResponseDataKey(), value);
            } else {
                resultJson.put(qmConstant.getSendConstant().getResponseDataKey(), qmResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.debug("加密失败");
        }
        return resultJson.toJSONString();
    }

    /**
     * 接口回调方法
     *
     * @param code QmCode
     * @param msg  自定义消息
     * @param data 传递数据
     * @return
     */
    public String sendJSON(QmCode code, String msg, Object data) {
        JSONObject resultJson = new JSONObject();
        QmResponse qmResponse = new QmResponse();
        qmResponse.setCode(code.getCode());
        qmResponse.setMsg(msg);
        qmResponse.setData(data);
        qmResponse.setResponseTime(new Date());
        //SerializerFeature.WriteMapNullValue设置后,返回Bean时字段为空时默认返回null
        String value = JSON.toJSONString(qmResponse, SerializerFeature.WriteMapNullValue);
        try {
            if (qmConstant.getAesConstant().isStart()) {
                value = AESUtil.encryptAES(value);
                resultJson.put(qmConstant.getSendConstant().getResponseDataKey(), value);
            } else {
                resultJson.put(qmConstant.getSendConstant().getResponseDataKey(), qmResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.debug("加密失败");
        }
        return resultJson.toJSONString();
    }

}
