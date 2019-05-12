package com.qm.code.service;

import com.qm.code.entity.User;

/**
 * Copyright © 2018浅梦工作室. All rights reserved.
 * @author 浅梦
 * @date 2018年11月26日 上午1:30:34
 * @Description Demo测试业务层
 */
public interface UserService {

	/**
	 * 登录
	 * @param username
	 * @param password
	 * @return
	 */
	User login(String username, String password);

}
