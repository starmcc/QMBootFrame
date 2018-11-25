package com.qm.frame.qmsecurity.manager;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.qm.frame.qmsecurity.entity.QmSecInfo;
import com.qm.frame.qmsecurity.entity.QmSecRole;

/**
 * Copyright © 2018浅梦工作室. All rights reserved.
 * @author: 浅梦
 * @date: 2018年11月26日 上午1:05:33
 * @Description: QmSecurity实例
 */
public interface QmSecurityManager {
	/**
	 * 根据提供的信息创建一个Token
	 * @param qmSecInfo 提供一个qmSecInfo用户信息对象
	 * @param timeType 1~6 分别对应 年/月/日/星期/时/分/秒，默认分钟
	 * @param time     时间，根据type定义类型。
	 * @return Token
	 * @throws Exception 参数或签名错误
	 */
	QmSecInfo login(QmSecInfo qmSecInfo, int timeType, int time) throws Exception;

	/**
	 * 获取QmSecInfo中的信息(校验)
	 * @param HttpServletRequest request
	 * @return QmSecInfo 用户信息
	 */
	QmSecInfo getQmSecInfo(HttpServletRequest request);

	/**
	 * 添加系统缓存中的角色权限信息
	 * @param qmSecRole 角色实体
	 * @return boolean
	 */
	boolean addQmSecRole(QmSecRole qmSecRole);

	/**
	 * 删除系统缓存中的角色权限信息
	 * @param id 角色id
	 * @return boolean
	 */
	boolean delQmSecRole(int id);

	/**
	 * 删除系统缓存中的角色权限信息
	 * @param ids 角色id数组
	 * @return boolean
	 */
	boolean delQmSecRoles(int[] ids);

	/**
	 * 更新系统缓存中的角色和权限信息
	 * @param qmSecRole
	 * @return boolean
	 */
	boolean updateQmSecRole(QmSecRole qmSecRole);

	/**
	 * 更新系统缓存中的角色权限信息
	 * @param qmSecRoles
	 */
	void updateQmSecRoleAll(List<QmSecRole> qmSecRoles);

	/**
	 * 获取系统缓存中的角色权限信息
	 * @return List<QmSecRole>
	 */
	List<QmSecRole> getQmSecRoles();

	/**
	 * 获取系统缓存中的角色权限信息
	 * @param id 角色id
	 * @return QmSecRole
	 */
	QmSecRole getQmSecRole(int id);

}
