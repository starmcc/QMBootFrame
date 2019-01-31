package com.qm.frame.basic.aop;

import org.aspectj.lang.JoinPoint;

/**
 * Copyright © 2019浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2019/1/7 0:57
 * @Description 请求方法执行完毕回调接口
 */
public interface QmResponseOut {

    /**
     * 当请求方法执行完毕时执行该方法
     * @param jp JoinPoint 切入点对象
     * @param result 返回结果
     * @param responseTime 响应时间
     */
    void responseOutHandling(JoinPoint jp, Object result, Long responseTime);

}
