package com.recommendgame.user.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.recommendgame.game.entity.Category;
import com.recommendgame.game.entity.Game;
import com.recommendgame.game.service.GameService;
import com.recommendgame.user.dto.UserFormdto;
import com.recommendgame.user.entity.User;
import com.recommendgame.user.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;

@Controller
@Log4j2
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	 PasswordEncoder passwordEncoder;

	@Autowired
	 UserService userService;
	
	@Autowired
	GameService gameService;
	
	@Autowired
	UserFormdto userFormdto;
	
	@GetMapping("/login")
	public String login() {
		return "/user/userLogin";
	}

	@GetMapping("/new")
	public String userNew(Model model) {
		List<Game> gameList=gameService.getSearchGameList("");
		List<Category> category=gameService.getAllCategory();
		
		model.addAttribute("userFormdto", new UserFormdto());
		model.addAttribute("category", category);
		model.addAttribute("gameList", gameList);
		
		return "user/userNew";
	}

	// Restful 방식, 특징이 get/post 요청 형식이 다르면 충돌이 생기지 않고 실행됩니다.
	@PostMapping("/new")
	public String userNew(@Valid UserFormdto userFormdto, BindingResult bindingResult, Model model) {
		//에러가 나도 싫어하는 태그와 게임리스트들이 나오게 하기
		List<Category> category=gameService.getAllCategory();
		model.addAttribute("category", category);
		List<Game> gameList=gameService.getSearchGameList("");
		model.addAttribute("gameList", gameList);
		
		//해본게임 배열에 저장
		String[] tryArray = userFormdto.getTryGame().split(",");
		
		if (bindingResult.hasErrors()) {
			return "user/userNew";
		}

		try {
			User user = User.createUser(userFormdto, passwordEncoder);
			userService.saveUser(user);
			if(!(tryArray[0].equals(""))) {
				userService.saveUserTryGame(user.getUserNo(), tryArray);
			}
		} catch (IllegalStateException e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "user/userNew";
		}

		log.info(" =====> post new " + userFormdto);

		return "/user/userLogin";
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if(authentication != null) {
			new SecurityContextLogoutHandler().logout(request, response, authentication);
		}		
		return "redirect:/";
	}
	
	
	@GetMapping("/login/error")
	public String loginError(Model model) {
		model.addAttribute("loginErrorMsg", "아이디나 비번이 틀렸습니다!!!");
		return "user/userLogin";
	}
	
	@GetMapping("/editForm")
	public String editForm(Model model) {
		//현재 로그인해 있는 세션 정보
		Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
		User user = userService.getUserById(authentication.getName());
		//로그인해있지 않으면 메인화면으로
		if(authentication.getName()=="anonymousUser") {
			return "user_Fail";
		}
		model.addAttribute("userId", authentication.getName());
		
		
		
		//싫어하는 장르 정보
		List<String> hCateList=new ArrayList<>();
		String[] hCateArray=user.getHCate().split(",");		
		
		for(String hCate:hCateArray) {
			hCateList.add(hCate);
		}

		if(!(hCateList.isEmpty()) && !(hCateList.get(0).equals(""))) {
			model.addAttribute("hCate",hCateList);
		}	
	
		//해본게임 정보
		List<String> tryGameTitle=gameService.getTitleByappId(user.getUserNo());
		if(!(tryGameTitle.isEmpty())) {
			model.addAttribute("tryGame", tryGameTitle);
		}
		
		
		return "user/userEdit";
	}
	
	@PostMapping("/edit")
	public String edit(@RequestParam("userId") String userId,@RequestParam("userPw") String userPw,@RequestParam("update_userPw") String update_userPw, Model model) {
		
		String checkPw = userService.getPassword(userId);

		//비밀번호 수정할때도 인코딩해줌
		if(passwordEncoder.matches(userPw,checkPw)) {
		
			try {
				//현재 로그인해 있는 세션 정보
				Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
				
				update_userPw = passwordEncoder.encode(update_userPw);
				userService.userEdit(userId, update_userPw);
				if(userService.userEdit(authentication.getName(),update_userPw) == true ) {
					//정보수정 완료 화면으로
					return "user/userEdit_Success";
				}else {
					return "user/user_Fail";
				}
			} catch (Exception e) {
				return "user/user_Fail";
			}
		}else {
			return "user/edit_Fail";
		}
	}
	
	
	@GetMapping("/delete")
	public String delete() {
		try {
			//현재 로그인해 있는 세션 정보
			Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
			if(userService.userDelete(authentication.getName()) == true ) {
				return "user/userDelete_Success";
			}else {
				return "user/user_Fail";
			}
		}catch(Exception e) {
			return "user/user_Fail";
		}
		
	}
	
	@GetMapping("/categoryPopup")
	public String categoryPopup(Model model) {
		List<Category> category=gameService.getAllCategory();

		model.addAttribute("category",category);
		return "user/categoryPopup";
	}
	
	@GetMapping("/tryGamePopup")
	public String tryGamePopup(Model model,@RequestParam("userId") String userId) {
		List<Game> tryGameList=gameService.listAll();
		
		model.addAttribute("tryGameList",tryGameList);
		return "user/tryGamePopup";
	}
	
	@PostMapping("/deleteGame")
	public String deleteGame(@RequestParam("gameName") String gameName,@RequestParam("userId")String userId) {
		String appId = gameService.findAppIdByTitle(gameName);
		Long userNo = userService.getUserById(userId).getUserNo();
		userService.deletetriedgame(userNo,appId);
	    return "user/userEdit";
	}
	
	@PostMapping("/updatetriedGame")
	public String updatetriedGame(@RequestParam("trygame") List<String> gameName, @RequestParam("userId") String userId) {
	    Long userNo = userService.getUserById(userId).getUserNo();
	    
	    for (String gameTitle : gameName) {
	        String appId = gameService.findAppIdByTitle(gameTitle);

	        if(userService.checkTriedgame(userNo, appId) == null) {
	        	userService.insertTryGame(userNo, appId);
	        }
	    }
	    
	    return "user/userEdit";
	}
	
	@PostMapping("/updateCategory")
	public String updatecategory(@RequestParam("hCate") String hCate, @RequestParam("userId") String userId,@RequestParam("parentCategory") String parentCategory) {
	    Long userNo = userService.getUserById(userId).getUserNo();
	    String[] parentCategoryList = parentCategory.split(",");
	    String[] hCateList = hCate.split(",");
	    List<String> ArrayCategory = new ArrayList<>(Arrays.asList(parentCategoryList));
        List<String> ArrayhCate = new ArrayList<>(Arrays.asList(hCateList));
        if(parentCategory.equals("")) {
        	userService.updatehCate(userNo, hCate);
        }else {
        // 배열 A의 각 요소에 대해 B에 포함 여부 확인
        for (String element : ArrayhCate) {
            if (!(ArrayCategory.contains(element))) {
            	parentCategory = parentCategory +","+element ;
            }else {
            	System.out.println(element);
            }
        }
        userService.updatehCate(userNo, parentCategory);
        }
		
	   
	    return "user/userEdit";
	}
	
	@PostMapping("/deleteCategory")
	public String deleteCategory(@RequestParam("hCate") String hCate, @RequestParam("userId") String userId) {
	    Long userNo = userService.getUserById(userId).getUserNo();
        userService.updatehCate(userNo, hCate);
	   
	    return "user/userEdit";
	}
}
