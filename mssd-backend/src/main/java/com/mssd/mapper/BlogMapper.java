package com.mssd.mapper;

import com.mssd.dto.BlogDto;
import com.mssd.dto.BlogRequestDto;
import com.mssd.model.Blog;
import org.springframework.stereotype.Component;

@Component
public class BlogMapper {

    public BlogDto toDto(Blog blog) {
        if (blog == null) {
            return null;
        }
        
        return BlogDto.builder()
            .id(blog.getId())
            .title(blog.getTitle())
            .slug(blog.getSlug())
            .excerpt(blog.getExcerpt())
            .content(blog.getContent())
            .imageUrl(blog.getImageUrl())
            .author(blog.getAuthor())
            .category(blog.getCategory())
            .tags(blog.getTags())
            .published(blog.getPublished())
            .active(blog.getActive())
            .publishedAt(blog.getPublishedAt())
            .createdAt(blog.getCreatedAt())
            .updatedAt(blog.getUpdatedAt())
            .build();
    }

    public Blog toEntity(BlogRequestDto dto) {
        if (dto == null) {
            return null;
        }
        
        return Blog.builder()
            .title(dto.getTitle())
            .slug(generateSlug(dto.getTitle(), dto.getSlug()))
            .excerpt(dto.getExcerpt())
            .content(dto.getContent())
            .imageUrl(dto.getImageUrl())
            .author(dto.getAuthor())
            .category(dto.getCategory() != null ? dto.getCategory() : "General")
            .tags(dto.getTags())
            .published(dto.getPublished() != null ? dto.getPublished() : false)
            .active(true)
            .build();
    }

    public void updateEntity(Blog blog, BlogRequestDto dto) {
        if (blog == null || dto == null) {
            return;
        }
        
        blog.setTitle(dto.getTitle());
        
        // Only update slug if provided, otherwise keep existing
        if (dto.getSlug() != null && !dto.getSlug().trim().isEmpty()) {
            blog.setSlug(dto.getSlug());
        } else if (blog.getSlug() == null || blog.getSlug().trim().isEmpty()) {
            blog.setSlug(generateSlug(dto.getTitle(), null));
        }
        
        blog.setExcerpt(dto.getExcerpt());
        blog.setContent(dto.getContent());
        blog.setImageUrl(dto.getImageUrl());
        blog.setAuthor(dto.getAuthor());
        blog.setCategory(dto.getCategory() != null ? dto.getCategory() : "General");
        blog.setTags(dto.getTags());
        blog.setPublished(dto.getPublished() != null ? dto.getPublished() : false);
    }

    private String generateSlug(String title, String providedSlug) {
        if (providedSlug != null && !providedSlug.trim().isEmpty()) {
            return providedSlug.toLowerCase().trim();
        }
        
        if (title == null || title.trim().isEmpty()) {
            return "untitled";
        }
        
        return title.toLowerCase()
                   .replaceAll("[^a-z0-9\\s-]", "")
                   .replaceAll("\\s+", "-")
                   .replaceAll("-+", "-")
                   .replaceAll("^-|-$", "");
    }
}