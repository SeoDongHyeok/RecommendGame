package com.recommendgame.admin.controller;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.recommendgame.game.entity.Game;
import com.recommendgame.game.entity.GameReview;
import com.recommendgame.game.service.GameReviewService;
import com.recommendgame.game.service.GameService;
import com.recommendgame.page.Page;
import com.recommendgame.user.entity.User;
import com.recommendgame.user.service.UserService;
@Controller
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	GameService gameservice;
	
	@Autowired
	UserService userservice;
	
	@Autowired
	private GameReviewService gameReviewService;
	
	@GetMapping("/game/{appId}")
	public String manageDetail_game(Model model,@PathVariable("appId") String appId) {
		Game game=gameservice.getGameDetail(appId);		
		List<GameReview> posiList=gameReviewService.GetPosiReview(appId);
		List<GameReview> negaList=gameReviewService.GetNegaReview(appId);
		int ReviewRange =0;
		int label = 2;
		List<GameReview> reviewList = new ArrayList<>();
		// 부정 리뷰의 길이가 더 적은 경우 부정리뷰 갯수를 기준으로 반복문 실행
		if(posiList.size() >negaList.size()) {
			ReviewRange = negaList.size();
			label = 0;
		}
		// 부정 리뷰의 길이가 더 적은 경우 부정리뷰 갯수를 기준으로 반복문 실행
		else {
			ReviewRange = posiList.size();
			label = 1;
		}
		
		
		for(int i = 0; i< ReviewRange; i++) {
			reviewList.add(posiList.get(i));
			reviewList.add(negaList.get(i));
		}
		
		
		if(label == 0) {
			while(ReviewRange<posiList.size()) {
				reviewList.add(posiList.get(ReviewRange));
				ReviewRange++;
			}
		}else if(label==1){
			while(ReviewRange<negaList.size()) {
				reviewList.add(negaList.get(ReviewRange));
				ReviewRange++;
			}
		}
		
		model.addAttribute("game", game);			
		model.addAttribute("review",reviewList);
		return "admin/manageDetail";
	}
	
	@GetMapping("/user/{userId}")
	public String manageDetail_user(Model model,@PathVariable("userId") String userId) {
		User user=userservice.getUserById(userId);
		model.addAttribute("user", user);
		String[] tryGame= userservice.getTryGame(user.getUserNo());
		model.addAttribute("tryGame", tryGame);
		
		return "/admin/manageDetail";
	}
	
	
	@PostMapping("/game/edit")
	public String edit(Model model,@RequestParam("appId") String appId,	@RequestParam("category") String category,
			@RequestParam("evaluate") String evaluate) {
		
		gameservice.editGame(appId,category,evaluate);
		
		return "redirect:"+appId;
		
	}
	
	@GetMapping("/game/delete")
	public String delete_game(Model model,@RequestParam("appId") String appId) {
		gameservice.deleteGame(appId);
		model.addAttribute("deleteType","game");
		
		return "/admin/deleteSuccess";
	}
	
	@GetMapping("/user/delete")
	public String delete_user(Model model,@RequestParam("userId") String userId) {
		userservice.userDelete(userId);
		model.addAttribute("deleteType","user");
		
		return "/admin/deleteSuccess";
	}
	
	@GetMapping("/user/search/{page}")
	public String searchUser(Model model, @RequestParam String searchType, @RequestParam String search,@PathVariable("page") int page) {
        int pageCount=userservice.userPageSize(search);
		
		if(page<1) {
			return "redirect:/admin/user/search/1?searchType="+searchType+"&search="+search;
		}else if(page>pageCount) {
			return "redirect:/admin/user/search/"+pageCount+"?searchType="+searchType+"&search="+search;
		}
		
		List<User> userList=userservice.userListAllPage(page,search);
		
		Page pagedto=new Page(page,userservice.userPageSize(search));
		model.addAttribute("userList", userList);
		model.addAttribute("pageCount", pageCount);
		model.addAttribute("currentPage", pagedto.getCurrentPage());
		model.addAttribute("startPage", pagedto.getStartPage());
        model.addAttribute("endPage", pagedto.getEndPage());
        model.addAttribute("search",search);
        model.addAttribute("type", "유저");
		return "/admin/manageList";
	}
	
	@GetMapping("/game/search/{page}")
	public String searchGames(Model model, @RequestParam String searchType, @RequestParam String search,@PathVariable("page") int page) {
        // 검색 로직을 구현하고 결과를 model에 담아서 뷰로 전달
        int pageCount=gameservice.searchGamePageSize(search);
		
		if(page<1) {
			return "redirect:/admin/game/search/1?searchType="+searchType+"&search="+search;
		}else if(page>pageCount) {
			return "redirect:/admin/game/search/"+pageCount+"?searchType="+searchType+"&search="+search;
		}
		
		List<Game> gameList=gameservice.searchGamelistPage(page,search,searchType);
		
		Page pagedto=new Page(page,gameservice.searchGamePageSize(search));
		model.addAttribute("gameList", gameList);
		model.addAttribute("pageCount", pageCount);
		model.addAttribute("currentPage", pagedto.getCurrentPage());
		model.addAttribute("startPage", pagedto.getStartPage());
        model.addAttribute("endPage", pagedto.getEndPage());
        model.addAttribute("type", "게임");
        model.addAttribute("searchType",searchType);
        model.addAttribute("search",search);
        return "admin/manageList"; // 적절한 뷰 이름으로 변경
	    }
}