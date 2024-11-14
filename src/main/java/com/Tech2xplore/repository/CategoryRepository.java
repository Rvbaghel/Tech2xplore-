package com.Tech2xplore.repository;

import com.Tech2xplore.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Additional query methods can be defined here if needed
}
