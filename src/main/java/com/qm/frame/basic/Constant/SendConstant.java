package com.qm.frame.basic.Constant;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * Copyright © 2018浅梦工作室. All rights reserved.
 * @author: 浅梦
 * @date: 2018年11月24日 上午1:41:17
 * @Description: 数据传递全局信息配置类
 */
@ConfigurationProperties(prefix = "qmframe.send")
public @Component class SendConstant {

	private String requestDataKey;

	private String responseDataKey;

	public String getRequestDataKey() {
		return requestDataKey;
	}

	public void setRequestDataKey(String requestDataKey) {
		this.requestDataKey = requestDataKey;
	}

	public String getResponseDataKey() {
		return responseDataKey;
	}

	public void setResponseDataKey(String responseDataKey) {
		this.responseDataKey = responseDataKey;
	}

}
