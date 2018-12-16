//package com.qm.frame.wechat.authorization;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.qm.frame.basic.util.PropertiesUtil;
//import com.qm.frame.redis.QmRedisUtil;
//import com.qm.frame.wechat.QmWechat;
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.util.EntityUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.net.URLConnection;
//import java.net.URLEncoder;
//import java.security.MessageDigest;
//import java.util.Formatter;
//import java.util.UUID;
//
///**
// * Copyright © 2018浅梦工作室. All rights reserved.
// * @author: 浅梦
// * @date: 2018年11月24日 上午2:34:20
// * @Description: 微信公众号授权工具（暂时未测试）
// */
//@SuppressWarnings({"deprecation","resource"})
//public final class WechaAuthorization extends QmWechat {
//
//	private static final Logger LOG = LoggerFactory.getLogger(WechaAuthorization.class);
//
//	/**
//	 * APPID可在微信公众号中获取
//	 */
// 	public static final String APPID;
// 	/**
// 	 * APPSECRET可在微信公众号中获取
// 	 */
//    public static final String APPSECRET;
//
//    static {
//    	APPID =  PropertiesUtil.get("wechat.appid");
//    	APPSECRET = PropertiesUtil.get("wechat.appsecret");
//    }
//
//
//    /**
//     * 微信授权
//     * @param response
//     * @param redirectURL 回调地址
//     * @throws Exception
//     */
//	public static void doWechatAuth(HttpServletResponse response, String redirectURL) throws Exception {
//		LOG.info("◆◆◆◆◆进入微信授权◆◆◆◆◆");
//		StringBuffer url = new StringBuffer();
//		url.append("https://open.weixin.qq.com/connect/oauth2/authorize");
//		LOG.info("◆◆◆◆◆APPID：" + APPID);
//		url.append("?appid=" + APPID);
//		LOG.info("◆◆◆◆◆回调地址：" + URLEncoder.encode(redirectURL, "UTF-8"));
//		url.append("&redirect_uri=" + URLEncoder.encode(redirectURL, "UTF-8")); // 重定向地址
//		url.append("&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect");
//		response.sendRedirect(url.toString());
//	}
//
//	/**
//	 * 微信获取用户信息
//	 * @param request
//	 * @return
//	 */
//	public static WeChatUserInfo getWechatUserInfo(HttpServletRequest request) {
//		try {
//			String code = request.getParameter("code");
//			LOG.info("◆◆◆◆◆回调Code：" + code);
//			if (code == null) {
//				return null;
//			}
//			StringBuffer url = new StringBuffer();
//			url.append("https://api.weixin.qq.com/sns/oauth2/access_token");
//			url.append("?appid=" + APPID);
//			url.append("&secret=" + APPSECRET);
//			url.append("&code=" + code);
//			url.append("&grant_type=authorization_code");
//			JSONObject jsonObject = doGetJson(url.toString());
//			WeChatUserInfo weChatUserInfo = new WeChatUserInfo();
//			String openId = jsonObject.getString("openid");
//			if (openId == null || openId.trim().equals("")) {
//				return null;
//			}
//			weChatUserInfo.setOpenId(openId);
//			String token = jsonObject.getString("access_token");
//			weChatUserInfo.setToken(token);
//			StringBuffer infoUrl = new StringBuffer();
//			infoUrl.append("https://api.weixin.qq.com/sns/userinfo");
//			infoUrl.append("?access_token=" + token);
//			infoUrl.append("&openid=" + openId);
//			infoUrl.append("&lang=zh_CN");
//			JSONObject resJson = doGetJson(infoUrl.toString());
//			LOG.info("◆◆◆◆◆微信用户信息如下：◆◆◆◆◆\n" + resJson.toString());
//			weChatUserInfo.setHeadImgUrl(resJson.getString("headimgurl"));
//			;
//			weChatUserInfo.setNickName(resJson.getString("nickname"));
//			weChatUserInfo.setSex(resJson.getInteger("sex"));
//			weChatUserInfo.setProvince(resJson.getString("province"));
//			weChatUserInfo.setCity(resJson.getString("city"));
//			weChatUserInfo.setCountry(resJson.getString("country"));
//			weChatUserInfo.setPrivilege(resJson.getString("privilege"));
//			weChatUserInfo.setUnionid(resJson.getString("unionid"));
//			return weChatUserInfo;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
//
//	/**
//	 * 请求获取JSON格式数据
//	 * @param url
//	 * @return
//	 */
//	private static JSONObject doGetJson(String url) {
//		try {
//			JSONObject jsonObject = null;
//			DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
//			HttpGet httpGet = new HttpGet(url);
//			HttpResponse httpResponse = defaultHttpClient.execute(httpGet);
//			HttpEntity httpEntity = httpResponse.getEntity();
//			if (httpEntity != null) {
//				String result = EntityUtils.toString(httpEntity, "UTF-8");
//				new JSONObject();
//				jsonObject = JSON.parseObject(result);
//				LOG.info("jsonObject:  " + jsonObject);
//			}
//			httpGet.releaseConnection();
//			return jsonObject;
//		} catch (IOException e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
//
//	/**
//	 * 模拟get请求
//	 * @param url
//	 * @param charset
//	 * @param timeout
//	 * @return
//	 */
//	private static String sendGet(String url, String charset, int timeout) {
//		String result = "";
//		try {
//			URL u = new URL(url);
//			try {
//				URLConnection conn = u.openConnection();
//				conn.connect();
//				conn.setConnectTimeout(timeout);
//				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
//				String line = "";
//				while ((line = in.readLine()) != null) {
//
//					result = result + line;
//				}
//				in.close();
//			} catch (IOException e) {
//				return result;
//			}
//		} catch (MalformedURLException e) {
//			return result;
//		}
//
//		return result;
//	}
//
//	/**
//	 * 获取微信签名
//	 * @param url
//	 * @return
//	 */
//	public static WechatSign getSignInfo(String url) {
//		String jsapi_ticket = (String) QmRedisUtil.get("jsapi_ticket");
//		if (jsapi_ticket == null || jsapi_ticket.trim().equals("")) {
//			jsapi_ticket = getJSApiTicket();
//			QmRedisUtil.set("jsapi_ticket", jsapi_ticket, 7000);
//		}
//		String nonce_str = UUID.randomUUID().toString();
//		String timestamp = Long.toString(System.currentTimeMillis() / 1000);
//		StringBuffer signStr = new StringBuffer();
//		String signature = "";
//
//		// 注意这里参数名必须全部小写，且必须有序
//		signStr.append("jsapi_ticket=" + jsapi_ticket);
//		signStr.append("&noncestr=" + nonce_str);
//		signStr.append("&timestamp=" + timestamp);
//		signStr.append("&url=" + url);
//
//		System.out.println("=============String--" + signStr.toString());
//		try {
//			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
//			crypt.reset();
//			crypt.update(signStr.toString().getBytes("UTF-8"));
//			signature = byteToHex(crypt.digest());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		WechatSign wechatSign = new WechatSign();
//		wechatSign.setJsapi_ticket(jsapi_ticket);
//		wechatSign.setNonce_str(nonce_str);
//		;
//		wechatSign.setTimestamp(timestamp);
//		wechatSign.setSignature(signature);
//		return wechatSign;
//	}
//
//	private static String byteToHex(final byte[] hash) {
//		Formatter formatter = new Formatter();
//		for (byte b : hash) {
//			formatter.format("%02x", b);
//		}
//		String result = formatter.toString();
//		formatter.close();
//		return result;
//	}
//
//	/**
//	 * 获取access_token
//	 * @return
//	 */
//	public static String getAccessToken() {
//		String appid = APPID;// 应用ID
//		String appSecret = APPSECRET;// (应用密钥)
//		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appid + "&secret="
//				+ appSecret;
//		String backData = sendGet(url, "utf-8", 10000);
//		String accessToken = (String) JSONObject.parseObject(backData).get("access_token");
//		return accessToken;
//	}
//
//	/**
//	 * 获取jsapi_ticket
//	 * @return
//	 */
//	public static String getJSApiTicket() {
//		// 获取token
//		String acess_token = getAccessToken();
//		String urlStr = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + acess_token
//				+ "&type=jsapi";
//		String backData = sendGet(urlStr, "utf-8", 10000);
//		String ticket = (String) JSONObject.parseObject(backData).get("ticket");
//		return ticket;
//
//	}
//}
