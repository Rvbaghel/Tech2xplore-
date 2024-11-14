package com.Tech2xplore.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author; // Assuming you have a User model

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category; // Link to the category

    @Column(columnDefinition = "TEXT") // For potentially long content
    private String body;

    private String visibility;

    @Lob
    @Column(name = "image", columnDefinition = "BLOB")
    private byte[] image; // Store image as binary data in the database

    private LocalDateTime createdAt; // Store post creation date and time

    // Constructors
    public Post(Long id, String title, User author, Category category, String body, String visibility, byte[] image, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.body = body;
        this.visibility = visibility;
        this.image = image;
        this.createdAt = createdAt;
    }

    public Post() {
        // Default constructor
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
