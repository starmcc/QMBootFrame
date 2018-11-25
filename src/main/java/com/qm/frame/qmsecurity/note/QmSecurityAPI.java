package com.qm.frame.qmsecurity.note;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 浅梦工作室
 * @createDate 2018年11月21日03:20:12
 * @Description QM权限注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface QmSecurityAPI {
	/**
	 * 权限名
	 * @return
	 */
	public String powerName() default "QmSecurity-ordinary";
}