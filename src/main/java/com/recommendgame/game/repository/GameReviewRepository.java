package com.recommendgame.game.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.recommendgame.game.entity.GameReview;
public interface GameReviewRepository extends JpaRepository<GameReview,Long> {
	GameReview findByAppId(String appId);
	
	@Query("select gr from GameReview gr where appId = :appId and label = '1'")
	List<GameReview> GetPosiReview(String appId);
	
	@Query("select gr from GameReview gr where appId = :appId and label = '0'")
	List<GameReview> GetNegaReview(String appId);
}