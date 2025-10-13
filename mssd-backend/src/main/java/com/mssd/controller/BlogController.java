package com.mssd.controller;

import com.mssd.model.Blog;
import com.mssd.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/blogs")
@CrossOrigin(origins = "http://localhost:4200")
public class BlogController {
    
    @Autowired
    private BlogService blogService;
    
    // Get all active blogs for public display
    @GetMapping
    public ResponseEntity<List<Blog>> getAllActiveBlogs() {
        try {
            List<Blog> blogs = blogService.getAllActiveBlogs();
            return ResponseEntity.ok(blogs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Get all blogs for admin (including inactive)
    @GetMapping("/admin")
    public ResponseEntity<List<Blog>> getAllBlogsForAdmin() {
        try {
            List<Blog> blogs = blogService.getAllBlogsForAdmin();
            return ResponseEntity.ok(blogs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Get blog by ID
    @GetMapping("/{id}")
    public ResponseEntity<Blog> getBlogById(@PathVariable Long id) {
        try {
            Optional<Blog> blog = blogService.getBlogById(id);
            return blog.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Create new blog post
    @PostMapping
    public ResponseEntity<Blog> createBlog(@Validated @RequestBody Blog blog) {
        try {
            Blog createdBlog = blogService.createBlog(blog);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBlog);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    // Create blog with image upload
    @PostMapping("/with-image")
    public ResponseEntity<Blog> createBlogWithImage(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam(value = "youtubeUrl", required = false) String youtubeUrl,
            @RequestParam(value = "publishDate", required = false) String publishDate,
            @RequestParam(value = "active", defaultValue = "true") Boolean active,
            @RequestParam(value = "image", required = false) MultipartFile imageFile) {
        
        try {
            // Validate required fields
            if (title == null || title.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            if (description == null || description.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            Blog blog = new Blog();
            blog.setTitle(title.trim());
            blog.setDescription(description.trim());
            blog.setYoutubeUrl(youtubeUrl);
            blog.setActive(active);
            
            // Parse publish date if provided
            if (publishDate != null && !publishDate.isEmpty()) {
                try {
                    // Try different date formats
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                    blog.setPublishDate(LocalDateTime.parse(publishDate, formatter));
                } catch (DateTimeParseException e1) {
                    try {
                        // Try ISO format
                        blog.setPublishDate(LocalDateTime.parse(publishDate));
                    } catch (DateTimeParseException e2) {
                        // If both fail, use current time
                        blog.setPublishDate(LocalDateTime.now());
                    }
                }
            }
            
            Blog createdBlog = blogService.createBlogWithImage(blog, imageFile);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBlog);
        } catch (Exception e) {
            // Log the exception for debugging
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    // Update existing blog post
    @PutMapping("/{id}")
    public ResponseEntity<Blog> updateBlog(@PathVariable Long id, @Validated @RequestBody Blog blogDetails) {
        try {
            Blog updatedBlog = blogService.updateBlog(id, blogDetails);
            return ResponseEntity.ok(updatedBlog);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    // Update blog with image upload
    @PutMapping("/{id}/with-image")
    public ResponseEntity<Blog> updateBlogWithImage(
            @PathVariable Long id,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam(value = "youtubeUrl", required = false) String youtubeUrl,
            @RequestParam(value = "publishDate", required = false) String publishDate,
            @RequestParam(value = "active", defaultValue = "true") Boolean active,
            @RequestParam(value = "image", required = false) MultipartFile imageFile) {
        
        try {
            Blog blogDetails = new Blog();
            blogDetails.setTitle(title);
            blogDetails.setDescription(description);
            blogDetails.setYoutubeUrl(youtubeUrl);
            blogDetails.setActive(active);
            
            // Parse publish date if provided
            if (publishDate != null && !publishDate.isEmpty()) {
                blogDetails.setPublishDate(LocalDateTime.parse(publishDate));
            }
            
            Blog updatedBlog = blogService.updateBlogWithImage(id, blogDetails, imageFile);
            return ResponseEntity.ok(updatedBlog);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    // Delete blog post
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBlog(@PathVariable Long id) {
        try {
            boolean deleted = blogService.deleteBlog(id);
            return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Toggle blog active status
    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<Blog> toggleBlogStatus(@PathVariable Long id) {
        try {
            Blog updatedBlog = blogService.toggleBlogStatus(id);
            return ResponseEntity.ok(updatedBlog);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Search blogs
    @GetMapping("/search")
    public ResponseEntity<List<Blog>> searchBlogs(@RequestParam(value = "q", required = false) String searchTerm) {
        try {
            List<Blog> blogs = blogService.searchBlogs(searchTerm);
            return ResponseEntity.ok(blogs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Get recent blogs
    @GetMapping("/recent")
    public ResponseEntity<List<Blog>> getRecentBlogs() {
        try {
            List<Blog> blogs = blogService.getRecentBlogs();
            return ResponseEntity.ok(blogs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Get blog statistics
    @GetMapping("/stats")
    public ResponseEntity<BlogStats> getBlogStats() {
        try {
            long activeCount = blogService.getActiveBlogCount();
            long totalCount = blogService.getAllBlogsForAdmin().size();
            
            BlogStats stats = new BlogStats(totalCount, activeCount, totalCount - activeCount);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Inner class for blog statistics
    public static class BlogStats {
        private long total;
        private long active;
        private long inactive;
        
        public BlogStats(long total, long active, long inactive) {
            this.total = total;
            this.active = active;
            this.inactive = inactive;
        }
        
        // Getters
        public long getTotal() { return total; }
        public long getActive() { return active; }
        public long getInactive() { return inactive; }
        
        // Setters
        public void setTotal(long total) { this.total = total; }
        public void setActive(long active) { this.active = active; }
        public void setInactive(long inactive) { this.inactive = inactive; }
    }
}