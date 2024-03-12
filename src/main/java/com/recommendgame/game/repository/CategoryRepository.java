package com.recommendgame.game.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recommendgame.game.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, String>{

}
