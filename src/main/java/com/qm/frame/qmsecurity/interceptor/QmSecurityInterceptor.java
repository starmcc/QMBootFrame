package com.qm.frame.qmsecurity.interceptor;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.qm.frame.basic.Constant.QmConstant;
import com.qm.frame.basic.controller.QmCode;
import com.qm.frame.basic.controller.QmController;
import com.qm.frame.basic.util.HttpApiUtil;
import com.qm.frame.qmsecurity.config.QmSecurityParam;
import com.qm.frame.qmsecurity.connector.QmSecurityService;
import com.qm.frame.qmsecurity.entity.QmSecInfo;
import com.qm.frame.qmsecurity.entity.QmSecRole;
import com.qm.frame.qmsecurity.note.QmSecurityAPI;

/**
 * Copyright © 2018浅梦工作室. All rights reserved.
 * @author: 浅梦
 * @date: 2018年11月26日 上午1:06:14
 * @Description: QmSecurity拦截器
 */
public @Component class QmSecurityInterceptor extends QmController implements HandlerInterceptor {

	private static final Logger LOG = LoggerFactory.getLogger(QmSecurityInterceptor.class);

	@Autowired
	private QmSecurityService qmSecurityService;
	@Autowired
	private ServletContext servletContext;
	@Autowired(required=false)
	private QmConstant qmConstant;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws IOException {
		// 是否启用用该框架
		if (qmConstant.getSecurityConstant().isStart() == false) return true;

		HttpServletRequest req = (HttpServletRequest) request;
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Method method = handlerMethod.getMethod();
			// 如果有这样的注释，则返回该注解的对象，否则null。
			QmSecurityAPI api = method.getAnnotation(QmSecurityAPI.class);
			// 这里判断如果有注解的话,需要校验Token
			if (api != null) {
				LOG.info("※※※※※※※※※正在验证Token是否正确※※※※※※※※※");
				String token = null;
				try {
					token = req.getHeader(qmConstant.getSecurityConstant().getRequestHeaderTokenKey());
				} catch (Exception e) {
					new Exception("※※※※※※※※※Token为空※※※※※※※※※").printStackTrace();
					return false;
				}
				String ip = HttpApiUtil.getHttpIp(req);
				QmSecInfo qmSecInfo = getTokenAndVerify(token,ip);
				if (qmSecInfo == null) {
					// token和ip验证不通过
					LOG.info("※※※※※※※※※Token与Ip验证不通过※※※※※※※※※");
					response.getWriter().write(super.sendJSON(QmCode._105));
					return false;
				}
				//存储到request,提供在其他地方获取该对象。
				req.setAttribute(QmSecurityParam.CONTEXT_QMSECINFO, qmSecInfo);
				LOG.info("※※※※※※※※※Token已通过校验※※※※※※※※※");
				if (verifyPowers(api,qmSecInfo)) {
					LOG.info("※※※※※※※※※用户鉴权失败※※※※※※※※※");
					return false;
				}
				LOG.info("※※※※※※※※※用户通过鉴权※※※※※※※※※");
			}
		}
		return true;
	}

	/**
	 * 验证Token和ip的可行性
	 * @param token
	 * @param reqIp
	 * @return
	 */
	private QmSecInfo getTokenAndVerify(String token, String reqIp) {
		QmSecInfo qmSecInfo;
		try {
			if (token == null || token.trim().equals("")) {
				return null;
			}
			qmSecInfo = getTokenInfo(token);
			boolean is = qmSecurityService.verifyInfo(qmSecInfo);
			// 如果为fasle则拦截,token不通过
			if (!is) {
				return null;
			}
			String infoIp = qmSecInfo.getInfoMap().get("ip");
			if (!infoIp.equals(reqIp)) {
				return null;
			}
			return qmSecInfo;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取token信息
	 * @param token
	 * @return
	 * @throws Exception
	 */
	private QmSecInfo getTokenInfo(String token) throws Exception {
		JWTVerifier verifier = JWT.require(Algorithm.HMAC256(qmConstant.getSecurityConstant().getSecret())).build();
		DecodedJWT jwt = null;
		try {
			jwt = verifier.verify(token);
		} catch (Exception e) {
			throw new Exception("Token已过期！");
		}
		//这里是一个坑，jwt.getClaims()的不是HashMap,调用remove方法报错,直接给他新的HashMap;
		Map<String, Claim> claimMap = new HashMap<>(jwt.getClaims());
		QmSecInfo qmSecInfo = new QmSecInfo();
		qmSecInfo.setLoginCode(claimMap.get("loginCode").asString());
		claimMap.remove("loginCode");
		qmSecInfo.setRoleName(claimMap.get("roleName").asString());
		claimMap.remove("roleName");
		qmSecInfo.setToken(token);
		Map<String, String> infoMap = new HashMap<String, String>();
		for (String key : claimMap.keySet()) {
			infoMap.put(key, claimMap.get(key).asString());
		}
		qmSecInfo.setInfoMap(infoMap);
		return qmSecInfo;
	}
	
	/**
	 * 鉴权方法
	 * @param QmSecurityAPI
	 * @return
	 */
	private boolean verifyPowers(QmSecurityAPI api,QmSecInfo qmSecInfo) {
		if (api.powerName().equals("QmSecurity-ordinary")) {
			//普通用户权限
			return true;
		}
		if (api.powerName() != null && !api.powerName().trim().equals("")) {
			//缓存中获取系统角色信息和对应的权限列表
			@SuppressWarnings("unchecked")
			List<QmSecRole> secRoles = (List<QmSecRole>) servletContext.getAttribute("QmSecurityRoles");
			//缓存中找不到该数据
			if (secRoles == null || secRoles.size() == 0) {
				LOG.info("※※※※※※※※※error:角色数据为空※※※※※※※※※");
				return false;
			}
			String roleName = qmSecInfo.getRoleName();
			// 遍历缓存中的角色权限
			int a = 0;
			for (QmSecRole qmSecRole : secRoles) {
				a++;
				// 找到该角色对应名称
				if (qmSecRole.getRoleName().equals(roleName)) {
					// 进入鉴权
					// 遍历该角色中的权限列表
					List<String> powers = qmSecRole.getRolePowers();
					for (int i = 0; i < powers.size(); i++) {
						// 如果存在该权限,放行
						if (powers.get(i).equals(api.powerName())) {
							break;
						}
						// 找不到该权限,拦截
						if (i + 1 >= powers.size()) {
							return false;
						}
					}
					break;
				}
				// 找不到该角色
				if (a + 1 >= secRoles.size()) {
					LOG.info("※※※※※※※※※找不到该角色【"+ roleName +"】的信息※※※※※※※※※");
					return false;
				}
			}
		}
		return true;
	}
}
