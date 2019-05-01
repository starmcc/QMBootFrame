package com.qm.frame.basic.body;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Title QmBody
 * @date 2018年11月30日14:46:28
 * @Description: 自定义请求json数据解析注解（主要解决单参数传递）
 */
@Target(ElementType.PARAMETER)//使用在参数上
@Retention(RetentionPolicy.RUNTIME)//运行时注解
public @interface QmBody {
    /**
     * 是否必须传递的参数
     */
    boolean required() default true;

    /**
     * 解析时用到的JSON的key
     */
    String key() default "";
}
