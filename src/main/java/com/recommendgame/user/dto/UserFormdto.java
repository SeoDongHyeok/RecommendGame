package com.recommendgame.user.dto;

import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Component
public class UserFormdto {
	private Long userNo;
	
	@NotBlank(message = "아이디는 필수 항목 입니다.")
	private String userId;
	
	@NotNull(message = "비밀번호는 필수 입력 입니다.")
	@Length(min=4, max=15, message = "비번은 4자이상, 15자 이하로 입력 바랍니다.")
	private String userPw;
	
	private String hCate;
	
	private String tryGame;
}
