package com.recommendgame.game.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="category")
@Getter
@Setter
@ToString
public class Category{

	@Id
	@Column(name="category")
	private String category;
	
}
