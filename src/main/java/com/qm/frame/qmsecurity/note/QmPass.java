package com.qm.frame.qmsecurity.note;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Copyright © 2018浅梦工作室}. All rights reserved.
 *
 * @author 浅梦
 * @date 2018/12/22 21:55
 * @Description 忽略安全认证、授权的注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface QmPass {
    /**
     * 是否需要登录(标注了该注解且该字段为true时,QmSecurity只做登录校验)
     * 默认为false，则忽略所有安全认证。
     * @return
     */
    boolean needLogin() default false;
}
