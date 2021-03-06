package com.qm.frame.basic.aop;

import com.qm.frame.basic.config.QmFrameConstants;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Copyright © 2018浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2018年11月24日 上午1:34:28
 * @Description 接口日志，返回请求时间，参数，返回值等信息。
 */
@Aspect
@Component
public class LoggerWholeAop {

    private static final Logger LOG = LoggerFactory.getLogger(LoggerWholeAop.class);

    private static final QmOutMethod QM_OUT_METHOD = getQmOutMethod();

    /**
     * 获取QmResponseOut 
     *
     * @return
     */
    private final static QmOutMethod getQmOutMethod() {
        try {
            if (StringUtils.isEmpty(QmFrameConstants.LOGGER_AOP_EXTEND_CLASS) == false) {
                return null;
            }
            return (QmOutMethod) Class.forName(QmFrameConstants.LOGGER_AOP_EXTEND_CLASS).newInstance();
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 记录请求时间
     */
    private Long starTime;

    /**
     * 切入点范围 execution(* com.qm..*.controller..*.*(..))
     * execution(* *..controller..*.*(..))
     * this(com.qm.frame.basic.controller.QmController)
     */
    @Pointcut("this(com.qm.frame.basic.controller.QmController) && execution(* *..controller..*.*(..))")
    public void qmPointcut() {
    }

    /**
     * 环绕增强(最强版)
     *
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("qmPointcut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        return pjp.proceed();
    }

    /**
     * 前置增强
     *
     * @param jp
     */
    @Before("qmPointcut()")
    public void before(JoinPoint jp) {
        starTime = System.currentTimeMillis();
        // getTarget得到被代理的目标对象(要切入的目标对象)
        LOG.info("※※※※※※※※※※※※※※※※※※");
        LOG.info("执行位置:" + jp.getTarget().getClass().getName());
        // getSignature得到被代理的目标对象的方法名(返回被切入的目标方法名)
        LOG.info("执行方法:[" + jp.getSignature().getName() + "]");
        // Arrays.toString(jp.getArgs())获得目标方法的参数列表
        LOG.debug("参数列表:" + Arrays.toString(jp.getArgs()));
        LOG.info("※※※※※※※※※※※※※※※※※※");
    }

    /**
     * 后置增强
     *
     * @param jp
     * @param result 方法返回的结果
     */
    @AfterReturning(pointcut = "qmPointcut()", returning = "result")
    public void afterReturning(JoinPoint jp, Object result) {
        LOG.debug("※※※执行结果:[" + result + "]※※※");
        Long endTime = System.currentTimeMillis();
        Long time = endTime - starTime;
        LOG.info("※※※执行耗时：" + time + "/ms※※※");
        if (QM_OUT_METHOD != null) {
            QM_OUT_METHOD.responseOutHandling(jp, result, time);
        }
    }

}
