package com.qm.code.service;

import java.util.List;

import javax.annotation.Resource;

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
	private static final String NAMESCAPSE = "Info-UserServiceImpl-0-Mapper";

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
	public List<User> getList(User user) {
		return qmBase.selectList(NAMESCAPSE + "demo6", user);
	}
	
}
