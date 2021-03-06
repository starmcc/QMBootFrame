package com.qm.frame.basic.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.qm.frame.basic.config.QmFrameConstants;
import com.qm.frame.basic.util.AesUtil;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright © 2018浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2018年11月24日 上午1:42:26
 * @Description 父类Controller, 编写Controller请继承该类。
 */
public class QmController {

    private static final Logger LOG = LoggerFactory.getLogger(QmController.class);

    /**
     * 接口回调方法
     *
     * @param code QmCode
     * @return
     */
    public String sendJSON(QmCode code) {
        Map<String, Object> responseMap = new HashMap<>(16);
        responseMap.put("code", code.getCode());
        responseMap.put("msg", QmCode.getMsg(code));
        responseMap.put("data", null);
        responseMap.put("responseTime", new Date());
        return this.parseJsonToResponse(responseMap);
    }

    /**
     * 接口回调方法
     *
     * @param code QmCode
     * @param data 传递数据
     * @return
     */
    public String sendJSON(QmCode code, Object data) {
        Map<String, Object> responseMap = new HashMap<>(16);
        responseMap.put("code", code.getCode());
        responseMap.put("msg", QmCode.getMsg(code));
        responseMap.put("data", data);
        responseMap.put("responseTime", new Date());
        return this.parseJsonToResponse(responseMap);
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
        Map<String, Object> responseMap = new HashMap<>(16);
        responseMap.put("code", code.getCode());
        responseMap.put("msg", msg);
        responseMap.put("data", data);
        responseMap.put("responseTime", new Date());
        return this.parseJsonToResponse(responseMap);
    }

    /**
     * 解析请求json字符串
     *
     * @param value
     * @return
     */
    public String parseRequestJson(String value) {
        JSONObject jsonObject = JSONObject.parseObject(value);
        String json = jsonObject.getString(QmFrameConstants.REQUEST_DATA_KEY);
        if (QmFrameConstants.AES_START) {
            try {
                json = AesUtil.decryptAES(json);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return json;
    }

    /**
     * 转换json
     *
     * @param responseMap
     * @return
     */
    private String parseJsonToResponse(Map<String, Object> responseMap) {
        //SerializerFeature.WriteMapNullValue设置后,返回Bean时字段为空时默认返回null
        String value = JSONObject.toJSONString(responseMap, SerializerFeature.WriteMapNullValue);
        value = StringEscapeUtils.unescapeJava(value);
        try {
            if (QmFrameConstants.AES_START) {
                value = AesUtil.encryptAES(value);
                Map<String, String> resMap = new HashMap<>(16);
                resMap.put(QmFrameConstants.RESPONSE_DATA_KEY, value);
                return StringEscapeUtils.unescapeJava(JSONObject.toJSONString(resMap, SerializerFeature.WriteMapNullValue));
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.debug("加密失败");
        }
        Map<String, Map<String, Object>> resMap = new HashMap<>(16);
        resMap.put(QmFrameConstants.RESPONSE_DATA_KEY, responseMap);
        return JSONObject.toJSONString(resMap, SerializerFeature.WriteMapNullValue);
    }
}
