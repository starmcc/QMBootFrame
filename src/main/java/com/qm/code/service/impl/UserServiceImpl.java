package com.qm.code.service.impl;


import com.qm.code.entity.User;
import com.qm.code.service.UserService;
import com.qm.frame.mybatis.base.QmBase;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

	@Resource
	QmBase qmBase;
	/**
	 * mapper的命名空间，请按照约定格式书写，相对应的是sql包中的Mapper的namescapse
	 */
	private static final String NAMESCAPSE = "UserManager-UserService-0-Mapper";

	@Override
	public List<User> getAutoList(User user) {
		return qmBase.autoSelectList(user, User.class);
	}

	@Override
	public int autoDel(User user) {
		return qmBase.autoDelete(user);
	}

	@Override
	public int autoUpdate(User user) {
		return qmBase.autoUpdate(user);
	}

	@Override
	public int autoInsert(User user) {
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
		return qmBase.selectList(NAMESCAPSE + "getList", user);
	}
}
