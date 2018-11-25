package com.qm.frame.qmsecurity.connector;

import java.util.List;

import com.qm.frame.qmsecurity.entity.QmSecInfo;
import com.qm.frame.qmsecurity.entity.QmSecRole;

/**
 * Copyright © 2018浅梦工作室. All rights reserved.
 * @author: 浅梦
 * @date: 2018年11月26日 上午12:55:28
 * @Description: 
 * QM权限管理业务接口，该接口提供的方法必须全部实现。该类提供的方法在提供了鉴权注解时会全部调用。
 * 过程：
 * 		系统启动时加载getSystemRoles获取角色列表→
 * 		用户请求→
 * 		进入控制器→
 * 		检查方法体上的注解→
 * 		调用 verifyInfo验证Token→
 * 		读取缓存中的角色列表→
 * 		与注解上的powerName进行权限鉴定→允许访问
 */
public interface QmSecurityService {
	
	/**
	 * QmSecurity验证Token的具体实现。
	 * 注意：实现该方法最好使用缓存技术，提高访问速度。
	 * 包括用户登录时存放的所有信息
	 * @param qmSecInfo 该方法会回调一个QmSecInfo的对象
	 * @return
	 */
	boolean verifyInfo(QmSecInfo qmSecInfo);

	/**
	 * QmSecurity调用此方法来获取系统角色表信息,系统会在启动时调用加载.
	 * @Title: getQmSecRoles
	 * @return List<QmSecRole> 返回角色实体，该实体必须包含所属权限
	 */
	List<QmSecRole> getQmSecRoles();
}