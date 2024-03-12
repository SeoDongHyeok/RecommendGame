package com.recommendgame.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.recommendgame.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	//회원가입시에 중복 회원인지 검사합니다.
	User findByUserId(String userId);

	
	//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! 실제 DB의 테이블명이나 칼럼명을 맞추는게아닌 자바소스에있는 엔티티에 이름과 일치시켜야함
	@Modifying
	@Transactional
	@Query("update User u set u.userPw= :userPw where u.userId= :userId")
	void updateUser(@Param("userId") String userId, 
					@Param("userPw") String userPw);
	
	//회원탈퇴
	void deleteByUserId(String userId);
	
	List<User> findAll();
	
	@Query("select userNo from User where userId =:userId")
	Long finduserNoByuserId(String userId);

	@Query("select userPw from User where userId =:userId")
	String usergetPassword(String userId);
	
	@Modifying
	@Transactional
	@Query("update User u set u.hCate=:hCategory where u.userNo=:userNo")
	void updatehCate(@Param("userNo")Long userNo,@Param("hCategory")String hCategory);

	@Query("select u from User u where u.userId like %:search%")
	List<User> searchUser(String search);
	
}
