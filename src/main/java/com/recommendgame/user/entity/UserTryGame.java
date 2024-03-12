package com.recommendgame.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="w_user_try")
@Getter
@Setter
@ToString
public class UserTryGame {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="trygame_no")
	private Long tryGameNo;
	
	@Column(name="user_no",nullable=false)
	private Long userNo;
	
	@Column(nullable=false)
	private String tryGame;
}
