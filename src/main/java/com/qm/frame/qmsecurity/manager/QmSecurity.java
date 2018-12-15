package com.qm.frame.qmsecurity.manager;

import com.qm.frame.qmsecurity.entity.QmSecInfo;
import com.qm.frame.qmsecurity.entity.QmSecRole;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Copyright © 2018浅梦工作室. All rights reserved.
 * @author: 浅梦
 * @date: 2018年11月26日 上午1:05:33
 * @Description: QmSecurity实例
 */
public interface QmSecurity {
	/**
	 * 根据提供的信息创建一个Token
	 * @param qmSecInfo 提供一个qmSecInfo用户信息对象
	 * @param timeType 1~7 分别对应 1年/2月/3日/4星期/5时/6分/7秒，默认分钟
	 * @param time     时间，根据type定义类型。
	 * @return Token
	 */
	QmSecInfo login(QmSecInfo qmSecInfo, int timeType, int time);

	/**
	 * 获取QmSecInfo中的信息(校验)
	 * @param  request httpServletRequest
	 * @return QmSecInfo 用户信息
	 */
	QmSecInfo getQmSecInfo(HttpServletRequest request);
	/**
	 * 在缓存中获取对应的角色信息，
	 * 如果缓存没有直接从抽象出的QmSecurityManager的getQmSecRole方法进行获取。
	 * 注意：如果isNewInfo为true时，无论缓存中是否存在该角色信息，都会直接更新缓存信息。
	 * @param roleId 角色id
	 * @param isNewInfo 查找时是否更新缓存信息
	 * @return QmSecRole
	 */
	QmSecRole extractQmSecRole(int roleId,boolean isNewInfo);

	/**
	 * 获取所有缓存中的角色信息集合
	 * @return
	 */
	List<QmSecRole> extractAllQmSecRole();

	/**
	 * 校验该用户对象是否合法
	 * 该方法既调用token校验也调用QmSecurityManager抽象的verifyInfo方法
	 * @param qmSecInfo 校验对象
	 * @return
	 */
	boolean verifyInfo(QmSecInfo qmSecInfo);

}
