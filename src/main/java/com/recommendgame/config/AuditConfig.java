package com.recommendgame.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration  //설정 파일 용도
@EnableWebSecurity  // 등록이나 수정 감지 기능
public class AuditConfig {

	@Bean  // 등록자와 수정자를 처리하는 AuditorAware를 빈으로 등록 합니다.
	public AuditorAware<String> auditorProvider() {
		return new AuditorAwareImpl(); 		
	}
	
}
