package com.qm.frame.basic.Constant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qm.frame.basic.util.QmSpringManager;

/**
 * Copyright © 2018浅梦工作室. All rights reserved.
 * @author 浅梦
 * @date 2018年11月24日 上午1:36:20
 * @Description 全局Bean依赖配置类 - 该类提供框架中所定义的自定义属性配置。
 * @Description 获取该类可使用注入方式也可以调用静态方法getQmConstantByContext获取该实例。
 */
public @Component class QmConstant {
	@Autowired
	private SendConstant sendConstant;
	@Autowired
	private QmVersionConstant qmVersionConstant;
	@Autowired
	private DruidConstant druidConstant;
	@Autowired
	private AesConstant aesConstant;
	
	public static QmConstant getQmConstantByContext() {
		return QmSpringManager.getBean(QmConstant.class);
	}

	public SendConstant getSendConstant() {
		return sendConstant;
	}

	public void setSendConstant(SendConstant sendConstant) {
		this.sendConstant = sendConstant;
	}

	public QmVersionConstant getQmVersionConstant() {
		return qmVersionConstant;
	}

	public void setQmVersionConstant(QmVersionConstant qmVersionConstant) {
		this.qmVersionConstant = qmVersionConstant;
	}

	public DruidConstant getDruidConstant() {
		return druidConstant;
	}

	public void setDruidConstant(DruidConstant druidConstant) {
		this.druidConstant = druidConstant;
	}

	public AesConstant getAesConstant() {
		return aesConstant;
	}

	public void setAesConstant(AesConstant aesConstant) {
		this.aesConstant = aesConstant;
	}

}
