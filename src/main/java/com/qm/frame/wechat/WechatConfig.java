package com.qm.frame.wechat;

import com.qm.frame.basic.util.PropertiesUtil;

/**
 * Copyright © 2018浅梦工作室}. All rights reserved.
 *
 * @author 浅梦
 * @date 2018/12/17 13:33
 * @Description: TODO
 */
public class WechatConfig {
    /**
     * APPID,该值请在微信公众号中获取。
     */
    public static final String APPID = PropertiesUtil.getValueByFileName("appid","wechat.properties");

    /**
     * 商户ID,该值请在微信公众号关联的商户号中进行获取，一般为10个数字
     */
    public static final String MCH_ID = PropertiesUtil.getValueByFileName("mch_id","wechat.properties");

    /**
     * 微信【商户】秘钥,该值请在微信公众号关联的商户号中进行获取
     */
    public static final String MCH_KEY = PropertiesUtil.getValueByFileName("mch_key","wechat.properties");

    /**
     * 接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。
     */
    public static final String NOTIFY_URL = PropertiesUtil.getValueByFileName("notify_url","wechat.properties");

    /**
     * 微信授权成功时的回调地址
     */
    public static final String REDIRECT_URL = PropertiesUtil.getValueByFileName("redirect_url","wechat.properties");
    /**
     * APPSECRET可在微信公众号中获取
     */
    public static final String APPSECRET = PropertiesUtil.getValueByFileName("appsecret","wechat.properties");
}
