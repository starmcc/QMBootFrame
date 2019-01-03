package com.qm.frame.basic.Constant;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Copyright © 2018浅梦工作室. All rights reserved.
 * @author 浅梦
 * @date 2018年11月24日 上午1:38:55
 * @Description Druid全局信息配置类
 */
@ConfigurationProperties(prefix="qmframe.druid")
public @Component class DruidConstant {
	private String allow;
	private String deny;
	private String loginUsername;
	private String loginPassword;
	private boolean resetEnable;

	public String getAllow() {
		return allow;
	}

	public void setAllow(String allow) {
		this.allow = allow;
	}

	public String getDeny() {
		return deny;
	}

	public void setDeny(String deny) {
		this.deny = deny;
	}

	public String getLoginUsername() {
		return loginUsername;
	}

	public void setLoginUsername(String loginUsername) {
		this.loginUsername = loginUsername;
	}

	public String getLoginPassword() {
		return loginPassword;
	}

	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}

	public boolean isResetEnable() {
		return resetEnable;
	}

	public void setResetEnable(boolean resetEnable) {
		this.resetEnable = resetEnable;
	}

}
