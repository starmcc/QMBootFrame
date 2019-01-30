package com.qm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

//SpringBoot启动注解
@SpringBootApplication
//@EnableCaching // 启动缓存r
public class QmApplication {
	
	/**
	 * Spring Boot 启动主函数
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(QmApplication.class, args);
	}
	
}
