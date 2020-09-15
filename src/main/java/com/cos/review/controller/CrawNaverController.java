package com.cos.review.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.review.model.Product;
import com.cos.review.model.SearchKeyword;
import com.cos.review.repository.ProductRepository;
import com.cos.review.repository.SearchKeywordRepository;
import com.cos.review.util.CrawNaverBlog;

@Controller
public class CrawNaverController {

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private SearchKeywordRepository searchKeywordRepository;
	
	@GetMapping("/searchKeyword")
	public @ResponseBody List<SearchKeyword> searchKeyword(){
		System.out.println("searchKeyword 호출됨");
		return searchKeywordRepository.findAll();
	}
	
	@GetMapping("/product")
	public @ResponseBody List<Product> product(){
		int keywordId = searchKeywordRepository.findAll().get(0).getId();
		return productRepository.mFindProductAll(keywordId);
	}
	
	@GetMapping("/product/{keywordId}")
	public @ResponseBody List<Product> productKeyword(@PathVariable int keywordId){
		return productRepository.mFindProductAll(keywordId);
	}
	
	@GetMapping({"/", "/craw/naver"})
	public String crawNaver(Model model) {
		model.addAttribute("keywords", searchKeywordRepository.findAll());
		return "craw_naver";
	}
	
	@GetMapping("/craw/list")
	public String crawList(Model model) {
		model.addAttribute("keywords", searchKeywordRepository.findAll());
		return "craw_list";
	}
	
	@GetMapping("/craw/clear")
	public String crawClear(
			Model model, 
			@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
		
		int keywordId = searchKeywordRepository.findAll().get(0).getId();
		Page<Product> products = productRepository.findByKeywordId(keywordId, pageable);
		model.addAttribute("products", products.getContent());
		model.addAttribute("prev", products.getPageable().getPageNumber()-1);
		model.addAttribute("next", products.getPageable().getPageNumber()+1);
		model.addAttribute("keywordId", keywordId);
		model.addAttribute("allKeyword", searchKeywordRepository.findAll());
		return "craw_clear";
	}
	
	@GetMapping("/craw/clear/{keywordId}")
	public String crawClearKeyword(
			@PathVariable int keywordId,
			Model model, 
			@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
		
		Page<Product> products = productRepository.findByKeywordId(keywordId, pageable);
		model.addAttribute("products", products.getContent());
		model.addAttribute("prev", products.getPageable().getPageNumber()-1);
		model.addAttribute("next", products.getPageable().getPageNumber()+1);
		model.addAttribute("keywordId", keywordId);
		model.addAttribute("allKeyword", searchKeywordRepository.findAll());
		return "craw_clear";
	}
	
	@PostMapping("/craw/naver/proc")
	public @ResponseBody String crawNaverProc(String keyword) {
		
		List<Product> products = new CrawNaverBlog().startAllCraw(keyword);
		
		SearchKeyword searchKeywordEntity = 
				searchKeywordRepository.findByKeyword(keyword);
		
		for (Product product : products) {
			product.setKeyword(searchKeywordEntity);
		}
		
		productRepository.saveAll(products);
		return "성공";
	}
	
	@PostMapping("/craw/keyword/proc")
	public String crawKeywordProc(String keyword) {
		SearchKeyword entity = SearchKeyword.builder()
				.keyword(keyword)
				.build();
		searchKeywordRepository.save(entity);
		return "redirect:/craw/list";
	}
	
	@DeleteMapping("/craw/keyword/delete/{id}")
	public ResponseEntity<?> crawKeywordDelete(@PathVariable int id){
		searchKeywordRepository.deleteById(id);
		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}
	
	@DeleteMapping("/craw/product/delete/id")
	public ResponseEntity<?> crawProductDelete(@PathVariable int id){
		productRepository.deleteById(id);
		return new ResponseEntity<String>("ok", HttpStatus.OK); 
	}
}