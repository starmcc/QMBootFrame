package com.qm.frame.wechat.pay;

import com.qm.frame.wechat.WechatConfig;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.*;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * 微信支付相关服务封装类
 * @author 浅梦
 */
public final class QmWechatPayBasis {
	/**
	 * 封闭构造,禁止创建该对象
	 */
	private QmWechatPayBasis() { }

	/**
	 *
	 * @param Encoding
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	/**
	 * 微信签名换算
	 * @param Encoding 编码格式
	 * @param qmWechatPayInfo 微信支付参数信息
	 * @return
	 */
	public static String createSign(String Encoding,QmWechatPayInfo qmWechatPayInfo) {
		try {
			SortedMap<String, Object> params = new TreeMap<String, Object>();
			Field[] fields = qmWechatPayInfo.getClass().getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				Object value = field.get(qmWechatPayInfo);
				String key = field.getName();
				if (value != null && !value.equals("")) {
					params.put(key, value);
				}
			}
			params.put("appid", WechatConfig.APPID);
			params.put("mch_id",WechatConfig.MCH_ID);
			params.put("notify_url",WechatConfig.NOTIFY_URL);
			StringBuffer sb = new StringBuffer();
			Set<Map.Entry<String, Object>> entrySet = params.entrySet();
			Iterator<Map.Entry<String, Object>> it = entrySet.iterator();
			while (it.hasNext()) {
				Map.Entry<String, Object> entry = it.next();
				String k = (String) entry.getKey();
				Object v = entry.getValue();
				sb.append(k + "=" + v + "&");
			}
			sb.append("key=" + WechatConfig.MCH_KEY);
			System.out.println("参与签名的参数：" + sb.toString());
			return MD5Util.MD5Encode(sb.toString(), Encoding).toUpperCase();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * 请求方法
	 * @param requestUrl
	 * @param requestMethod
	 * @param outputStr
	 * @return
	 * @throws IOException
	 */
	public static String httpsRequest(String requestUrl, String requestMethod, String outputStr) throws IOException {
		URL url = new URL(requestUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setUseCaches(false);
		conn.setRequestMethod(requestMethod);
		conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
		if (null != outputStr) {
			OutputStream outputStream = conn.getOutputStream();
			outputStream.write(outputStr.getBytes("UTF-8"));
			outputStream.close();
		}
		InputStream inputStream = conn.getInputStream();
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		String str = null;
		StringBuffer buffer = new StringBuffer();
		while ((str = bufferedReader.readLine()) != null) {
			buffer.append(str);
		}
		bufferedReader.close();
		inputStreamReader.close();
		inputStream.close();
		inputStream = null;
		conn.disconnect();
		return buffer.toString();
	}


	/**
	 * 将微信信息类转换成转XML报文
	 * @param qmWechatPayInfo
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static String getRequestXml(QmWechatPayInfo qmWechatPayInfo,String sign)
			throws IllegalArgumentException, IllegalAccessException {
		StringBuffer xmlStrBuf = new StringBuffer();
		xmlStrBuf.append("<xml>");
		// 填入appid和mch_id
		xmlStrBuf.append("<appid>" + WechatConfig.APPID + "</appid>");
		xmlStrBuf.append("<mch_id>" + WechatConfig.MCH_ID + "</mch_id>");
		xmlStrBuf.append("<notify_url>" + WechatConfig.NOTIFY_URL + "</notify_url>");
		Field[] fields = qmWechatPayInfo.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			Object value = field.get(qmWechatPayInfo);
			String key = field.getName();
			if (value != null) {
				if (key.equalsIgnoreCase("attach")) {
					xmlStrBuf.append("<" + key + ">" + "<![CDATA[" + value + "]]></" + key + ">");
				} else {
					xmlStrBuf.append("<" + key + ">" + value + "</" + key + ">");
				}
			}
		}
		xmlStrBuf.append("<sign>" + sign + "</sign>");
		xmlStrBuf.append("</xml>");
		return xmlStrBuf.toString();
	}

	/**
	 * 解析微信返回的XML报文
	 * @param strxml
	 * @return
	 * @throws IOException
	 * @throws JDOMException
	 */
	public static Map<String, String> doXMLParse(String strxml) throws IOException, JDOMException {
		strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");
		if (null == strxml || "".equals(strxml)) {
			return null;
		}
		Map<String, String> resMap = new HashMap<String, String>();
		InputStream in = new ByteArrayInputStream(strxml.getBytes("UTF-8"));
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(in);
		Element root = doc.getRootElement();
		List<?> list = root.getChildren();
		Iterator<?> it = list.iterator();
		while (it.hasNext()) {
			Element e = (Element) it.next();
			String k = e.getName();
			String v = "";
			List<?> children = e.getChildren();
			if (children.isEmpty()) {
				v = e.getTextNormalize();
			} else {
				v = getChildrenText(children);
			}
			System.out.println(k + "-" + v);
			resMap.put(k, v);
		}
		in.close();
		return resMap;
	}

	/**
	 * 解析报文依赖
	 * 
	 * @param children
	 * @return
	 */
	public static String getChildrenText(List<?> children) {
		StringBuffer sb = new StringBuffer();
		if (!children.isEmpty()) {
			Iterator<?> it = children.iterator();
			while (it.hasNext()) {
				Element e = (Element) it.next();
				String name = e.getName();
				String value = e.getTextNormalize();
				List<?> list = e.getChildren();
				sb.append("<" + name + ">");
				if (!list.isEmpty()) {
					sb.append(getChildrenText(list));
				}
				sb.append(value);
				sb.append("</" + name + ">");
			}
		}
		return sb.toString();
	}
}