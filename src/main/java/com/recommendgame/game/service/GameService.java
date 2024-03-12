package com.recommendgame.game.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.recommendgame.game.entity.Category;
import com.recommendgame.game.entity.Game;
import com.recommendgame.game.repository.CategoryRepository;
import com.recommendgame.game.repository.GameRepository;
import com.recommendgame.user.entity.User;
import com.recommendgame.user.service.UserService;

import jakarta.persistence.EntityManager;

@Service
public class GameService {
	@Autowired
	GameRepository gamerepository;
	
	@Autowired
	CategoryRepository caterepository;
	
	@Autowired
	UserService userService;
	
	@Autowired
	EntityManager em;
	
	public List<Game> listAll() {
		return gamerepository.findAll();
	}

	public Game getGameDetail(String appId) {
		Game game=gamerepository.findByAppId(appId);
		
		return game;
	}

	public void deleteGame(String appId) {
		gamerepository.deleteById(appId);
		
	}

	public void editGame(String appId, String category, String evaluate) {
		gamerepository.editGame(appId,category,evaluate);
	}

	public List<Game> getSearchGameList(String search) {
		
		return gamerepository.searchGame(search);
	}

	public List<String> getTitleByappId(Long userNo) {
		String[] tryGameAppId=userService.getTryGame(userNo);
		List<String> title=new ArrayList<>();
		for(String appId : tryGameAppId) {
			title.add(gamerepository.findTitleByAppId(appId));
		}
		
		return title;
	}
	
	public String findAppIdByTitle(String title) {
		String appId = gamerepository.findAppIdByTitle(title); 
		return appId;
	}
	
	public List<Category> getAllCategory() {
		return caterepository.findAll();
	}

	//검색 정보 형태에 따른 검색 시스템
	public List<Game> searchGames(String searchType, String search) {
		if (searchType.equals("1")) {
			return gamerepository.searchTitleByAppId(search);
		}
		else {
			return gamerepository.searchTitleByTitle(search);
		}
	}
	
	//AI게임추천 시스템
	public List<Game> getRecommendGame(String userId,List<String> tagList) {
//		String url = "http://192.168.0.135:5000"; // 형민 PC
		String url = "http://localhost:5000";
		//"http://192.168.0.135:5000"  "http://localhost:5000"
		try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            // POST 요청 설정
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            // 전송할 데이터 생성
            User user=userService.getUserById(userId);
            String info="{";
            //싫어하는 태그 Json형태로 가공
            List<String> hCategory=new ArrayList<>();
            if(!(user.getHCate().equals(""))) {
            	String[] hCateArray=user.getHCate().split(",");
            	for(String hcate:hCateArray) {
                	hCategory.add(hcate);
            	}
            	info+="\"Hate Category\":[";
            	for(int i=0; i<hCategory.size();i++) {
            		info+="\""+hCategory.get(i)+"\",";
            	}
            	info=info.substring(0, info.length() - 1) + "],";
            }
            //해본 게임 Json형태로 가공
            
            String[] tryGameArray = userService.getTryGame(user.getUserNo());
            List<String> tryGame=new ArrayList<>();
            if(!(tryGameArray.equals(""))) {
            	for(String tgame:tryGameArray) {
                	tryGame.add(tgame);
                }
	            info+="\"Try Game\":[";
	            for(int i=0; i<tryGame.size();i++) {
	            	info+="\""+tryGame.get(i)+"\",";
	            }

	            String str1 = (info.substring(info.length()-1,info.length()));
	           
	            if (str1.equals("[")) {
	            	info=info + "],";
	            }else {
	            	info=info.substring(0, info.length() - 1) + "],";
	            }
            }
            
            //좋아하는 태그 Json형태로 가공
            List<String> tag=new ArrayList<>();
            if(!(tagList.isEmpty())) {
	            for(String tagname:tagList) {
	            	tag.add("\""+tagname+"\"");
	            }
	            info+="\"Want Category\":"+tag+",";
            }
            info=info.substring(0, info.length() - 1) + "}";
            System.out.println(info);
            // 데이터 전송을 위한 OutputStream 열기
            con.setDoOutput(true);
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = info.getBytes("utf-8");
                os.write(input, 0, input.length);
            } 
            // 응답 데이터 읽기
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                String line;
                if((line = reader.readLine()) != null) {
                	System.out.println(line);
                	String[] lineArray=line.split(",");
                	List<Game> game=new ArrayList<>();
                	for(String appId:lineArray) {
                		if (gamerepository.AIRecommandResult(appId) != null) {
                			game.add(gamerepository.AIRecommandResult(appId));
                		}
                	}
                	
                	for(Game d:game) {
                		System.out.println(d.getTitle()); 
                	}
       
                    return game;
                }
            } 
			
            // 서버로부터 응답 받기
            int responseCode = con.getResponseCode();
            System.out.println("HTTP Response Code: " + responseCode);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
		List<Game> game = null;
		return game;
	}


	//Game Paging
	public List<Game> searchGamelistPage(int page,String search,String searchType) {
		String jpql = "select g from Game g where g.title like :search";
		if(searchType.equals("1")) {
			jpql = "select g from Game g where g.appId like :search";
		}
		List<Game> gameList = em.createQuery(jpql, Game.class)
		    .setParameter("search", "%" + search + "%")
		    .setFirstResult((page - 1) * 10)
		    .setMaxResults(10)
		    .getResultList();

		
		return gameList;
	}


	public int searchGamePageSize(String search) {
		int pageCount=(gamerepository.searchGame(search).size()/10)+1;
		
		return pageCount;
	}

}
