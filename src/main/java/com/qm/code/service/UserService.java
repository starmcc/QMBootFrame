package com.qm.code.service;

import com.qm.code.entity.Permissions;
import com.qm.code.entity.User;

import java.util.List;

/**
 * Copyright © 2018浅梦工作室. All rights reserved.
 * @author 浅梦
 * @date 2018年11月26日 上午1:30:34
 * @Description Demo测试业务层
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
	 * 调用auto删除
	 * @param user
	 * @return
	 */
	int autoDel(User user);

	/**
	 * 调用auto更新
	 * @param user
	 * @return
	 */
	int autoUpdate(User user);

	/**
	 * 调用auto新增
	 * @param user
	 * @return
	 */
	int autoInsert(User user);

	/**
	 * 登录
	 * @param userName
	 * @param password
	 * @return
	 */
	User login(String userName,String password);

}
