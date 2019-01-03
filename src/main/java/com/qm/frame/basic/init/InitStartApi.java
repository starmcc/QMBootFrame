package com.qm.frame.basic.init;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author 浅梦工作室
 * @createDate 2018年11月18日17:56:56
 * @Description 项目启动时，初始化某些设定
*/
public @Component class InitStartApi {
	
	private final static Logger LOG = LoggerFactory.getLogger(InitStartApi.class);
	
	/**
	 * @Title init
	 * @Description 项目启动时，只执行一次
	 */
	@PostConstruct
	public void init() {
		LOG.info("\n" +
				" .----------------. .----------------. .----------------. .----------------. .----------------. .----------------. .----------------. \n" +
				"| .--------------. | .--------------. | .--------------. | .--------------. | .--------------. | .--------------. | .--------------. |\n" +
				"| |    ___       | | | ____    ____ | | |  _________   | | |  _______     | | |      __      | | | ____    ____ | | |  _________   | |\n" +
				"| |  .'   '.     | | ||_   \\  /   _|| | | |_   ___  |  | | | |_   __ \\    | | |     /  \\     | | ||_   \\  /   _|| | | |_   ___  |  | |\n" +
				"| | /  .-.  \\    | | |  |   \\/   |  | | |   | |_  \\_|  | | |   | |__) |   | | |    / /\\ \\    | | |  |   \\/   |  | | |   | |_  \\_|  | |\n" +
				"| | | |   | |    | | |  | |\\  /| |  | | |   |  _|      | | |   |  __ /    | | |   / ____ \\   | | |  | |\\  /| |  | | |   |  _|  _   | |\n" +
				"| | \\  `-'  \\_   | | | _| |_\\/_| |_ | | |  _| |_       | | |  _| |  \\ \\_  | | | _/ /    \\ \\_ | | | _| |_\\/_| |_ | | |  _| |___/ |  | |\n" +
				"| |  `.___.\\__|  | | ||_____||_____|| | | |_____|      | | | |____| |___| | | ||____|  |____|| | ||_____||_____|| | | |_________|  | |\n" +
				"| |              | | |              | | |              | | |              | | |              | | |              | | |              | |\n" +
				"| '--------------' | '--------------' | '--------------' | '--------------' | '--------------' | '--------------' | '--------------' |\n" +
				" '----------------' '----------------' '----------------' '----------------' '----------------' '----------------' '----------------' " +
				"\n浅梦-gitHub:https://github.com/starmcc" +
				"\nEmail:[starczt1992@163.com]" +
				"\n开源:[https://github.com/starmcc/QMBoootFrame]" +
				"\n帮助文档:[https://github.com/starmcc/QMBoootFrame/wiki]");
	}
	
	
	@PreDestroy
	public void preDestroy() {
		LOG.info("浅梦gitHub:https://github.com/starmcc/QMBoootFrame");
	}
}
