package com.recommendgame.game.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.recommendgame.game.entity.Game;



public interface GameRepository extends JpaRepository<Game,String>{
	Game findByAppId(String appId);
	
	List<Game> findAll();

	@Modifying
	@Transactional
	@Query("update Game g set g.category= :category ,g.evaluate= :evaluate where g.appId= :appId")
	void editGame(@Param("appId") String appId,
				@Param("category")  String category,
				@Param("evaluate")  String evaluate);
	
	@Transactional
	@Query("select g from Game g where g.title like %:search%")
	List<Game> searchGame(@Param("search") String search);

	@Transactional
	@Query("select g.title from Game g where appId=:appId")
	String findTitleByAppId(@Param("appId")String appId);
	
	@Transactional
	@Query("select g from Game g where g.appId like %:appId%")
	List<Game> searchTitleByAppId(@Param("appId") String appId);
	
	@Transactional
	@Query("select g from Game g where g.title like %:title%")
	List<Game> searchTitleByTitle(@Param("title") String title);

	@Transactional
	@Query("select g.appId from Game g where g.title=:title")
	String findAppIdByTitle(@Param("title")String title);
	
	@Query("select g from Game g where g.appId  = :appId")
	Game AIRecommandResult(@Param("appId")String appId);
	
}
