package com.qm.frame.mybatis.note;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Copyright © 2019浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2019/1/9 11:28
 * @Description 主键id标识
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface QmId {

    /**
     * 主键是否为UUID
     *
     * @return
     */
    boolean uuid() default false;

    /**
     * 设置该字段的别名
     *
     * @return
     */
    String name() default "";

}
