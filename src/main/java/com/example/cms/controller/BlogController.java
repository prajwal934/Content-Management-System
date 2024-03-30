package com.example.cms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cms.dto.BlogRequest;
import com.example.cms.dto.BlogResponse;
import com.example.cms.service.BlogService;
import com.example.cms.utility.ResponseStructure;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
public class BlogController {

	private BlogService blogService;
	
	@PostMapping(value = "/users/{userId}/blogs")
	public ResponseEntity<ResponseStructure<BlogResponse>> createBlog(@PathVariable int userId , @RequestBody BlogRequest blogRequest) {
		return blogService.createBlog(userId , blogRequest);
	}
	
	@GetMapping(value = "/titles/{title}/blogs")
	public ResponseEntity<ResponseStructure<BlogResponse>> fetchByTitle(@PathVariable String title) {
		return blogService.fetchByTitle(title);
	}
	
	@GetMapping(value = "/blogs/{blogId}")
	public ResponseEntity<ResponseStructure<BlogResponse>> findById(@PathVariable int blogId) {
		return blogService.findById(blogId);
	}
	
	@PutMapping(value = "/blogs/{blogId}")
	public ResponseEntity<ResponseStructure<BlogResponse>> updateBlog(@PathVariable int blogId, @RequestBody BlogRequest blogRequest) {
		return blogService.updateBlog(blogId , blogRequest);
	}
}
