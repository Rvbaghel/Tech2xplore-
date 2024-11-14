package com.Tech2xplore.service;

import com.Tech2xplore.model.Category;
import com.Tech2xplore.model.Post;
import com.Tech2xplore.model.User;
import com.Tech2xplore.repository.CategoryRepository;
import com.Tech2xplore.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // Service method to save a new post
    public void savePost(String title, Long categoryId, User author, String visibility, String body, byte[] image) {
        // Find the category by ID
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // Create a new Post object
        Post post = new Post();
        post.setTitle(title);
        post.setAuthor(author);
        post.setCategory(category);
        post.setVisibility(visibility);
        post.setBody(body);
        post.setImage(image); // Set the image data
        post.setCreatedAt(LocalDateTime.now()); // Set the creation date and time

        // Save the post to the database
        postRepository.save(post);
    }
    public List<Post> getPostsByUser(Long userId) {
        return postRepository.findByAuthorId(userId); // Make sure to implement this in the repository
    }

}
