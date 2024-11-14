package com.Tech2xplore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Tech2xplore.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
	// UserRepository.java
	User findByUsernameAndPassword(String username, String password);

    User findByUsername(String username); // New method to find by username

    User findByEmail(String email);
    User findByMobileNumber(String mobileNumber);// New method to find by email
}
