package com.qm.frame.basic.filter;

import com.alibaba.fastjson.JSONObject;
import com.qm.frame.basic.config.QmFrameConstants;
import com.qm.frame.basic.util.AesUtil;
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
 *
 * @author 浅梦
 * @date 2018年11月24日 上午1:23:44
 * @Description 实现重写RequestBody, 并实现AES对称无缝解密
 */
public class QmRequestWrapper extends HttpServletRequestWrapper {

    private static final Logger LOG = LoggerFactory.getLogger(QmRequestWrapper.class);
    /**
     * body
     */
    private final byte[] body;

    /**
     * @param request
     * @throws IOException
     * @Title QmRequestWrapper
     * @Description 主要通过构造方法实现
     */
    public QmRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        Enumeration<String> e = request.getHeaderNames();
        // 日志打印
        StringBuffer sbf = new StringBuffer();
        while (e.hasMoreElements()) {
            String name = (String) e.nextElement();
            String value = request.getHeader(name);
            sbf.append(name);
            sbf.append("=");
            sbf.append(value);
            sbf.append("\n");
        }
        LOG.debug(sbf.toString());
        // 日志打印结束
        String bodyTemp = getBodyString(request);
        body = getBodyByAes(bodyTemp).getBytes(Charset.forName("UTF-8"));
    }

    /**
     * @param body
     * @return
     * @Title getBodyByDes
     * @Description 请求解析，解析格式为{"配置的key名":{"param":"xxx","param2":"xxx"}}的JSON参数
     */
    private String getBodyByAes(String body) {
        if ("".equals(body.trim())) {
            return body;
        }
        JSONObject jsonObject = JSONObject.parseObject(body);
        String json = jsonObject.getString(QmFrameConstants.REQUEST_DATA_KEY);
        if (QmFrameConstants.AES_START) {
            try {
                json = AesUtil.decryptAES(json);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return json;
    }

    /**
     * @param request
     * @return
     * @Title getBodyString
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
