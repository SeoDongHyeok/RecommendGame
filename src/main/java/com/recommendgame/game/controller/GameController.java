package com.recommendgame.game.controller;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.recommendgame.game.entity.Category;
import com.recommendgame.game.entity.Game;
import com.recommendgame.game.entity.GameReview;
import com.recommendgame.game.service.GameReviewService;
import com.recommendgame.game.service.GameService;
import com.recommendgame.page.Page;
@Controller
@RequestMapping("/game")
public class GameController {
	@Autowired
	GameService gameservice;
	
	@Autowired
	GameReviewService gameReviewService;
	
	
	@GetMapping("/{appId}")
	public String Detail(Model model,@PathVariable("appId") String appId) {
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
		model.addAttribute("review", reviewList);
		return "/game/gameDetail";
	}
	
	@GetMapping("/search/{page}")
	public String search(Model model,@RequestParam String search,@PathVariable("page") int page) {
		int pageCount=gameservice.searchGamePageSize(search);
		
		if(page<1) {
			return "redirect:/game/search/1?search="+search;
		}else if(page>pageCount) {
			return "redirect:/game/search/"+pageCount+"?search="+search;
		}
		
		List<Game> game=gameservice.searchGamelistPage(page,search,"0");
		
		Page pagedto=new Page(page,gameservice.searchGamePageSize(search));
		
		model.addAttribute("game", game);
		model.addAttribute("pageCount", pageCount);
		model.addAttribute("currentPage", pagedto.getCurrentPage());
		model.addAttribute("startPage", pagedto.getStartPage());
        model.addAttribute("endPage", pagedto.getEndPage());
        model.addAttribute("search",search);
		
		
		model.addAttribute("game", game);
		
		return "/game/gameList";
	}
	
	@PostMapping("/recommend")
	public String recommend(Model model,@RequestParam("AICate") String[] AICate) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication.getName().equals("anonymousUser"))
			return "/user/user_Fail";
		
		List<String> tagList=new ArrayList<>();
		
		if(!(AICate.equals(null))) {
			for(String tagname:AICate) {
				tagList.add(tagname);
			}
		}
		
		List<Game> game =gameservice.getRecommendGame(authentication.getName(),tagList);
		
		model.addAttribute("game", game);
		System.out.println(game);
		return "/game/AIRecommendResult";
	}
	
	@GetMapping("/AIrecommend")
	public String AIrecommend(Model model) {
		List<Category> category=gameservice.getAllCategory();
		model.addAttribute("category",category);
		return "/game/AIRecommend";
	}
	
}