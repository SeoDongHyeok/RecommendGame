package com.recommendgame.page;

public class Page {
	//한 화면에 표시할 페이지 수
	final int pageSize = 10;
	
	//현재페이지
    int currentPage;
    
    //표시한 페이지의 첫페이지 (ex. 1,11,21,31)
    int startPage;
    
    //표시한 페이지의 마지막페이지 (ex. 10,20,30,40)
    int endPage;
       
    public int getPageSize() {
		return pageSize;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public int getStartPage() {
		return startPage;
	}

	public int getEndPage() {
		return endPage;
	}


	public Page(int page, int total_page){
    	this.currentPage=(page/10)+1;
    	this.startPage=(currentPage - 1) * pageSize + 1;
    	if((page%10)==0) {
    		this.startPage-=10;
    	}
    	this.endPage=Math.min(startPage + pageSize - 1, total_page);
    	
    }
    
}
