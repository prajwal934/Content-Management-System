package com.example.cms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.cms.model.Blog;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Integer> {

	boolean existsByTitle(String title);
	
	Optional<Blog> findByTitle(String title);
}
