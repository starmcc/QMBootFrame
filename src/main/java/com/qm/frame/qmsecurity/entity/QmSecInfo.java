package com.qm.frame.qmsecurity.entity;

import java.util.Map;

/**
 * Copyright © 2018浅梦工作室. All rights reserved.
 * @author: 浅梦
 * @date: 2018年11月26日 上午1:06:41
 * @Description: QmSercurity用户实体类
 */
public final class QmSecInfo {
	/**
	 * 用户识别(唯一校验)
	 */
	private String loginCode;
	/**
	 * 角色名
	 */
	private String roleName;
	/**
	 * 附加信息
	 */
	private Map<String,String> infoMap;
	/**
	 * 生成Token
	 */
	private String token;
	

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getLoginCode() {
		return loginCode;
	}

	public void setLoginCode(String loginCode) {
		this.loginCode = loginCode;
	}

	public Map<String, String> getInfoMap() {
		return infoMap;
	}

	public void setInfoMap(Map<String, String> infoMap) {
		this.infoMap = infoMap;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	
}
