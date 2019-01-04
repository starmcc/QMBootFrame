package com.qm.frame.basic.mvc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Copyright © 2019浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2019/1/4 15:18
 * @Description TODO
 */
@Target(ElementType.PARAMETER)//使用在参数上
@Retention(RetentionPolicy.RUNTIME)//运行时注解
public @interface QmMvc {
}
