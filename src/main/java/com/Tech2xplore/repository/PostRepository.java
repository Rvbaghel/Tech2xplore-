package com.Tech2xplore.repository;

import com.Tech2xplore.model.Post;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    // You can add custom query methods if needed
	 List<Post> findByAuthorId(Long authorId);

}
