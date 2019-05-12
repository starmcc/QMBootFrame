package com.qm.frame.qmsecurity.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * Copyright © 2019浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2019/5/12 1:12
 * @Description 跨域工具类
 */
public class OptionsUtils {

    /**
     * 是否为跨域探路请求
     *
     * @param request
     * @return
     */
    public static boolean isOptions(HttpServletRequest request) {
        if (request.getMethod().toUpperCase().equalsIgnoreCase("OPTIONS")) {
            return true;
        }
        return false;
    }
}
