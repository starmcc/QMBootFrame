package com.qm.frame.qmsecurity.entity;

import java.util.List;

/**
 * Copyright © 2018浅梦工作室. All rights reserved.
 * @author: 浅梦
 * @date: 2018年11月25日 上午1:57:19
 * @Description: QmSecurity角色实体类
 */
public final class QmSecRole {
	/**
	 * 角色id
	 */
	private int roleId;

	private Object roleInfo;
	/**
	 * 角色权限列表
	 */
	private List<String> rolePowers;


	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public Object getRoleInfo() {
		return roleInfo;
	}

	public void setRoleInfo(Object roleInfo) {
		this.roleInfo = roleInfo;
	}

	public List<String> getRolePowers() {
		return rolePowers;
	}

	public void setRolePowers(List<String> rolePowers) {
		this.rolePowers = rolePowers;
	}
	
	
	
}
