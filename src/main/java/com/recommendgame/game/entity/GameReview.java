package com.recommendgame.game.entity;
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
@Table(name="w_game_rev")
@Getter
@Setter
@ToString
public class GameReview {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="review_no")
	private Long reviewNo;
	
	@Column(name="app_id")
	private String appId;
	
	@Column(nullable=true,columnDefinition = "LONGTEXT")
	private String review;
	
	@Column(nullable=false)
	private String label;
}