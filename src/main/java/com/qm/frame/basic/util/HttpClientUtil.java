package com.qm.frame.basic.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
 * @author: 浅梦
 * @date: 2018年11月24日 上午2:03:20
 * @Description: Http封装工具类
 */
public class HttpClientUtil {

	//提交方式
	private final static String POST = "post";
	private final static String GET = "get";
	/**
	 * 默认字符编码
	 */
	private final static String ENCODING = "UTF-8";

	/**
	 * @Title: sendPost
	 * @param url
	 * @param params
	 * @param encoding
	 * @return
	 * @Description: POST提交，指定字符编码
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
	 * @Title: sendPost
	 * @param url
	 * @param params
	 * @return
	 * @Description: POST提交，使用默认字符编码UTF-8
	 */
	public static String sendPost(String url, Map<String, Object> params) {
		return sendPost(url, params, ENCODING);
	}

	/**
	 * @Title: sendGet
	 * @param url
	 * @param params
	 * @param encoding
	 * @return
	 * @Description: GET提交，指定字符编码
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
	 * @Title: sendGet
	 * @param url
	 * @param params
	 * @return
	 * @Description: GET提交，使用默认字符编码UTF-8
	 */
	public static String sendGet(String url, Map<String, Object> params) {
		return sendGet(url, params, ENCODING);
	}

	/**
	 * @Title: service
	 * @param type 请求类型
	 * @param url 请求URL
	 * @param params 请求参数
	 * @param encoding 请求编码格式
	 * @return
	 * @Description: 提交请求，指定字符编码
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
	 * @Title: service
	 * @param type 请求类型
	 * @param url 请求URL
	 * @param params 请求参数
	 * @return
	 * @Description: 提交请求，使用默认字符编码UTF-8
	 */
	public static String service(String type, String url, Map<String, Object> params) {
		return service(type, url, params, ENCODING);
	}

	/**
	 * @Title: handleParam
	 * @param params 请求参数
	 * @param encoding 请求编码格式
	 * @return
	 * @Description: 处理参数
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
	 * @Title: close
	 * @param response
	 * @param httpClient
	 * @Description: 关闭
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
	 * @Title: main
	 * @param args
	 * @throws ParseException
	 * @throws IOException
	 * @Description: 测试工具方法
	 */
	public static void main(String[] args) throws ParseException, IOException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ip", "127.0.0.1");
		String content = HttpClientUtil.sendGet("http://ip.ws.126.net/ipquery", params);
		System.out.println(content);
	}
}
