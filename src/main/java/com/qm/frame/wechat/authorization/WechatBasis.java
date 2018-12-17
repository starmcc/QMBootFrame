package com.qm.frame.wechat.authorization;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qm.frame.wechat.WechatConfig;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Formatter;

/**
 * Copyright © 2018浅梦工作室. All rights reserved.
 * @author: 浅梦
 * @date: 2018年11月24日 上午2:34:20
 * @Description: 微信公众号授权工具（暂时未测试）
 */
public final class WechatBasis {

    /**
     * 微信获取用户信息
     * @param request
     * @return
     */
    public static WeChatUserInfo getWechatUserInfo(HttpServletRequest request) {
        try {
            String code = request.getParameter("code");
            System.out.println("◆◆◆◆◆回调Code：" + code + "◆◆◆◆◆");
            if (code == null) {
                return null;
            }
            StringBuffer url = new StringBuffer();
            url.append("https://api.weixin.qq.com/sns/oauth2/access_token");
            url.append("?appid=" + WechatConfig.APPID);
            url.append("&secret=" + WechatConfig.APPSECRET);
            url.append("&code=" + code);
            url.append("&grant_type=authorization_code");
            JSONObject jsonObject = doGetJson(url.toString());
            WeChatUserInfo weChatUserInfo = new WeChatUserInfo();
            String openId = jsonObject.getString("openid");
            if (openId == null || openId.trim().equals("")) {
                return null;
            }
            weChatUserInfo.setOpenId(openId);
            String token = jsonObject.getString("access_token");
            weChatUserInfo.setToken(token);
            StringBuffer infoUrl = new StringBuffer();
            infoUrl.append("https://api.weixin.qq.com/sns/userinfo");
            infoUrl.append("?access_token=" + token);
            infoUrl.append("&openid=" + openId);
            infoUrl.append("&lang=zh_CN");
            JSONObject resJson = doGetJson(infoUrl.toString());
            System.out.println("◆◆◆◆◆微信用户信息如下：◆◆◆◆◆\n" + resJson.toString());
            weChatUserInfo.setHeadImgUrl(resJson.getString("headimgurl"));
            ;
            weChatUserInfo.setNickName(resJson.getString("nickname"));
            weChatUserInfo.setSex(resJson.getInteger("sex"));
            weChatUserInfo.setProvince(resJson.getString("province"));
            weChatUserInfo.setCity(resJson.getString("city"));
            weChatUserInfo.setCountry(resJson.getString("country"));
            weChatUserInfo.setPrivilege(resJson.getString("privilege"));
            weChatUserInfo.setUnionid(resJson.getString("unionid"));
            return weChatUserInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

	/**
	 * 请求获取JSON格式数据
	 * @param url
	 * @return
	 */
	public static JSONObject doGetJson(String url) {
		try {
			JSONObject jsonObject = null;
			DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);
			HttpResponse httpResponse = defaultHttpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			if (httpEntity != null) {
				String result = EntityUtils.toString(httpEntity, "UTF-8");
				new JSONObject();
				jsonObject = JSON.parseObject(result);
				System.out.println("jsonObject:  " + jsonObject);
			}
			httpGet.releaseConnection();
			return jsonObject;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 模拟get请求
	 * @param url
	 * @param charset
	 * @param timeout
	 * @return
	 */
	public static String sendGet(String url, String charset, int timeout) {
		String result = "";
		try {
			URL u = new URL(url);
			try {
				URLConnection conn = u.openConnection();
				conn.connect();
				conn.setConnectTimeout(timeout);
				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
				String line = "";
				while ((line = in.readLine()) != null) {

					result = result + line;
				}
				in.close();
			} catch (IOException e) {
				return result;
			}
		} catch (MalformedURLException e) {
			return result;
		}

		return result;
	}

	public static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

	/**
	 * 获取access_token
	 * @return
	 */
	public static String getAccessToken() {
		String appid = WechatConfig.APPID;// 应用ID
		String appSecret = WechatConfig.APPSECRET;// (应用密钥)
		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appid + "&secret="
				+ appSecret;
		String backData = WechatBasis.sendGet(url, "utf-8", 10000);
		String accessToken = (String) JSONObject.parseObject(backData).get("access_token");
		return accessToken;
	}

	/**
	 * 获取jsapi_ticket
	 * @return
	 */
	public static String getJSApiTicket() {
		// 获取token
		String acess_token = getAccessToken();
		String urlStr = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + acess_token
				+ "&type=jsapi";
		String backData = WechatBasis.sendGet(urlStr, "utf-8", 10000);
		String ticket = (String) JSONObject.parseObject(backData).get("ticket");
		return ticket;
	}
}
