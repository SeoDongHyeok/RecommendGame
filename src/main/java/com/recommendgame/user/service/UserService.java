package com.recommendgame.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.recommendgame.user.entity.User;
import com.recommendgame.user.entity.UserTryGame;
import com.recommendgame.user.repository.UserRepository;
import com.recommendgame.user.repository.UserTryGameRepository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Transactional //입출력 준비용 
@Log4j2
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
	
	private final UserRepository userRepository;
	
	private final UserTryGameRepository userTryGameRepository;
	
	@Autowired
	EntityManager em;
	
	// 중복 회원 체크
	private void ValdateDuplicate(User user) {
		User findUser_id = userRepository.findByUserId(user.getUserId());
			
		if(findUser_id != null) {
			throw new IllegalStateException("등록된 사용자 입니다.!!!");
		}		
	}
	
	// 회원 저장
	public User saveUser(User user) {
		ValdateDuplicate(user);
		return userRepository.save(user); 
	}
	
	public String getPassword(String userId) {
		return userRepository.usergetPassword(userId);
	}
	
	//Id로 유저정보 받기
	public User getUserById(String userId) {
		return userRepository.findByUserId(userId); 
	}	

	@Override
	public UserDetails loadUserByUsername(String user_id) throws UsernameNotFoundException {
		User user = userRepository.findByUserId(user_id);
		
		if(user == null) {
			throw new UsernameNotFoundException("해당 사용자가 없습니다." + user_id);
		}
		
		log.info("=================> loadUserByUsername : " + user); 
		
		return org.springframework.security.core.userdetails.User.builder()
				.username(user.getUserId())
				.password(user.getUserPw())
				.roles(user.getRole().toString())
				.build();
	}
	

	public boolean userEdit(String userId,String userPw) {
		User user=userRepository.findByUserId(userId);

		if(user != null) {
			userRepository.updateUser(userId,userPw);
			return true;
		}
		
		return false;
	}

	public boolean userDelete(String userId) {
		User user=userRepository.findByUserId(userId);
		if(user != null) {
			userRepository.deleteByUserId(userId);
			return true;
		}
		
		return false;
	}
	
	
	// TryGame
	public void saveUserTryGame(Long userNo, String[] tryArray) {
		for(String tryGame:tryArray) {
			userTryGameRepository.save(userNo, tryGame);
		}
	}

	public String[] getTryGame(Long userNo) {
		return userTryGameRepository.getAppId(userNo);
	}

	public void deletetriedgame(Long userNo, String appId) {
		userTryGameRepository.deleteTriedGame(userNo, appId);
	}
	
	public void insertTryGame(Long userNo, String string) {
		userTryGameRepository.insertTryGame(userNo, string);
	}
	
	public UserTryGame checkTriedgame(Long userNo,String appId) {
		return userTryGameRepository.checkTriedgame(userNo,appId);
	}
	
	
	//Hate Category
	public void updatehCate(Long userNo,String hCategory) {
		userRepository.updatehCate(userNo, hCategory);
	}
	
	
	// User Paging
	public List<User> userListAllPage(int page,String search) {
		String jpql = "select u from User u where u.userId like :search";
		List<User> userList = em.createQuery(jpql, User.class)
			    .setParameter("search", "%" + search + "%")
			    .setFirstResult((page - 1) * 10)
			    .setMaxResults(10)
			    .getResultList();
			
		return userList;
	}
	
	
	public int userPageSize(String search) {
		int pageCount=(userRepository.searchUser(search).size()/10)+1;
		
		return pageCount;
	}
	
}
