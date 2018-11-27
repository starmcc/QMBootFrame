package com.qm.code.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qm.code.entity.User;
import com.qm.code.service.UserService;
import com.qm.frame.basic.controller.QmCode;
import com.qm.frame.basic.controller.QmController;
import com.qm.frame.qmsecurity.entity.QmSecInfo;
import com.qm.frame.qmsecurity.manager.QmSecurityManager;
import com.qm.frame.qmsecurity.note.QmSecurityAPI;

/**
 * Copyright © 2018浅梦工作室. All rights reserved.
 * @author: 浅梦
 * @date: 2018年11月24日 下午9:06:39
 * @Description: Controller控制器示例
 */
@RestController
@RequestMapping("rest")
public class ResController extends QmController{
	
	@Autowired
	private QmSecurityManager qmSecurityManager; //权限管理框架。
	@Autowired
	private UserService userService;
	
	/**
	 * REST风格以参数以URL进行传递
	 * @param value
	 * @return
	 */
	@GetMapping("/demo1/{id}")
	public String demo1 (@PathVariable int id) {
		return super.sendJSON(QmCode._1,id);
	}
	
	
	
	/**
	 * 该方法以POST进行请求,并以body的形式传递json参数。
	 * 传递格式为：
	 * {
	 * 		"value":{
	 * 			"userName":"zhangsan"
	 * 		}
	 * }
	 * 返回的则是相同的格式。并且调用框架中的QmController的返回格式
	 * @param map
	 * @return
	 */
	@PostMapping("/demo2")
	public String demo2 (@RequestBody Map<?,?> map) {
		return super.sendJSON(QmCode._1,map);
	}
	
	
	/**
	 * 利用QmSercurity进行登录操作
	 * @param value
	 * @return
	 */
	@GetMapping("/demo3")
	public String demo3 () {
		QmSecInfo qmSecInfo = new QmSecInfo();
		//用户唯一标识，可以是用户名，或者是id、或者是code
		qmSecInfo.setLoginCode("qmyule2018");
		//角色名,验证角色权限时会进行比对。
		qmSecInfo.setRoleName("admin");
		//附加信息
		Map<String,String> infoMap = new HashMap<String,String>();
		infoMap.put("nickName", "浅梦");
		qmSecInfo.setInfoMap(infoMap);
		try {
			//调用login方法，会自动生成对应的token，token会存放在QmSecInfo
			qmSecInfo = qmSecurityManager.login(qmSecInfo, 5, 111);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return super.sendJSON(QmCode._500);
			
		}
		//返回token
		return super.sendJSON(QmCode._1,qmSecInfo.getToken());
	}
	
	/**
	 * 模拟限制权限访问
	 * 请查看com.qm.code.sercurity.SecurityOnline
	 * 该类getQmSecRoles设置了系统中的admin角色有demoPower权限。
	 * 请求时必须在头部带上token，token可在demo3获取。
	 * @return
	 */
	@GetMapping("/demo4")
	@QmSecurityAPI(powerName="demoPower")
	public String demo4 () {
		System.out.println("demo4");
		// 授权成功可以调用方法获取该用户储存在token中的信息。
		QmSecInfo info = qmSecurityManager.getQmSecInfo(super.request);
		return super.sendJSON(QmCode._1,info);
	}
	
	/**
	 * 调用通用查询demo
	 * @return
	 */
	@GetMapping("/demo5")
	public String demo5 () {
		System.out.println("demo5");
		List<User> userLis = userService.getAutoList(new User());
		return super.sendJSON(QmCode._1,userLis);
	}
	
	
	/**
	 * 调用通用查询demo
	 * @return
	 */
	@GetMapping("/demo6")
	public String demo6 () {
		System.out.println("demo6");
		List<User> userLis = userService.getList(new User());
		return super.sendJSON(QmCode._1,userLis);
	}
	
	
}
