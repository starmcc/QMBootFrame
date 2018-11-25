package com.qm.frame.qmsecurity.manager;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.algorithms.Algorithm;
import com.qm.frame.basic.Constant.QmConstant;
import com.qm.frame.basic.util.HttpApiUtil;
import com.qm.frame.qmsecurity.config.QmSecurityParam;
import com.qm.frame.qmsecurity.entity.QmSecInfo;
import com.qm.frame.qmsecurity.entity.QmSecRole;

/**
 * Copyright © 2018浅梦工作室. All rights reserved.
 * @author: 浅梦
 * @date: 2018年11月26日 上午1:05:47
 * @Description: QmSecurity实例的实现类
 */
@SuppressWarnings("unchecked")
@Service
public final class QmSecurityManagerImpl implements QmSecurityManager {

	@Autowired
	private HttpServletRequest request;
	@Autowired
	private QmConstant qmConstant;
	@Autowired
	private ServletContext servletContext;

	@Override
	public QmSecInfo login(QmSecInfo qmSecInfo, int timeType, int time) throws Exception {
		Map<String, String> infoMap = qmSecInfo.getInfoMap();
		String loginCode = qmSecInfo.getLoginCode();
		String roleName = qmSecInfo.getRoleName();
		if (infoMap == null || infoMap.size() == 0 || loginCode == null || loginCode.trim().equals("") || timeType < 1
				|| timeType > 6) {
			throw new Exception("接收参数有误！");
		}
		// header
		Builder builder = JWT.create();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("alg", "HS256");
		map.put("typ", "JWT");
		builder.withHeader(map);
		// 将数据封装到token中
		builder.withClaim("loginCode", loginCode);
		builder.withClaim("roleName", roleName);
		for (String key : infoMap.keySet()) {
			builder.withClaim(key, infoMap.get(key));
		}
		// 封装ip
		String ip = HttpApiUtil.getHttpIp(request);
		builder.withClaim("ip", ip);
		// 设置过期时间
		Calendar nowTime = Calendar.getInstance();
		int calendarType = 10;
		switch (timeType) {
		case 1:
			calendarType = 1;
			break;
		case 2:
			calendarType = 2;
			break;
		case 3:
			calendarType = 5;
			break;
		case 4:
			calendarType = 7;
			break;
		case 5:
			calendarType = 10;
			break;
		case 6:
			calendarType = 12;
			break;
		default:
			calendarType = 10;
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
			throw new Exception("签名错误");
		}
		qmSecInfo.setToken(token);
		return qmSecInfo;
	}

	@Override
	public QmSecInfo getQmSecInfo(HttpServletRequest request) {
		QmSecInfo qmSecInfo = (QmSecInfo) request.getAttribute(QmSecurityParam.CONTEXT_QMSECINFO);
		return qmSecInfo;
	}

	@Override
	public boolean addQmSecRole(QmSecRole qmSecRole) {
		try {
			List<QmSecRole> qmSecRoles = this.getQmSecRoles();
			for (int i = 0; i < qmSecRoles.size(); i++) {
				if (qmSecRole.getId() == qmSecRoles.get(i).getId()) {
					return false;
				}
			}
			qmSecRoles.add(qmSecRole);
			servletContext.setAttribute(QmSecurityParam.CONTEXT_QMSECROLES, qmSecRoles);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean delQmSecRole(int id) {
		try {
			List<QmSecRole> qmSecRoles = this.getQmSecRoles();
			for (int i = 0; i < qmSecRoles.size(); i++) {
				if (id == qmSecRoles.get(i).getId()) {
					qmSecRoles.remove(i);
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean delQmSecRoles(int[] ids) {
		try {
			List<QmSecRole> qmSecRoles = this.getQmSecRoles();
			for (int i = 0; i < qmSecRoles.size(); i++) {
				for (int j = 0; j < ids.length; j++) {
					if (ids[j] == qmSecRoles.get(i).getId()) {
						qmSecRoles.remove(i);
						return true;
					}
				}

			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean updateQmSecRole(QmSecRole qmSecRole) {
		try {
			List<QmSecRole> qmSecRoles = this.getQmSecRoles();
			for (int i = 0; i < qmSecRoles.size(); i++) {
				if (qmSecRoles.get(i).getId() == qmSecRole.getId()) {
					qmSecRoles.set(i, qmSecRole);
					break;
				}
			}
			servletContext.setAttribute(QmSecurityParam.CONTEXT_QMSECROLES, qmSecRoles);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void updateQmSecRoleAll(List<QmSecRole> qmSecRoles) {
		servletContext.setAttribute(QmSecurityParam.CONTEXT_QMSECROLES, qmSecRoles);
	}

	@Override
	public List<QmSecRole> getQmSecRoles() {
		return (List<QmSecRole>) servletContext.getAttribute(QmSecurityParam.CONTEXT_QMSECROLES);
	}

	@Override
	public QmSecRole getQmSecRole(int id) {
		try {
			List<QmSecRole> qmSecRoles = this.getQmSecRoles();
			for (QmSecRole qmSecRole : qmSecRoles) {
				if (id == qmSecRole.getId()) {
					return qmSecRole;
				}
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}