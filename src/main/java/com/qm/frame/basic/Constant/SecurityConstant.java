package com.qm.frame.basic.Constant;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Copyright © 2018浅梦工作室. All rights reserved.
 * @author: 浅梦
 * @date: 2018年11月24日 上午1:40:48
 * @Description: QmSecurity全局信息配置类
 */
@ConfigurationProperties(prefix = "qmframe.security")
public @Component class SecurityConstant {

	private boolean start;
	private String secret;
	private String requestHeaderTokenKey;

	public boolean isStart() {
		return start;
	}

	public void setStart(boolean start) {
		this.start = start;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getRequestHeaderTokenKey() {
		return requestHeaderTokenKey;
	}

	public void setRequestHeaderTokenKey(String requestHeaderTokenKey) {
		this.requestHeaderTokenKey = requestHeaderTokenKey;
	}

}
