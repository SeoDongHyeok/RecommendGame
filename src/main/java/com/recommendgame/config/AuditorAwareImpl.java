package com.recommendgame.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuditorAwareImpl implements AuditorAware<String>{

	@Override  //java 1.8부터 등장, Optional<String> : 있거나 없으면
	public Optional<String> getCurrentAuditor() {
		
		//인증 받은 후에 처리
		Authentication authentication =             // 인증받은 권한 가져오기 
				SecurityContextHolder.getContext().getAuthentication();
		String userId = "";
		
		if(authentication != null) {
			
			//현재 로그인한 사용자의 정보를 조회하여 사용자 이름을 등록자와 수정자로 지정 합니다.
			userId = authentication.getName();
			
		}		
		return Optional.of(userId);
	}
	
	
}
