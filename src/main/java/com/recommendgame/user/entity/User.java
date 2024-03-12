package com.recommendgame.user.entity;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.recommendgame.user.constant.Role;
import com.recommendgame.user.dto.UserFormdto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="w_user")
@Getter
@Setter
@ToString
public class User{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="user_no")
	private Long userNo;
	
	@Column(unique = true, name="user_id")
	private String userId;
	@NotNull
	@Column(nullable = false)
	private String userPw;
	
	@Column(nullable = true)
	private String hCate;
	
	@Column(nullable = true)
	private String tryGame;
	
	@Enumerated(EnumType.STRING) //숫자형이 문자형으로 들어오도록 설정
	private Role role; //com.recommendgame.user.constant.Role 적용
	
	//회원정보 생성, User를 Entity선언하므로 @Entity을 넣어줍니다.
	public static User createUser(UserFormdto userFormdto, PasswordEncoder passwordEncoder) {
		
		User user = new User();
		
		user.setUserId(userFormdto.getUserId());
		String password = passwordEncoder.encode(userFormdto.getUserPw());
		user.setUserPw(password);
		user.setHCate(userFormdto.getHCate());
		user.setTryGame(userFormdto.getTryGame());
		user.setRole(Role.USER); 		
		
		return user;
	}
	public static User updateUser(UserFormdto userFormdto) {
		
		User user = new User();
		
		user.setUserId(userFormdto.getUserId());
		user.setUserPw(userFormdto.getUserPw());
		user.setHCate(userFormdto.getHCate());
		user.setTryGame(userFormdto.getTryGame());
		user.setRole(Role.USER); 		
		
		return user;
	}
}
