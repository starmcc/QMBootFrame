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
	private int id;
	/**
	 * 角色名
	 */
	private String roleName;
	/**
	 * 角色权限列表
	 */
	private List<String> rolePowers;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public List<String> getRolePowers() {
		return rolePowers;
	}

	public void setRolePowers(List<String> rolePowers) {
		this.rolePowers = rolePowers;
	}
	
	
	
}
