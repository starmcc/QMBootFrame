package com.qm.frame.basic.filter;


import com.alibaba.fastjson.JSONObject;
import com.qm.frame.basic.Constant.QmConstant;
import com.qm.frame.basic.util.AESUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Enumeration;

/**
 * Copyright © 2018浅梦工作室. All rights reserved.
 * @author 浅梦
 * @date 2018年11月24日 上午1:23:44
 * @Description 实现重写RequestBody,并实现AES对称无缝解密
 */
public class QmRequestWrapper extends HttpServletRequestWrapper {

	private static final Logger LOG = LoggerFactory.getLogger(QmRequestWrapper.class);

	/**
	 * body
	 */
	private final byte[] body;

	/**
	 * @Title QmRequestWrapper
	 * @param request
	 * @throws IOException
	 * @Description 主要通过构造方法实现
	 */
	public QmRequestWrapper(HttpServletRequest request) throws IOException {
		super(request);
		// logPrint(request);
		// 开始对body进行解析
		String bodyTemp = getBodyString(request);
		body =  getBodyByAes(bodyTemp).getBytes(Charset.forName("UTF-8"));
	}

	/**
	 * 打印请求日志
	 * @param request
	 */
	public void logPrint(HttpServletRequest request) {
		LOG.info("-------------------------------------------------");
		Enumeration<String> e = request.getHeaderNames();
		while (e.hasMoreElements()) {
			String name = (String) e.nextElement();
			String value = request.getHeader(name);
			LOG.info(name + " = " + value);
		}
	}

	/**
	 * @Title getBodyByDes
	 * @param body
	 * @return
	 * @Description 请求解析，解析格式为{"配置的key名":{"param":"xxx","param2":"xxx"}}的JSON参数
	 */
	private String getBodyByAes(String body){
		if (body == null || body.trim().equals("")) return body;
		JSONObject jsonObject = JSONObject.parseObject(body);
		QmConstant config = QmConstant.getQmConstantByContext();
		String json = jsonObject.getString(config.getSendConstant().getRequestDataKey());
		if (config.getAesConstant().isStart()) {
			try {
				json = AESUtil.decryptAES(json);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
    	return json;
    }

	/**
	 * @Title getBodyString
	 * @param request
	 * @return
	 * @Description 获取请求Body
	 */
    public String getBodyString(ServletRequest request) {
        StringBuilder sb = new StringBuilder();
        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            inputStream = request.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }
	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(getInputStream()));
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {

		final ByteArrayInputStream bais = new ByteArrayInputStream(body);

		return new ServletInputStream() {
			@Override
			public int read() throws IOException {
				return bais.read();
			}

			@Override
			public boolean isFinished() {
				return false;
			}

			@Override
			public boolean isReady() {
				return false;
			}

			@Override
			public void setReadListener(ReadListener listener) {

			}
		};
	}

	@Override
	public String getHeader(String name) {
		return super.getHeader(name);
	}

	@Override
	public Enumeration<String> getHeaderNames() {
		return super.getHeaderNames();
	}

	@Override
	public Enumeration<String> getHeaders(String name) {
		return super.getHeaders(name);
	}

}
