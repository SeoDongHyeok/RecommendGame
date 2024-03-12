package com.recommendgame.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.recommendgame.user.entity.UserTryGame;

public interface UserTryGameRepository extends JpaRepository<UserTryGame, Long>{
	
	@Modifying
	@Transactional
	@Query("insert into UserTryGame (userNo, tryGame) values (:userNo, :tryGame)")
	void save(@Param("userNo") Long userNo,@Param("tryGame") String tryGame);

	@Query("select u.tryGame from UserTryGame u where u.userNo=:userNo")
	String[] getAppId(@Param("userNo") Long userNo);

	@Modifying
	@Transactional
	@Query("insert into UserTryGame (userNo, tryGame) values (:userNo, :tryGame)")
	void insertTryGame(Long userNo, String tryGame);
	
	@Transactional
	@Modifying
	@Query(value = "delete from UserTryGame u where u.userNo = :userNo and u.tryGame = :appId")
	void deleteTriedGame(Long userNo, String appId);
	
	@Query("select u from UserTryGame u where u.userNo = :userNo and u.tryGame = :appId")
	UserTryGame checkTriedgame(Long userNo,String appId);
}
