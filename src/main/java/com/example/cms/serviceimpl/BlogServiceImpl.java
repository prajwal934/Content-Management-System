package com.example.cms.serviceimpl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.cms.dto.BlogRequest;
import com.example.cms.dto.BlogResponse;
import com.example.cms.exception.BlogAlreadyExistByTitle;
import com.example.cms.exception.BlogNotAvailableByTitleException;
import com.example.cms.exception.BlogNotFoundByIdException;
import com.example.cms.exception.TopicNotSpecifiedException;
import com.example.cms.exception.UserNotFoundByIdException;
import com.example.cms.model.Blog;
import com.example.cms.repository.BlogRepository;
import com.example.cms.repository.UserRepository;
import com.example.cms.service.BlogService;
import com.example.cms.utility.ResponseStructure;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BlogServiceImpl  implements BlogService{
	private UserRepository userRepository;
	private BlogRepository blogRepository;
	private ResponseStructure<BlogResponse> responseStructure;
	
	
	@Override
	public ResponseEntity<ResponseStructure<BlogResponse>> createBlog(int userId, BlogRequest blogRequest) {
		return userRepository.findById(userId).map(user -> {
			if(blogRepository.existsByTitle(blogRequest.getTitle())) {
				throw new BlogAlreadyExistByTitle("invalid blog title");
			}
			if(blogRequest.getTopics().length < 1) {
				throw new TopicNotSpecifiedException("failed to create the blog");
			}
			Blog blog = mapToBlog(blogRequest);
			user.getBlogs().add(blog);
			blogRepository.save(blog);
			userRepository.save(user);
			return blog;
		}).map(blog -> ResponseEntity.ok(responseStructure.setStatuscode(HttpStatus.OK.value())
				.setMessage("blog created successfully!").setData(mapToResponse(blog))))
				.orElseThrow(() -> new UserNotFoundByIdException("invalid userId"));
	}
	
	private BlogResponse mapToResponse(Blog blog2) {
		return BlogResponse.builder().blogId(blog2.getBlogId()).about(blog2.getAbout()).topics(blog2.getTopics())
				.title(blog2.getTitle()).build();
	}
	
	private Blog mapToBlog(BlogRequest blogRequest) {
		Blog blog = new Blog();
		blog.setTitle(blogRequest.getTitle());
		blog.setAbout(blogRequest.getAbout());
		blog.setTopics(blogRequest.getTopics());
		return blog;

	}

	@Override
	public ResponseEntity<ResponseStructure<BlogResponse>> fetchByTitle(String title) {
		return blogRepository.findByTitle(title).map(blog -> ResponseEntity.ok(responseStructure.setStatuscode(HttpStatus.OK.value())
				.setMessage("blog fetchByTitle successfully!").setData(mapToResponse(blog))))
				.orElseThrow(() -> new BlogNotAvailableByTitleException("invalid blog title"));
	}

	@Override
	public ResponseEntity<ResponseStructure<BlogResponse>> findById(int blogId) {
		return blogRepository.findById(blogId).map(blog -> ResponseEntity.ok(responseStructure.setStatuscode(HttpStatus.OK.value())
				.setMessage("data fetched").setData(mapToResponse(blog))))
				.orElseThrow(() -> new BlogNotFoundByIdException("invalid blogId"));
	}

	@Override
	public ResponseEntity<ResponseStructure<BlogResponse>> updateBlog(int blogId, BlogRequest blogRequest) {
		Blog blog = mapToBlog(blogRequest);
		return blogRepository.findById(blogId).map(blogs -> {
			blog.setBlogId(blogs.getBlogId());
			blogs = blogRepository.save(blog);
			return ResponseEntity.ok(responseStructure.setStatuscode(HttpStatus.OK.value())
					.setMessage("blog updated successfully!").setData(mapToResponse(blogs)));
		}).orElseThrow(() -> new BlogNotFoundByIdException("Invalid blogId"));
	}

}
