package com.Tech2xplore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Tech2xplore.model.User;
import com.Tech2xplore.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Save the user object in the database
    public void saveUser(User user) throws Exception {
        userRepository.save(user);
    }

    // Authenticate user by username and password
    public User authenticateUser(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }

    // Check if the username exists
    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username) != null;
    }

    // Check if the email exists
    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email) != null;
    }
    
    public boolean existsByMobileNumber(String mobileNumber) {
        return userRepository.findByMobileNumber(mobileNumber) != null;
    }
    
    // Find a user by username
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Save user and return saved entity
    public User save(User user) {
        return userRepository.save(user);
    }
   
    @Autowired
    private HttpSession httpSession; // Inject the HttpSession

    // Method to get the currently logged-in user
    public User getCurrentUser() {
        // Assuming you store the user ID in the session when they log in
        Long userId = (Long) httpSession.getAttribute("userId"); // Replace "userId" with your actual session attribute name
        if (userId != null) {
            return userRepository.findById(userId).orElse(null); // Find and return the user by ID
        }
        return null; // Return null if no user is found
    }
}
