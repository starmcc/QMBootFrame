package com.qm.frame.mybatis.note;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Copyright © 2019浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2019/1/9 11:38
 * @Description 字段标识
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface QmParams {

    /**
     * 字段别名
     *
     * @return
     */
    String name() default "";

    /**
     * 是否排除该字段
     *
     * @return
     */
    boolean except() default false;
}
