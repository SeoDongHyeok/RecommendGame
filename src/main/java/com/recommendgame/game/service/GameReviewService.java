package com.recommendgame.game.service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.recommendgame.game.entity.GameReview;
import com.recommendgame.game.repository.GameReviewRepository;
@Service
public class GameReviewService {
	@Autowired
	GameReviewRepository gamereviewrepository;
	
	public GameReview getGameReview(String appId) {
		return gamereviewrepository.findByAppId(appId);
	}
	
	public List<GameReview> GetPosiReview(String appId) {
		return gamereviewrepository.GetPosiReview(appId);
	}
	public List<GameReview> GetNegaReview(String appId) {
		return gamereviewrepository.GetNegaReview(appId);
	}
}