package com.qm.code.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.qm.code.service.UserService;
import org.springframework.stereotype.Service;

import com.qm.code.entity.User;
import com.qm.frame.mybatis.base.QmBase;

@Service
public class UserServiceImpl implements UserService {

	@Resource
	QmBase qmBase;
	/**
	 * mapper的命名空间，请按照约定格式书写，相对应的是sql包中的Mapper的namescapse
	 */
	private static final String NAMESCAPSE = "User-UserServiceImpl-0-Mapper";

	@Override
	public List<User> getAutoList(User user) {
		return qmBase.autoSelectList(user, User.class);
	}

	@Override
	public int del(User user) {
		return qmBase.autoDelete(user);
	}

	@Override
	public int update(User user) {
		return qmBase.autoUpdate(user);
	}

	@Override
	public int insert(User user) {
		return qmBase.autoInsert(user);
	}

	@Override
	public User login(String userName, String password) {
		User user = new User();
		user.setUserName(userName);
		user.setPassword(password);
		List<User> users = this.getAutoList(user);
		if (users.size() == 0) {
		    return null;
		}
		return users.get(0);
	}

	@Override
	public List<User> getList(User user) {
		return qmBase.selectList(NAMESCAPSE + "demo6", user);
	}
	
}
