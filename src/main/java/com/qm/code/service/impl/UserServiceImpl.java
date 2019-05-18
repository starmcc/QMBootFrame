package com.qm.code.service.impl;


import com.qm.code.entity.User;
import com.qm.code.service.UserService;
import com.qm.frame.mybatis.base.QmBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private QmBase qmBase;


	@Override
	public User login(String username, String password) {
		User user = new User();
		user.setUserName(username);
		user.setPassword(password);
		return qmBase.autoSelectOne(user,User.class);
	}
}
