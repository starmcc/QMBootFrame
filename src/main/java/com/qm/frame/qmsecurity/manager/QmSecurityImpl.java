package com.qm.frame.qmsecurity.manager;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.algorithms.Algorithm;
import com.qm.frame.basic.Constant.QmConstant;
import com.qm.frame.basic.util.HttpApiUtil;
import com.qm.frame.qmsecurity.config.QmSecurityParam;
import com.qm.frame.qmsecurity.entity.QmSecInfo;
import com.qm.frame.qmsecurity.entity.QmSecRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Copyright © 2018浅梦工作室. All rights reserved.
 * @author: 浅梦
 * @date: 2018年11月26日 上午1:05:47
 * @Description: QmSecurity实例的实现类
 */
@SuppressWarnings("unchecked")
@Service
public final class QmSecurityImpl implements QmSecurity {

	@Autowired(required=false)
	private QmSecurityManager qmSecurityManager;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private QmConstant qmConstant;
	@Autowired
	private ServletContext servletContext;

	@Override
	public QmSecInfo login(QmSecInfo qmSecInfo, int timeType, int time){
		Map<String, String> infoMap = qmSecInfo.getInfoMap();
		String loginCode = qmSecInfo.getLoginCode();
		int roleId = 0;
		try {
			roleId = qmSecInfo.getRoleId();
		} catch (Exception e) {
			System.out.println("login空指针异常roleId");
			return null;
		}
		boolean superUser = qmSecInfo.isSuperUser();
		if (StringUtils.isEmpty(loginCode)
				|| timeType < 1
				|| timeType > 6) {
			System.out.println("login接收参数有误！");
			return null;
		}
		// header
		Builder builder = JWT.create();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("alg", "HS256");
		map.put("typ", "JWT");
		builder.withHeader(map);
		// 将数据封装到token中
		builder.withClaim("loginCode", loginCode);
		builder.withClaim("roleId", roleId);
		builder.withClaim("superUser",superUser);
		for (String key : infoMap.keySet()) {
			builder.withClaim(key, infoMap.get(key));
		}
		// 封装ip
		String ip = HttpApiUtil.getHttpIp(request);
		builder.withClaim("ip", ip);
		// 设置过期时间
		Calendar nowTime = Calendar.getInstance();
		int calendarType;
		switch (timeType) {
		case 1:
			calendarType = Calendar.YEAR;
			break;
		case 2:
			calendarType = Calendar.MONTH;
			break;
		case 3:
			calendarType = Calendar.DATE;
			break;
		case 4:
			calendarType = Calendar.DAY_OF_WEEK;
			break;
		case 5:
			calendarType = Calendar.HOUR;
			break;
		case 6:
			calendarType = Calendar.MINUTE;
			break;
		case 7:
			calendarType = Calendar.SECOND;
		default:
			calendarType = Calendar.MINUTE;
			break;
		}
		nowTime.add(calendarType, time);
		Date expiresDate = nowTime.getTime();
		builder.withExpiresAt(expiresDate);
		// 签发时间
		Date istDate = new Date();
		builder.withIssuedAt(istDate);
		String token = null;
		try {
			token = builder.sign(Algorithm.HMAC256(qmConstant.getSecurityConstant().getSecret()));
		} catch (Exception e) {
			System.out.println("签名错误");
			return null;
		}
		qmSecInfo.setToken(token);
		return qmSecInfo;
	}

	@Override
	public QmSecInfo getQmSecInfo(HttpServletRequest request) {
		QmSecInfo qmSecInfo = (QmSecInfo) request.getAttribute(QmSecurityParam.CONTEXT_QMSECINFO);
		return qmSecInfo;
	}

	/**
	 * 获取系统缓存中的角色信息集合
	 * @return 返回角色列表
	 */
	@Override
	public List<QmSecRole> extractAllQmSecRole() {
		List<QmSecRole> roles = null;
		try {
			roles = (List<QmSecRole>) servletContext.getAttribute(QmSecurityParam.CONTEXT_QMSECROLES);
			if (roles == null) {
				roles = new ArrayList<>();
			}
		} catch (Exception e) {
			roles = new ArrayList<>();
		}
		return roles;
	}


	/**
	 * 将角色列表保存到application中。
	 */
	public void setQmSecRoles(List<QmSecRole> qmSecRoleList) {
		servletContext.setAttribute(QmSecurityParam.CONTEXT_QMSECROLES, qmSecRoleList);
	}


	@Override
	public QmSecRole extractQmSecRole(int roleId,boolean isNewInfo) {
		List<QmSecRole> qmSecRoles = this.extractAllQmSecRole();
		for (int i = 0; i < qmSecRoles.size(); i++) {
			// 遍历该角色的id是否一致
			if (roleId == qmSecRoles.get(i).getRoleId()) {
				if (isNewInfo) {
					// 如果isNewInfo=true，则直接调用使用者提供方法并更新缓存。
					try {
						QmSecRole qmSecRole = qmSecurityManager.getQmSecRole(roleId);
						if (qmSecRole != null) {
							qmSecRoles.set(i,qmSecRole);
							// 该处设置回去最新的角色集合
							this.setQmSecRoles(qmSecRoles);
						}
					} catch (Exception e) {
						new Exception("继承QmSecurityManager的getQmSecRole方法发生了异常").printStackTrace();
						return null;
					}
				}
				return qmSecRoles.get(i);
			}
		}
		try {
			QmSecRole qmSecRole = qmSecurityManager.getQmSecRole(roleId);
			if (qmSecRole != null) {
				qmSecRoles.add(qmSecRole);
				this.setQmSecRoles(qmSecRoles);
			}
			return qmSecRole;
		} catch (Exception e) {
			new Exception("继承QmSecurityManager的getQmSecRole方法发生了异常").printStackTrace();
			return null;
		}
	}

	@Override
	public boolean verifyInfo(QmSecInfo qmSecInfo) {
		return qmSecurityManager.verifyInfo(qmSecInfo);
	}


}