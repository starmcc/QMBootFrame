package com.qm.frame.wechat;

import com.qm.frame.wechat.pay.QmWechatCallBack;
import com.qm.frame.wechat.pay.QmWechatPayInfo;
import org.jdom.JDOMException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Copyright © 2018浅梦工作室}. All rights reserved.
 *
 * @author 浅梦
 * @date 2018/12/17 2:37
 * @Description: TODO
 */
public interface QmWechat {
    /**
     * 微信支付-统一下单
     * @param qmWechatPayInfo 微信支付信息实体类
     * @param apiKey 微信【商户】秘钥,该值请在微信公众号关联的商户号中进行获取
     * @param requestUrl 微信统一下单接口地址
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws IOException
     * @throws JDOMException
     */
    abstract Map<String,String> pay(QmWechatPayInfo qmWechatPayInfo, String apiKey, String requestUrl)
            throws IllegalArgumentException,
            IllegalAccessException,
            IOException,
            JDOMException;

    /**
     * 微信异步回调通知
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param qmWechatCallBack 回调函数 使用new QmWechatCallBack 进行参数列表回调方法调用。
     * @throws IOException
     * @throws JDOMException
     */
    abstract void payReadit(HttpServletRequest request,
                          HttpServletResponse response,
                          QmWechatCallBack qmWechatCallBack)
            throws IOException, JDOMException;
}
