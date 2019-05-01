package com.qm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//@EnableCaching // 启动缓存r
@EnableTransactionManagement // 启动事务注解
@SpringBootApplication //SpringBoot启动注解
public class QmApplication {
	
	/**
	 * Spring Boot 启动主函数
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(QmApplication.class, args);
	}
	
}
