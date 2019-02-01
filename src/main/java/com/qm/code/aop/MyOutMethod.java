package com.qm.code.aop;

import com.qm.frame.basic.aop.QmOutMethod;
import org.aspectj.lang.JoinPoint;

/**
 * Copyright © 2019浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2019/2/2 4:53
 * @Description 实现AOP切入接口
 */
public class MyOutMethod implements QmOutMethod {

    @Override
    public void responseOutHandling(JoinPoint jp, Object result, Long responseTime) {

    }
}
