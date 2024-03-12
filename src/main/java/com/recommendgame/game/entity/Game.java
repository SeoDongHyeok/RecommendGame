package com.recommendgame.game.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Entity
@Table(name="w_game")
@Getter
@Setter
@ToString
public class Game{
	//앱아이디
	@Id
	@Column(name="app_id")
	private String appId;
	
	//제목
	@Column(nullable = false)
	private String title;
	
	//카테고리
	@Column(nullable = false)
	private String category;
		
	//최소사양
	@Column(nullable = false, name="min_spec",columnDefinition = "TEXT")
	private String minSpec;
	
	//권장사양
	@Column(nullable = false, name="rec_spec",columnDefinition = "TEXT")
	private String recSpec;
	
	//개발사
	@Column(nullable = false,columnDefinition = "VARCHAR(255) default '개발사 정보 확인'")
	private String develope;
	
	//배급사
	@Column(nullable = false,columnDefinition = "VARCHAR(255) default '베급사 정보 확인'")
	private String publish;
	
	//이미지
	@Column(columnDefinition = "VARCHAR(255) default 'defaultImage.jpg'")
	private String image;
	
	//설명
	@Column(nullable = true)
	private String explan;
	
	//평가
	@Column(nullable = true)
	private String evaluate;
	
	//언어
	@Column(nullable = false, columnDefinition = "VARCHAR(255) default '언어 지원여부 확인'")
	private String lang;
	
	//가격
	@Column(nullable = false, columnDefinition = "VARCHAR(255) default '가격 확인 필요'")
	private String price;
	
	//출시일
	@Column(nullable = false, name="rel_date", columnDefinition = "VARCHAR(255) default '출시일 확인 요망'")
	private String relDate;
	
	//멀티여부
	@Column(nullable = false, name="multi_poss",columnDefinition = "TEXT")
	private String multiPoss;
	
	//이용연령
//	@Column(nullable = false, name="age_lim")
//	private String ageLim;
	
}