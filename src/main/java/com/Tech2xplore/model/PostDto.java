package com.Tech2xplore.model;



public class PostDto {
    private String title;
    private String body;
    private Long categoryId; // Category ID, assuming a Long type
    private String visibility;
    private String image;
    
  
    private User author; // Changed to User object instead of String

    // Default Constructor
    public PostDto() {}

    // Parameterized Constructor for convenience
    public PostDto(String title, String body, Long categoryId, String visibility, String image, User author) {
        this.title = title;
        this.body = body;
        this.categoryId = categoryId;
        this.visibility = visibility;
        this.image = image;
        this.author = author;
    }

    // Getters and setters

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
}
