package com.Tech2xplore.controller;

import com.Tech2xplore.model.Category;
import com.Tech2xplore.model.Post;
import com.Tech2xplore.model.User;
import com.Tech2xplore.repository.PostRepository;
import com.Tech2xplore.service.CategoryService;
import com.Tech2xplore.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@Controller
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private PostRepository postRepository; // Inject the repository to interact with the database

    // Display the create post page
    @GetMapping("/createPost")
    public String showCreatePostPage(Model model, HttpSession session) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories); // Add the list of categories to the model

        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login"; // Redirect to login if user is not logged in
        }

        model.addAttribute("loggedInUser", loggedInUser);
        return "createPost"; // Return the create post view
    }
    
    @PostMapping("/createPost")
    public String createPost(@RequestParam("title") String title,
                             @RequestParam("category") Long categoryId,
                             @RequestParam("visibility") String visibility,
                             @RequestParam("body") String body,
                             @RequestParam("image") MultipartFile image,
                             HttpSession session, Model model) {

        // Get the logged-in user from the session
        User author = (User) session.getAttribute("loggedInUser");
        if (author == null) {
            return "redirect:/login"; // Redirect to login if no user is in session
        }

        byte[] imageBytes = null;
        String errorMessage = null;

        // Check if body length is within the allowed limit
        if (body.length() > 2500) {
            errorMessage = "The body content must be between 2,000 and 2,500 characters.";
        }

        // Process the image upload if no error so far
        if (errorMessage == null) {
            try {
                if (!image.isEmpty()) {
                    imageBytes = image.getBytes(); // Convert image to byte array
                }
            } catch (IOException e) {
                errorMessage = "Error processing the image upload.";
                e.printStackTrace();
            }
        }

        if (errorMessage != null) {
            model.addAttribute("error", errorMessage);
            model.addAttribute("categories", categoryService.getAllCategories()); // Repopulate categories for the form
            model.addAttribute("loggedInUser", author); // Add the logged-in user to the model
            return "createPost"; // Return to create post view on error
        }

        // Create and populate the Post object
        Post post = new Post();
        post.setTitle(title);
        post.setBody(body);
        post.setVisibility(visibility);
        post.setCategory(categoryService.getCategoryById(categoryId)); // Fetch the category
        post.setImage(imageBytes); // Assuming a setter for image bytes exists
        post.setCreatedAt(LocalDateTime.now());

        String mimeType = image.getContentType(); // Get the MIME type
        String base64Image = imageBytes != null ? Base64.getEncoder().encodeToString(imageBytes) : null;

        model.addAttribute("post", post);
        model.addAttribute("image", base64Image);
        model.addAttribute("mimeType", mimeType); // Add the MIME type to the model
        model.addAttribute("plainTextBody", body); // Directly add the body as plain text for review
        model.addAttribute("loggedInUser", author); // Add the logged-in user to the model

        return "reviewPost"; // Redirect to the review post page
    }


   
    // GET Mapping for Review Post
    @GetMapping("/reviewPost")
    public String reviewPost(Model model, HttpSession session) {
        // Ensure the user is logged in
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login"; // Redirect to login if no user is in session
        }

        model.addAttribute("loggedInUser", loggedInUser); // Add user info to the model
        return "reviewPost"; // Return the review post view
    }

    @PostMapping("/confirmPost")
    public String confirmPost(@RequestParam("title") String title,
                              @RequestParam("categoryId") Long categoryId,
                              @RequestParam("visibility") String visibility,
                              @RequestParam("body") String body,
                              @RequestParam("image") String base64Image, // Base64 string of the image
                              HttpSession session) {

        // Get the logged-in user from the session
        User author = (User) session.getAttribute("loggedInUser");
        if (author == null) {
            return "redirect:/login"; // Redirect to login if no user is in session
        }

        byte[] image = null;
        if (base64Image != null && !base64Image.isEmpty()) {
            // Decode the Base64 image string
            String[] parts = base64Image.split(","); // Split to remove data prefix
            if (parts.length > 1) {
                image = Base64.getDecoder().decode(parts[1]); // Decode the actual Base64 string
            }
        }

        // Save the post using the post service
        postService.savePost(title, categoryId, author, visibility, body, image);

        return "redirect:/"; // Redirect to the home page after successful submission
    }
    
    @GetMapping("/myPosts")
    public String getMyPosts(Model model, HttpSession session) {
        // Ensure the user is logged in
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login"; // Redirect to login if no user is in session
        }

        // Fetch posts by the logged-in user's ID
        List<Post> posts = postRepository.findByAuthorId(loggedInUser.getId()); // Get the posts of the logged-in user
        model.addAttribute("loggedInUser", loggedInUser); 
        model.addAttribute("posts", posts); // Add the posts to the model
        return "myPosts"; // Name of the Thymeleaf template
    }
}
