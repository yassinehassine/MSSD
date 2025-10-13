package com.mssd.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * TEMPORARY Blog Entity - Without created_at and updated_at columns
 * Use this version if you want to test the blog functionality immediately
 * without fixing the database schema first.
 * 
 * To use this version:
 * 1. Replace the content of Blog.java with this file content
 * 2. Restart the application
 * 3. The blog system will work without timestamp columns
 * 4. Later, when you fix the database, you can restore the full version
 */
@Entity
@Table(name = "blog")
public class BlogSimple {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 255)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "youtube_url", length = 500)
    private String youtubeUrl;
    
    @Column(name = "image_url", length = 500)
    private String imageUrl;
    
    @Column(name = "publish_date")
    private LocalDateTime publishDate;
    
    @Column(nullable = false)
    private Boolean active = true;
    
    // Removed created_at and updated_at for now
    // @Column(name = "created_at", nullable = false, updatable = false)
    // private LocalDateTime createdAt;
    
    // @Column(name = "updated_at")
    // private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        // createdAt = LocalDateTime.now();
        // updatedAt = LocalDateTime.now();
        if (publishDate == null) {
            publishDate = LocalDateTime.now();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        // updatedAt = LocalDateTime.now();
    }
    
    // Constructors
    public BlogSimple() {}
    
    public BlogSimple(String title, String description) {
        this.title = title;
        this.description = description;
        this.active = true;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getYoutubeUrl() { return youtubeUrl; }
    public void setYoutubeUrl(String youtubeUrl) { this.youtubeUrl = youtubeUrl; }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    
    public LocalDateTime getPublishDate() { return publishDate; }
    public void setPublishDate(LocalDateTime publishDate) { this.publishDate = publishDate; }
    
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    
    // Dummy getters for compatibility (they will return publish date)
    public LocalDateTime getCreatedAt() { return publishDate; }
    public void setCreatedAt(LocalDateTime createdAt) { /* ignored for now */ }
    
    public LocalDateTime getUpdatedAt() { return publishDate; }
    public void setUpdatedAt(LocalDateTime updatedAt) { /* ignored for now */ }
}