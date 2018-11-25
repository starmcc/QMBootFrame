package com.qm.frame.qmsecurity.init;


import java.util.List;

import javax.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.qm.frame.basic.Constant.QmConstant;
import com.qm.frame.qmsecurity.config.QmSecurityParam;
import com.qm.frame.qmsecurity.connector.QmSecurityService;
import com.qm.frame.qmsecurity.entity.QmSecRole;

/**
 * Copyright © 2018浅梦工作室. All rights reserved.
 * @author: 浅梦
 * @date: 2018年11月26日 上午1:06:27
 * @Description: 初始化QmSecurity配置
 */
@Component
public final class QmSecurityInit implements ApplicationRunner /*CommandLineRunner*/{

	@Autowired
	private ServletContext servletContext;
	@Autowired(required=false)
	private QmSecurityService qmSecurityService;
	@Autowired 
	private QmConstant qmConstant;
	
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		//是否开启
		boolean is = qmConstant.getSecurityConstant().isStart();
		if (is == false) {
			return;
		}
		//将角色缓存到application中。
		List<QmSecRole> roles = qmSecurityService.getQmSecRoles();
		servletContext.setAttribute(QmSecurityParam.CONTEXT_QMSECROLES, roles);
	}


}
