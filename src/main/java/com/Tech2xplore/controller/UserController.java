package com.Tech2xplore.controller;



import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.Tech2xplore.service.CategoryService;
import com.Tech2xplore.model.Category;
import com.Tech2xplore.model.User;
import com.Tech2xplore.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private CategoryService categoryService;

    @ModelAttribute
    public void addLoggedInUserToModel(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", user);
    }
    
    @GetMapping("/")
    public String homePage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", user);

        // Specify the folder containing trending images
        String trendingImageFolder = "src/main/resources/static/image"; // Update with your folder path
        List<String> trendingImages = new ArrayList<>();

        try {
            // List all image files in the folder
            Files.newDirectoryStream(Paths.get(trendingImageFolder), "*.{jpg,jpeg,png,gif}")
                 .forEach(path -> trendingImages.add("/image/" + path.getFileName().toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        model.addAttribute("trendingImages", trendingImages);

        return "home"; // This should correspond to your home.html file
    }


    
    @GetMapping("/register")
    public String register() {
        return "signup"; // Make sure this corresponds to your signup.html template
    }

    @GetMapping("/login")
    public String login() {
        return "login"; // Make sure this corresponds to your login.html template
    }
    @PostMapping("/register")
    public String registerUser(
        @RequestParam("username") String username,
        @RequestParam("email") String email,
        @RequestParam("password") String password,
        @RequestParam("confirmPassword") String confirmPassword,
        @RequestParam("mobileNumber") String mobileNumber,
        @RequestParam("bio") String bio,
        Model model, HttpSession session) {

    	// Validate username (15 characters max, no spaces)
    	if (username == null || username.trim().isEmpty() || username.length() > 15 || username.contains(" ")) {
    	    model.addAttribute("error", "Username cannot be empty, must be up to 15 characters, and cannot contain spaces");
    	    return "signup";
    	}


        // Validate email
        if (email == null || email.trim().isEmpty() || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            model.addAttribute("error", "Invalid email address");
            return "signup";
        }

        // Validate mobile number (10 digits)
        if (mobileNumber == null || mobileNumber.trim().isEmpty() || !mobileNumber.matches("\\d{10}")) {
            model.addAttribute("error", "Mobile number must be 10 digits");
            return "signup";
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            return "signup";
        }

        // Validate password (minimum 6 characters)
        if (password.length() < 4) {
            model.addAttribute("error", "Password must be at least 6 characters long");
            return "signup";
        }

        // Create a new User object and set the fields
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setMobileNumber(mobileNumber);
        user.setBio(bio);
        // Set profilePhoto to null for now

        try {
            // Check if the username or email already exists
            if (userService.existsByUsername(username)) {
                model.addAttribute("error", "Username already exists");
                return "signup";
            }

            if (userService.existsByEmail(email)) {
                model.addAttribute("error", "Email already exists");
                return "signup";
            }
            if(userService.existsByMobileNumber(mobileNumber)) {
            	model.addAttribute("error","Mobile Number already exists");
            }
            													
            userService.saveUser(user); // Save user to the database
            session.setAttribute("loggedInUser", user); // Log in the user
        } catch (Exception e) {
            model.addAttribute("error", "Error saving user");
            return "signup";
        }

        return "redirect:/"; // Redirect to home page
    }

    @PostMapping("/login")
    public String loginUser(
        @RequestParam("username") String username,
        @RequestParam("password") String password,
        Model model, HttpSession session) {

        User user = userService.authenticateUser(username, password);
        if (user != null) {
            session.setAttribute("loggedInUser", user);
            return "redirect:/";
        } else {
            model.addAttribute("error", "Invalid credentials");
            return "login";
        }
    }


    @GetMapping("/logout")
    public String logoutUser(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }


    @GetMapping("/profile")
    public String profilePage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "profilemanagement";
    }
    
      

    @GetMapping("/about")	
    public String aboutPage(Model model) {
        // If you need to pass any data to the about page, you can add it to the model
        // model.addAttribute("someAttribute", someValue);
        return "about"; // This returns about.html
    }

}