package com.mssd.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "blog")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Blog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 255)
    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;
    
    @Column(nullable = false, unique = true, length = 255)
    @NotBlank(message = "Slug is required")
    @Size(max = 255, message = "Slug must not exceed 255 characters")
    private String slug;
    
    @Column(columnDefinition = "TEXT")
    private String excerpt;
    
    @Column(columnDefinition = "LONGTEXT", nullable = false)
    @NotBlank(message = "Content is required")
    private String content;
    
    @Column(name = "image_url", length = 500)
    private String imageUrl;
    
    @Column(name = "author", length = 100)
    private String author;
    
    @Column(length = 100)
    @Builder.Default
    private String category = "General";
    
    @Column(length = 500)
    private String tags;
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean published = false;
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;
    
    @Column(name = "published_at")
    private LocalDateTime publishedAt;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (published == null) {
            published = false;
        }
        if (active == null) {
            active = true;
        }
        if (category == null || category.trim().isEmpty()) {
            category = "General";
        }
        // Auto-generate excerpt from content if not provided
        if ((excerpt == null || excerpt.trim().isEmpty()) && content != null) {
            generateExcerpt();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        
        // Set publishedAt when first published
        if (published && publishedAt == null) {
            publishedAt = LocalDateTime.now();
        }
        
        // Auto-generate excerpt from content if not provided
        if ((excerpt == null || excerpt.trim().isEmpty()) && content != null) {
            generateExcerpt();
        }
    }
    
    private void generateExcerpt() {
        if (content != null && !content.trim().isEmpty()) {
            // Remove HTML tags and get first 150 characters
            String plainText = content.replaceAll("<[^>]*>", "");
            if (plainText.length() > 150) {
                excerpt = plainText.substring(0, 150) + "...";
            } else {
                excerpt = plainText;
            }
        }
    }
}