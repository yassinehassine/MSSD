package com.mssd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogRequestDto {
    
    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;
    
    @Pattern(regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$", message = "Slug must contain only lowercase letters, numbers, and hyphens")
    @Size(max = 255, message = "Slug must not exceed 255 characters")
    private String slug;
    
    private String excerpt;
    
    @NotBlank(message = "Content is required")
    private String content;
    
    private String imageUrl;
    
    @Size(max = 100, message = "Author must not exceed 100 characters")
    private String author;
    
    @Size(max = 100, message = "Category must not exceed 100 characters")
    private String category;
    
    @Size(max = 500, message = "Tags must not exceed 500 characters")
    private String tags;
    
    @Builder.Default
    private Boolean published = false;
}