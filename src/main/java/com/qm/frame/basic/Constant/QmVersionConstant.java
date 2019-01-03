package com.qm.frame.basic.Constant;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Copyright © 2018浅梦工作室. All rights reserved.
 * @author 浅梦
 * @date 2018年11月24日 上午1:39:43
 * @Description 版本全局信息配置类
 */
@ConfigurationProperties(prefix = "qmframe.version")
public @Component class QmVersionConstant {
	private boolean start;

	private String version;
	private List<String> permitVersions;

	public boolean isStart() {
		return start;
	}

	public void setStart(boolean start) {
		this.start = start;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public List<String> getPermitVersions() {
		return permitVersions;
	}

	public void setPermitVersions(List<String> permitVersions) {
		this.permitVersions = permitVersions;
	}

}
