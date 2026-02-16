package com.mssd.service;

import com.mssd.model.Blog;
import com.mssd.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BlogService {
    
    @Autowired
    private BlogRepository blogRepository;
    
    @Autowired
    private FileService fileService;
    
    // Get all active blog posts for public display
    public List<Blog> getAllActiveBlogs() {
        return blogRepository.findByActiveTrueOrderByPublishDateDesc();
    }
    
    // Get all blog posts for admin (including inactive)
    public List<Blog> getAllBlogsForAdmin() {
        return blogRepository.findAllByOrderByCreatedAtDesc();
    }
    
    // Get blog by ID
    public Optional<Blog> getBlogById(Long id) {
        return blogRepository.findById(id);
    }
    
    // Create new blog post
    public Blog createBlog(Blog blog) {
        // Validate YouTube URL format if provided
        if (blog.getYoutubeUrl() != null && !blog.getYoutubeUrl().isEmpty()) {
            blog.setYoutubeUrl(formatYouTubeUrl(blog.getYoutubeUrl()));
        }
        
        return blogRepository.save(blog);
    }
    
    // Create blog with image upload
    public Blog createBlogWithImage(Blog blog, MultipartFile imageFile) {
        try {
            // Handle image upload if provided
            if (imageFile != null && !imageFile.isEmpty()) {
                String imageUrl = fileService.saveFile(imageFile);
                blog.setImageUrl(imageUrl);
            }
            
            return createBlog(blog);
        } catch (Exception e) {
            throw new RuntimeException("Error creating blog with image: " + e.getMessage());
        }
    }
    
    // Update existing blog post
    public Blog updateBlog(Long id, Blog blogDetails) {
        Optional<Blog> optionalBlog = blogRepository.findById(id);
        
        if (optionalBlog.isPresent()) {
            Blog existingBlog = optionalBlog.get();
            
            existingBlog.setTitle(blogDetails.getTitle());
            existingBlog.setDescription(blogDetails.getDescription());
            
            // Format YouTube URL if provided
            if (blogDetails.getYoutubeUrl() != null && !blogDetails.getYoutubeUrl().isEmpty()) {
                existingBlog.setYoutubeUrl(formatYouTubeUrl(blogDetails.getYoutubeUrl()));
            } else {
                existingBlog.setYoutubeUrl(null);
            }
            
            existingBlog.setImageUrl(blogDetails.getImageUrl());
            existingBlog.setPublishDate(blogDetails.getPublishDate());
            existingBlog.setActive(blogDetails.getActive());
            
            return blogRepository.save(existingBlog);
        } else {
            throw new RuntimeException("Blog not found with id: " + id);
        }
    }
    
    // Update blog with new image
    public Blog updateBlogWithImage(Long id, Blog blogDetails, MultipartFile imageFile) {
        try {
            // Handle new image upload if provided
            if (imageFile != null && !imageFile.isEmpty()) {
                String imageUrl = fileService.saveFile(imageFile);
                blogDetails.setImageUrl(imageUrl);
            }
            
            return updateBlog(id, blogDetails);
        } catch (Exception e) {
            throw new RuntimeException("Error updating blog with image: " + e.getMessage());
        }
    }
    
    // Delete blog post
    public boolean deleteBlog(Long id) {
        Optional<Blog> optionalBlog = blogRepository.findById(id);
        
        if (optionalBlog.isPresent()) {
            Blog blog = optionalBlog.get();
            
            // Delete associated image file if exists
            if (blog.getImageUrl() != null && !blog.getImageUrl().isEmpty()) {
                try {
                    fileService.deleteFile(blog.getImageUrl());
                } catch (Exception e) {
                    // Log error but continue with deletion
                    System.err.println("Error deleting image file: " + e.getMessage());
                }
            }
            
            blogRepository.deleteById(id);
            return true;
        }
        
        return false;
    }
    
    // Toggle blog active status
    public Blog toggleBlogStatus(Long id) {
        Optional<Blog> optionalBlog = blogRepository.findById(id);
        
        if (optionalBlog.isPresent()) {
            Blog blog = optionalBlog.get();
            blog.setActive(!blog.getActive());
            return blogRepository.save(blog);
        } else {
            throw new RuntimeException("Blog not found with id: " + id);
        }
    }
    
    // Search blogs
    public List<Blog> searchBlogs(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllBlogsForAdmin();
        }
        return blogRepository.searchBlogs(searchTerm.trim());
    }
    
    // Get recent blogs for homepage or sidebar
    public List<Blog> getRecentBlogs() {
        return blogRepository.findTop5ByActiveTrueOrderByPublishDateDesc();
    }
    
    // Get blog count
    public long getActiveBlogCount() {
        return blogRepository.countByActiveTrue();
    }
    
    // Helper method to format YouTube URL to embed format
    private String formatYouTubeUrl(String url) {
        if (url == null || url.isEmpty()) {
            return url;
        }
        
        // Convert various YouTube URL formats to embed format
        if (url.contains("youtube.com/watch?v=")) {
            String videoId = url.substring(url.indexOf("v=") + 2);
            if (videoId.contains("&")) {
                videoId = videoId.substring(0, videoId.indexOf("&"));
            }
            return "https://www.youtube.com/embed/" + videoId;
        } else if (url.contains("youtu.be/")) {
            String videoId = url.substring(url.lastIndexOf("/") + 1);
            if (videoId.contains("?")) {
                videoId = videoId.substring(0, videoId.indexOf("?"));
            }
            return "https://www.youtube.com/embed/" + videoId;
        } else if (url.contains("youtube.com/embed/")) {
            // Already in embed format
            return url;
        }
        
        // Return original URL if not a recognized YouTube format
        return url;
    }
}