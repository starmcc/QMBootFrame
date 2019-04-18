package com.qm.frame.basic.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.qm.frame.basic.body.JsonPathArgumentResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright © 2018浅梦工作室. All rights reserved.
 * @author 浅梦
 * @date 2018年11月24日 上午1:33:11
 * @Description QMFrame框架的基础配置类
 */
@Configuration
//@WebFilter(urlPatterns = "/*")
public class QmFrameConfig implements WebMvcConfigurer {

	/**
	 * 添加静态资源--过滤
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// addResourceLocations指的是文件放置的目录，addResoureHandler指的是对外暴露的访问路径
		registry.addResourceHandler("/views/**").addResourceLocations("classpath:/views/src/");
		WebMvcConfigurer.super.addResourceHandlers(registry);
	}


	/**
	 * 跨域过滤器
	 *
	 * @return
	 */
	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.addAllowedOrigin("*");
		corsConfiguration.addAllowedHeader("*");
		corsConfiguration.addAllowedMethod("*");
		source.registerCorsConfiguration("/**", corsConfiguration);
		return new CorsFilter(source);
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
		fastJsonConfig.setSerializerFeatures(
				SerializerFeature.PrettyFormat,
				SerializerFeature.WriteMapNullValue,
				SerializerFeature.WriteNullStringAsEmpty,
				SerializerFeature.DisableCircularReferenceDetect,
				SerializerFeature.WriteNullListAsEmpty,
				SerializerFeature.WriteDateUseDateFormat);
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
	 * QmBody自定义参数管理器
	 */
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new JsonPathArgumentResolver());
	}

}
