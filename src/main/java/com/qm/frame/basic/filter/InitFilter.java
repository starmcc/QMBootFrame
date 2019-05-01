package com.qm.frame.basic.filter;

import com.qm.frame.basic.config.QmFrameContent;
import com.qm.frame.basic.controller.QmCode;
import com.qm.frame.basic.controller.QmController;
import com.qm.frame.basic.util.QmSpringManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Copyright © 2018浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2018年11月24日 上午1:15:27
 * @Description 该过滤器主要实现版本控制、重写RequestBody、实现AES对称无痕解密
 */
@Order(1)
@Component
@WebFilter(urlPatterns = "/*", filterName = "InitFilter")
public class InitFilter extends QmController implements Filter {
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
        LOG.info("/n" + "请求URI：" + req.getRequestURI());
        //特殊请求
        if (verifySpecialURI(req)) {
            chain.doFilter(request, response);
            return;
        }
        //版本控制
        if (!verifyVersion(req)) {
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
     * 验证是否为特殊请求
     *
     * @param request
     * @return
     */
    private boolean verifySpecialURI(HttpServletRequest request) {
        for (String uri : QmFrameContent.REQUEST_SPECIAL_URI) {
            if (QmSpringManager.verifyMatchURI(uri, request.getRequestURI())) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param request
     * @return
     * @throws IOException
     * @Title verify
     * @Description 版本验证工具
     */
    private boolean verifyVersion(HttpServletRequest request) throws IOException {
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