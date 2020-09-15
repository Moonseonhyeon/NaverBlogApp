package com.cos.review.util;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.cos.review.model.Product;

public class CrawNaverBlog {
	
	// 스프링 스케줄 리눅스로 크론탭으로  .class파일은 때리기 이걸로 배치 프로그램을 돌리는 거다.
	//키워드 테이블 만들어서
	// 스프링 스케줄!!
		public List<Product> startDayCraw(String keyword){
			int start = 1; //10씩 증가하면 됨.
			List<Product> products = new ArrayList<>();
			while(products.size() < 100) {
				String url = "https://search.naver.com/search.naver?&date_option=0&date_to=&dup_remove=1&nso=&query="+keyword+"&sm=tab_pge&srchby=all&st=date&where=post&start="+start;
				
				try {
					Document doc = Jsoup.connect(url).get();
					Elements els1 = doc.select(".blog .sh_blog_top .sh_blog_title");
					Elements els2 = doc.select(".blog .sh_blog_top .txt_inline");
					Elements els3 = doc.select(".blog .sh_blog_top .sp_thmb img");
					for (int i=0; i<els1.size(); i++) {
						Product product = new Product();
						product.setTitle(els1.get(i).attr("title"));
						product.setBlogUrl(els1.get(i).attr("href"));
						product.setDay(els2.get(i).text());
						if(els3.size() <= i) {
							product.setThumnail("사진없음");
						}else {
							product.setThumnail(els3.get(i).attr("src"));	
						}
						// 오늘 날짜 것만 크롤링 데이터에서 걸러내기
						if(product.getDay().equals(LocalDate.now().toString())) {
							products.add(product);
						}
						
					}
					System.out.println("start : "+start);
					start = start + 10;
				} catch (Exception e) {
					e.printStackTrace();
				}	
			}
			
			return products;
		}
	//관리자가 클릭해서 돌리는 함수
	public List<Product> startAllCraw(String keyword){
		
		//String keyword = "%EA%B0%A4%EB%9F%AD%EC%8B%9C20";
			
		int start = 1; //10씩 증가하면 됨.
		List<Product> products = new ArrayList<>();
		while(products.size() < 101) {
			// keyword를 ENUM으로! 아무거나 못넣게하려고.
			//String url = "https://search.naver.com/search.naver?date_from=&date_option=0&date_to=&dup_remove=1&nso=&post_blogurl=&post_blogurl_without=&query=%EA%B0%A4%EB%9F%AD%EC%8B%9C20&sm=tab_pge&srchby=all&st=sim&where=post&start="+start;
			String url = "https://search.naver.com/search.naver?date_from=&date_option=0&date_to=&dup_remove=1&nso=&post_blogurl=&post_blogurl_without=&query="+keyword+"&sm=tab_pge&srchby=all&st=sim&where=post&start="+start;
			
			// 관련순 -> 최신순 정렬순서 주소어떻게 변하는지 st값이 바뀐다.
			//st=sim -> st=date
						
			try {
				Document doc = Jsoup.connect(url).get();
				Elements els1 = doc.select(".blog .sh_blog_top .sh_blog_title");
				Elements els2 = doc.select(".blog .sh_blog_top .txt_inline");
				Elements els3 = doc.select(".blog .sh_blog_top .sp_thmb img");
				for (int i=0; i<els1.size(); i++) {
					Product product = new Product();
					product.setTitle(els1.get(i).attr("title"));
					product.setBlogUrl(els1.get(i).attr("href"));
					product.setDay(els2.get(i).text()); //날짜 함수하나 만들어서 파싱해서 .contains("전")
					if(els3.size() <= i) {
						product.setThumnail("사진없음");
					}else {
						product.setThumnail(els3.get(i).attr("src"));	
						//System.out.println(els3.get(i).attr("src"));
					}
					products.add(product);
				}
				System.out.println("start : "+start);
				start = start + 10;
			} catch (Exception e) {
				e.printStackTrace();
			}	
		}
		
//		for (Product product : products) {
//			System.out.println("===========================");
//			System.out.println("제목 : "+product.getTitle());
//			System.out.println("주소 : "+product.getBlogUrl());
//			System.out.println("섬네일 : "+product.getThumnail());
//			System.out.println("날짜 : "+product.getDay());
//			System.out.println();
//		}
		
		
		return products;
	}

}
