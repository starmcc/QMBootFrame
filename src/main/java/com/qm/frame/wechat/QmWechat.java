package com.qm.frame.wechat;

import com.qm.frame.basic.util.QmSpringManager;
import com.qm.frame.wechat.authorization.WeChatUserInfo;
import com.qm.frame.wechat.authorization.WechatBasis;
import com.qm.frame.wechat.authorization.WechatSign;
import com.qm.frame.wechat.pay.QmWechatCallBack;
import com.qm.frame.wechat.pay.QmWechatPayBasis;
import com.qm.frame.wechat.pay.QmWechatPayInfo;
import org.apache.commons.lang3.StringUtils;
import org.jdom.JDOMException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * Copyright © 2018浅梦工作室}. All rights reserved.
 *
 * @author 浅梦
 * @date 2018/12/17 2:37
 * @Description: TODO
 */
public class QmWechat {

    /**
     * 微信支付-统一下单
     * @param qmWechatPayInfo 微信支付信息实体类
     * @param requestUrl 微信统一下单接口地址
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws IOException
     * @throws JDOMException
     */
    public Map<String,String> pay(QmWechatPayInfo qmWechatPayInfo, String requestUrl)
            throws IllegalArgumentException,
            IllegalAccessException,
            IOException,
            JDOMException{
        // 签名
        String sign = QmWechatPayBasis.createSign("UTF-8",qmWechatPayInfo);
        //将微信信息对象置换为XML报文形式
        String requestXML = QmWechatPayBasis.getRequestXml(qmWechatPayInfo,sign);
        System.out.println("请求报文：\n" + requestXML);
        String resultXML = QmWechatPayBasis.httpsRequest(requestUrl, "POST", requestXML);
        System.out.println("返回报文：\n" + resultXML);
        Map<String,String> resMap = QmWechatPayBasis.doXMLParse(resultXML);
        System.out.println("返回具体信息：\n" + resMap.toString());
        return resMap;
    }

    /**
     * 微信异步回调通知
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param qmWechatCallBack 回调函数 使用new QmWechatCallBack 进行参数列表回调方法调用。
     * @throws IOException
     * @throws JDOMException
     */
    public void payReadit(HttpServletRequest request,
                          HttpServletResponse response,
                          QmWechatCallBack qmWechatCallBack)
            throws IOException, JDOMException{
        InputStream inStream = request.getInputStream();
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        outSteam.close();
        inStream.close();
        String resultStr  = new String(outSteam.toByteArray(),"utf-8");
        System.out.println("支付成功的回调："+resultStr);
        Map<String, String> resultInfo = QmWechatPayBasis.doXMLParse(resultStr);
        // 调用回调函数
        qmWechatCallBack.payResultInfo(resultInfo);
        // 通知微信服务器已接收到信息
        StringBuffer resXml = new StringBuffer();
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        resXml.append("<xml>");
        resXml.append("<return_code><![CDATA[SUCCESS]]></return_code>");
        resXml.append("<return_msg><![CDATA[OK]]></return_msg>");
        resXml.append("</xml>");
        out.write(resXml.toString().getBytes());
    }


    /**
     * 微信授权
     * @param response
     * @throws Exception
     */
    public void doWechatAuth(HttpServletResponse response) throws Exception {
        System.out.println("◆◆◆◆◆进入微信授权◆◆◆◆◆");
        StringBuffer url = new StringBuffer();
        url.append("https://open.weixin.qq.com/connect/oauth2/authorize");
        System.out.println("◆◆◆◆◆APPID：" + WechatConfig.APPID);
        url.append("?appid=" + WechatConfig.APPID);
        System.out.println("◆◆◆◆◆回调地址：" + URLEncoder.encode(WechatConfig.REDIRECT_URL, "UTF-8"));
        url.append("&redirect_uri=" + URLEncoder.encode(WechatConfig.REDIRECT_URL, "UTF-8")); // 重定向地址
        url.append("&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect");
        response.sendRedirect(url.toString());
    }

    /**
     * 微信授权成功后
     * 微信回调时调用此方法获取用户信息
     * @param request
     * @return
     */
    public WeChatUserInfo getWechatUserInfo(HttpServletRequest request) {
        return WechatBasis.getWechatUserInfo(request);
    }

    /**
     * 获取微信签名
     * @param url
     * @return
     */
    public WechatSign getSignInfo(String url) {
        // 利用Spring IOC 缓存机制 进行缓存jsapi_ticket
        ServletContext servletContext = QmSpringManager.getBean(ServletContext.class);
        String jsapi_ticket = (String) servletContext.getAttribute("jsapi_ticket");
        // 获取到这个jsapi_ticket的保存时间
        Date date = (Date)servletContext.getAttribute("jsapi_ticket_time");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 加上7000秒，因为jsapi_ticket是7200秒过期
        cal.add(Calendar.SECOND,7000);
        date = cal.getTime();
        if (StringUtils.isEmpty(jsapi_ticket)
                // 判断保存时间+7000秒是否小于现在的时间，如果小于则直接重新获取一次
                || date.getTime() < new Date().getTime()) {
            jsapi_ticket = WechatBasis.getJSApiTicket();
            servletContext.setAttribute("jsapi_ticket", jsapi_ticket);
            servletContext.setAttribute("jsapi_ticket_time",new Date());
        }
        // 开始进行签名业务
        String nonce_str = UUID.randomUUID().toString();
        String timestamp = Long.toString(System.currentTimeMillis() / 1000);
        StringBuffer signStr = new StringBuffer();
        String signature = "";

        // 注意这里参数名必须全部小写，且必须有序
        signStr.append("jsapi_ticket=" + jsapi_ticket);
        signStr.append("&noncestr=" + nonce_str);
        signStr.append("&timestamp=" + timestamp);
        signStr.append("&url=" + url);

        System.out.println("=============String--" + signStr.toString());
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(signStr.toString().getBytes("UTF-8"));
            signature = WechatBasis.byteToHex(crypt.digest());
        } catch (Exception e) {
            e.printStackTrace();
        }
        WechatSign wechatSign = new WechatSign();
        wechatSign.setJsapi_ticket(jsapi_ticket);
        wechatSign.setNonce_str(nonce_str);
        ;
        wechatSign.setTimestamp(timestamp);
        wechatSign.setSignature(signature);
        return wechatSign;
    }
}
