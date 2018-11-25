package com.qm.code.sercurity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.qm.frame.qmsecurity.connector.QmSecurityService;
import com.qm.frame.qmsecurity.entity.QmSecInfo;
import com.qm.frame.qmsecurity.entity.QmSecRole;

/**
 * Copyright © 2018浅梦工作室. All rights reserved.
 * @author: 浅梦
 * @date: 2018年11月26日 上午1:31:48
 * @Description: 实现权限框架的QmSecurityService
 * 使用权限框架必须实现该类，否则启动报错。
 * 该类提供的两个接口在系统启动时调用。实现缓存鉴权的过程。
 */
@Service
public class SecurityOnline implements QmSecurityService{

	@Override
	public boolean verifyInfo(QmSecInfo qmSecInfo) {
		//该方法在Security中校验Token时会调用一次,返回true则表示通过这个方法
		return true;
	}

	
	//// 系统启动缓存时调用该方法
	@Override
	public List<QmSecRole> getQmSecRoles() {
		//创建一个角色集合
		List<QmSecRole> roles = new ArrayList<>();
		//模拟数据库中有一个叫做admin的角色，该角色有一个demoPower的权限。
		QmSecRole qmSecRole = new QmSecRole();
		qmSecRole.setId(1);
		qmSecRole.setRoleName("admin");
		List<String> powers = new ArrayList<>();
		powers.add("demoPower");
		qmSecRole.setRolePowers(powers);
		//添加到角色集合
		roles.add(qmSecRole);
		
		return roles;
	}

}
