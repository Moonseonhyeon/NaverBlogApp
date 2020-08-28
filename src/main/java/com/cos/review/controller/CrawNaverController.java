package com.cos.review.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.review.model.Product;
import com.cos.review.repository.ProductRepository;
import com.cos.review.util.CrawNaverBlog;

@Controller
public class CrawNaverController {

	@Autowired
	private ProductRepository productRepository;
	
	@GetMapping("/craw/naver")
	public String crawNaver() {
		return "craw_naver";
	}
	
	@PostMapping("/craw/naver/proc")
	public @ResponseBody String crawNaver(String keyword) {
		System.out.println(keyword);		
		List<Product> products = new CrawNaverBlog().startAllCraw(keyword); 
		productRepository.saveAll(products);
		return "성공";
	}
	
	// 이거 받아서 디비에 넣을거다
	
	//날짜 부분에 /전 오늘날짜 -몇일 / 
	
}
