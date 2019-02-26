package com.qm.frame.basic.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import com.qm.frame.basic.config.QmFrameContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.qm.frame.basic.controller.QmCode;
import com.qm.frame.basic.controller.QmController;

/**
 * Copyright © 2018浅梦工作室. All rights reserved.
 * @author 浅梦
 * @date 2018年11月24日 上午1:15:27
 * @Description 该过滤器主要实现版本控制、重写RequestBody、实现AES对称无痕解密
 */
@WebFilter(urlPatterns="/*",filterName="InitFilter")
@Order(1)
public @Component class InitFilter extends QmController implements Filter{
    /**
     * Logger slf4j
     */
    private static final Logger LOG = LoggerFactory.getLogger(InitFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        HttpServletRequest req = (HttpServletRequest) request;
        //版本控制
        boolean is = verifyVersion(req);
        if (!is) {
            response.getWriter().write(super.sendJSON(QmCode._102));
            return;
        }
        /**
         * 重写RequestBody,并对body进行对称AES解密。
         */
        ServletRequest requestWrapper = new QmRequestWrapper(req);
        chain.doFilter(requestWrapper, response);
    }


    /**
     * @param request
     * @return
     * @throws IOException
     * @Title verify
     * @Description 版本验证工具
     */
    public boolean verifyVersion(HttpServletRequest request) throws IOException {
        //不开启版本控制
        if (!QmFrameContent.VERSION_START) return true;
        //目前版本号
        String versionRequest = request.getHeader("version");
        LOG.debug("请求版本号：" + versionRequest);
        LOG.debug("当前版本号：" + QmFrameContent.VERSION_NOW);
        if (QmFrameContent.VERSION_NOW.equals(versionRequest)) {
            //通过
            return true;
        }
        LOG.debug("进入版本控制判断");
        if (QmFrameContent.VERSION_ALLOWS != null && QmFrameContent.VERSION_ALLOWS.size() > 0) {
            for (String version : QmFrameContent.VERSION_ALLOWS) {
                if (version.equals(versionRequest)) {
                    //通过
                    return true;
                }
            }
        } else {
            LOG.debug("请求失败,服务器并无配置可允许版本");
            return false;
        }
        LOG.debug("错误失败返回");
        return false;
    }

}