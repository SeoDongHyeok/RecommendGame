package com.recommendgame.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

//	@Value(value = "${uploadPath}")
//	private String uploadPath;
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/images/**")  //이미지 저장 폴더
				.addResourceLocations("classpath:static/images/"); //이미지 저장 위치
		registry.addResourceHandler("/css/**")  //이미지 저장 폴더
				.addResourceLocations("classpath:static/css/"); //이미지 저장 위치
	}

	
	
	
}
