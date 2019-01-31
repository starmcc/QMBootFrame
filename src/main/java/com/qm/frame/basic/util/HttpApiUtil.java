package com.qm.frame.basic.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * Copyright © 2018浅梦工作室. All rights reserved.
 * 
 * @author 浅梦
 * @date 2018年11月24日 上午2:01:35
 * @Description HttpApi工具类
 */
public class HttpApiUtil {
	// 提交方式
	private final static String POST = "post";
	private final static String GET = "getQmResponseOut";
	/**
	 * 默认字符编码
	 */
	private final static String ENCODING = "UTF-8";

	/**
	 * @param mobile
	 * @return 手机归属地字符串
	 * @Description 
	 * {
	 * 		mts:'1585078',
	 *  	province:'江苏',
	 *  	catName:'中国移动',
	 *  	telString:'15850781443',
	 *  	areaVid:'30511',
	 *  	ispVid:'3236139',
	 *  	carrier:'江苏移动' 
	 *  }
	 * 	根据手机号获取手机号归属地
	 */
	public static String mobilePhoneHome(String mobile) {
		String url = "https://tcc.taobao.com/cc/json/mobile_tel_segment.htm";
		Map<String, Object> map = new HashMap<>();
		map.put("tel", mobile);
		String response = sendGet(url, map);
		return response;
	}

	/**
	 * @param ip
	 * @return
	 * @Description 获取ip地址的归属地
	 * localAddress={city:"广州市", province:"广东省"}
	 */
	public static String getIpAddress(String ip) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ip", "127.0.0.1");
		String content = sendGet("http://ip.ws.126.net/ipquery", params);
		String str = content.substring(content.indexOf("{") + 1, content.indexOf("}"));
		String[] st = str.split(",");
		String pro = st[1].substring(st[1].indexOf("province:\"") + 10, st[1].lastIndexOf("\""));
		String city = st[0].substring(st[0].indexOf("city:\"") + 6, st[0].lastIndexOf("\""));
		String ipAddress = pro + "-" + city;
		System.out.println(ipAddress);
		return ipAddress;
	}

	/**
	 * @param request
	 * @return
	 * @Description
	 * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址,
	 * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？
	 * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。
	 * 如：X-Forwarded-For：192.168.1.110, 192.168.1.120,
	 * 192.168.1.130,192.168.1.100
	 * 用户真实IP为： 192.168.1.110
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

	/**
	 * @param url
	 * @param params
	 * @param encoding
	 * @return
	 * @Description POST提交，指定字符编码
	 */
	public static String sendPost(String url, Map<String, Object> params, String encoding) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost post = new HttpPost(url);
		if (params != null) {
			// 处理参数
			HttpEntity entity = handleParam(params, encoding);
			// 添加参数
			post.setEntity(entity);
		}
		CloseableHttpResponse response = null;
		String content = null;
		try {
			response = httpClient.execute(post);
			content = EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(response, httpClient);
		}
		System.out.println(content);
		return content;
	}

	/**
	 * @param url
	 * @param params
	 * @return
	 * @Description POST提交，使用默认字符编码UTF-8
	 */
	public static String sendPost(String url, Map<String, Object> params) {
		return sendPost(url, params, ENCODING);
	}

	/**
	 * @param url
	 * @param params
	 * @param encoding
	 * @return
	 * @Description GET提交，指定字符编码
	 */
	public static String sendGet(String url, Map<String, Object> params, String encoding) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet get = new HttpGet(url);
		if (params != null) {
			// 处理参数
			HttpEntity entity = handleParam(params, encoding);
			try {
				String paramStr = EntityUtils.toString(entity);
				get = new HttpGet(url + "?" + paramStr);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		CloseableHttpResponse response = null;
		String content = null;
		try {
			response = httpClient.execute(get);
			content = EntityUtils.toString(response.getEntity(), encoding);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(response, httpClient);
		}
		System.out.println(content);
		return content;
	}

	/**
	 * @param url
	 * @param params
	 * @return
	 * @Description GET提交，使用默认字符编码UTF-8
	 */
	public static String sendGet(String url, Map<String, Object> params) {
		return sendGet(url, params, ENCODING);
	}

	/**
	 * @param type     请求类型
	 * @param url      请求URL
	 * @param params   请求参数
	 * @param encoding 请求编码格式
	 * @return
	 * @Description 提交请求，指定字符编码
	 */
	public static String service(String type, String url, Map<String, Object> params, String encoding) {
		if (POST.equals(type)) {
			return sendPost(url, params, encoding);
		} else if (GET.equals(type)) {
			return sendGet(url, params, encoding);
		} else {
			return null;
		}
	}

	/**
	 * @param type   请求类型
	 * @param url    请求URL
	 * @param params 请求参数
	 * @return
	 * @Description 提交请求，使用默认字符编码UTF-8
	 */
	public static String service(String type, String url, Map<String, Object> params) {
		return service(type, url, params, ENCODING);
	}

	/**
	 * @param params   请求参数
	 * @param encoding 请求编码格式
	 * @return
	 * @Description 处理参数
	 */
	private static HttpEntity handleParam(Map<String, Object> params, String encoding) {
		List<NameValuePair> pList = new ArrayList<NameValuePair>();
		Set<String> keys = params.keySet();
		for (String key : keys) {
			Object value = params.get(key);
			try {
				// 处理数组
				Object[] objs = (Object[]) value;
				for (Object obj : objs) {
					pList.add(new BasicNameValuePair(key, obj.toString()));
				}
			} catch (Exception e) {
				// 处理普通类型
				pList.add(new BasicNameValuePair(key, value.toString()));
			}
		}
		UrlEncodedFormEntity uefEntity = null;
		try {
			uefEntity = new UrlEncodedFormEntity(pList, encoding);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return uefEntity;
	}

	/**
	 * @param response
	 * @param httpClient
	 * @Description 关闭
	 */
	private static void close(CloseableHttpResponse response, CloseableHttpClient httpClient) {
		try {
			if (response != null) {
				response.close();
			}
			if (httpClient != null) {
				httpClient.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 * @throws ParseException
	 * @throws IOException
	 * @Description 测试工具方法
	 */
	public static void main(String[] args) throws ParseException, IOException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ip", "127.0.0.1");
		String content = sendGet("http://ip.ws.126.net/ipquery", params);
		System.out.println(content);
	}
}
