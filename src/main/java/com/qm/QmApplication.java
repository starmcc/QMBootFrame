package com.qm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//SpringBoot启动注解
@SpringBootApplication
public class QmApplication {
	
	/**
	 * SpringBoot启动主函数
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(QmApplication.class, args);
	}
	
}
