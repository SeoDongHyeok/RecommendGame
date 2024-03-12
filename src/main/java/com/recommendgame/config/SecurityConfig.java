package com.recommendgame.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean  //메모리에 올려서 사용하기 위해....
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		http.formLogin(form -> form
			.loginPage("/user/login")
			.defaultSuccessUrl("/", true)
			.usernameParameter("userId")
			.passwordParameter("userPw")
			.failureUrl("/user/login/error")
			.permitAll()); // 관리자나 일반 유저도 로그인이 가능하도록... (모든 유저)

		http.logout(Customizer.withDefaults());
		
		
		// 각 사용자의 인증 처리
		http.authorizeHttpRequests(request -> request
				.requestMatchers("/", "user/**", "game/**").permitAll()
				.requestMatchers("css/**", "js/**","images/**").permitAll()
				.requestMatchers("admin/**").hasRole("ADMIN")
				.anyRequest().authenticated());
		
		http.exceptionHandling(exception -> exception
				.authenticationEntryPoint(new CustomAuthenticationEntryPoint()));
						
				
		return http.build();	 
	}
	
	@Bean //패스워드 암호화 작업
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}
