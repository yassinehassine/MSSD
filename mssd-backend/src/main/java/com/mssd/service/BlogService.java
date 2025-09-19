package com.mssd.service;

import com.mssd.dto.BlogDto;
import com.mssd.dto.BlogRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BlogService {
    
    // Public methods for blog viewing
    List<BlogDto> getPublishedBlogs();
    
    Page<BlogDto> getPublishedBlogs(Pageable pageable);
    
    BlogDto getPublishedBlogBySlug(String slug);
    
    Page<BlogDto> searchPublishedBlogs(String keyword, Pageable pageable);
    
    Page<BlogDto> getPublishedBlogsByCategory(String category, Pageable pageable);
    
    // Admin methods for blog management
    List<BlogDto> getAllBlogs();
    
    Page<BlogDto> getAllBlogs(Pageable pageable);
    
    BlogDto getBlogById(Long id);
    
    BlogDto getBlogBySlug(String slug);
    
    Page<BlogDto> searchAllBlogs(String keyword, Pageable pageable);
    
    Page<BlogDto> getBlogsByCategory(String category, Pageable pageable);
    
    Page<BlogDto> getBlogsByAuthor(String author, Pageable pageable);
    
    BlogDto createBlog(BlogRequestDto blogRequestDto);
    
    BlogDto updateBlog(Long id, BlogRequestDto blogRequestDto);
    
    void deleteBlog(Long id);
    
    void deactivateBlog(Long id);
    
    void activateBlog(Long id);
    
    BlogDto publishBlog(Long id);
    
    BlogDto unpublishBlog(Long id);
    
    // Utility methods
    List<String> getCategories();
    
    List<String> getAuthors();
    
    boolean isSlugAvailable(String slug);
    
    boolean isSlugAvailable(String slug, Long excludeId);
}