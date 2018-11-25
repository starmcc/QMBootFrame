package com.qm.frame.basic.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * Copyright © 2018浅梦工作室. All rights reserved.
 * @author: 浅梦
 * @date: 2018年11月24日 上午2:01:35
 * @Description: HttpApi工具类
 */
public class HttpApiUtil {
	/**
	 * 根据手机号获取手机号归属地
	 * 
	 * @param mobile
	 * @return 手机归属地字符串
	 * @author __GetZoneResult_ = {
	 * @author mts:'1585078',
	 * @author province:'江苏',
	 * @author catName:'中国移动',
	 * @author telString:'15850781443',
	 * @author areaVid:'30511',
	 * @author ispVid:'3236139',
	 * @author carrier:'江苏移动' }
	 * 
	 */
	public static String mobilePhoneHome(String mobile) {
		String url = "https://tcc.taobao.com/cc/json/mobile_tel_segment.htm";
		Map<String, Object> map = new HashMap<>();
		map.put("tel", mobile);
		String response = HttpClientUtil.sendGet(url, map);
		return response;
	}

	/**
	 * @Title: getIpAddress
	 * @param ip
	 * @return
	 * @Description: 获取ip地址的归属地 localAddress={city:"广州市", province:"广东省"}
	 */
	public static String getIpAddress(String ip) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ip", "127.0.0.1");
		String content = HttpClientUtil.sendGet("http://ip.ws.126.net/ipquery", params);
		String str = content.substring(content.indexOf("{") + 1, content.indexOf("}"));
		String[] st = str.split(",");
		String pro = st[1].substring(st[1].indexOf("province:\"") + 10, st[1].lastIndexOf("\""));
		String city = st[0].substring(st[0].indexOf("city:\"") + 6, st[0].lastIndexOf("\""));
		String ipAddress = pro + "-" + city;
		System.out.println(ipAddress);
		return ipAddress;
	}

	
	/**
	 * @Title: getHttpIp
	 * @param request
	 * @return
	 * @Description: 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址,
	 * @Description: 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？ 
	 * @Description: 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。 
	 * @Description: 如：X-Forwarded-For：192.168.1.110, 192.168.1.120, 192.168.1.130,192.168.1.100 
	 * @Description: 用户真实IP为： 192.168.1.110
	 */
    public static String getHttpIp(HttpServletRequest request) {  
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("WL-Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_CLIENT_IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getRemoteAddr();  
        }  
        return ip;  
    }  
}
