package com.qm.frame.basic.config;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.annotation.WebFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.qm.frame.basic.Constant.QmConstant;
import com.qm.frame.basic.body.JsonPathArgumentResolver;
import com.qm.frame.basic.util.QmSpringManager;
import com.qm.frame.qmsecurity.interceptor.QmSecurityInterceptor;

/**
 * Copyright © 2018浅梦工作室. All rights reserved.
 * @author: 浅梦
 * @date: 2018年11月24日 上午1:33:11
 * @Description: QMFrame框架的基础配置类
 */
@Configuration
@WebFilter(urlPatterns = "/*")
@EnableCaching // 启动缓存
@Import(QmSpringManager.class)
public class QmFrameConfig implements WebMvcConfigurer {

	@Autowired
	private QmConstant qmConstant;

//	/**
//	 * 添加类型转换器和格式化器
//	 * @param registry
//	 */
//	@Override
//	public void addFormatters(FormatterRegistry registry) {
//		registry.addFormatterForFieldType(LocalDate.class, new USLocalDateFormatter());
//	}

	/**
	 * 添加静态资源--过滤
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
		// 按需求添加
	}

	/**
	 * 跨域支持
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")// 设置允许跨域的路径
				.allowedOrigins("*")// 设置允许跨域请求的域名
				.allowCredentials(true)// 是否允许证书 不再默认开启
				.allowedMethods("GET", "POST", "PUT", "DELETE")// 设置允许的方法
				.maxAge(3600);// 跨域允许时间
	}

	/**
	 * 配置消息转换器--这里我用的是alibaba 开源的 fastjson
	 */
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		// 1.需要定义一个convert转换消息的对象;
		FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
		// 2.添加fastJson的配置信息，比如：是否要格式化返回的json数据;
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue,
				SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.DisableCircularReferenceDetect,
				SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteDateUseDateFormat);
		// 3处理中文乱码问题
		List<MediaType> fastMediaTypes = new ArrayList<>();
		fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
		// 4.在convert中添加配置信息.
		fastJsonHttpMessageConverter.setSupportedMediaTypes(fastMediaTypes);
		fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
		// 5.将convert添加到converters当中.
		converters.add(fastJsonHttpMessageConverter);
	}

	/**
	 * 权限管理器
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 检查是否开启权限管理器
		boolean start = qmConstant.getSecurityConstant().isStart();
		if (start) {
			registry.addInterceptor(QmSpringManager.getBean(QmSecurityInterceptor.class)).addPathPatterns("/**");
		}
	}

	/**
	 * QmBody自定义参数管理器
	 */
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new JsonPathArgumentResolver());
	}

	/**
	 * QmSecurity权限框架拦截器依赖
	 */
//	@Bean
//	public FilterRegistrationBean<InitFilter> initFilter() {
//		FilterRegistrationBean<InitFilter> filterRegistrationBean = new FilterRegistrationBean<InitFilter>();
//		filterRegistrationBean.setOrder(1);
//		filterRegistrationBean.setFilter(new InitFilter());
//		filterRegistrationBean.setName("initFilter");
//		filterRegistrationBean.addUrlPatterns("/*");
//		return filterRegistrationBean;
//	}

}
