package com.qm.code.service;

import java.util.List;

import com.qm.code.entity.User;

/**
 * Copyright © 2018浅梦工作室. All rights reserved.
 * @author: 浅梦
 * @date: 2018年11月26日 上午1:30:34
 * @Description: Demo测试业务层
 */
public interface UserService {

	/**
	 * 调用通用查询demo
	 * @param user
	 * @return
	 */
	List<User> getAutoList(User user);
	
	/**
	 * 调用普通查询demo
	 * @param user
	 * @return
	 */
	List<User> getList(User user);

	/**
	 * 
	 * @param user
	 * @return
	 */
	int del(User user);

	int update(User user);

	int insert(User user);

	User login(String userName,String password);
}
