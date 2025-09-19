package com.mssd.controller;

import com.mssd.dto.BlogDto;
import com.mssd.dto.BlogRequestDto;
import com.mssd.service.BlogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/blog")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
@Slf4j
public class BlogController {

    private final BlogService blogService;

    // Public endpoints for blog viewing
    
    @GetMapping
    public ResponseEntity<List<BlogDto>> getAllPublishedBlogs() {
        try {
            log.debug("Getting all published blogs");
            List<BlogDto> blogs = blogService.getPublishedBlogs();
            return ResponseEntity.ok(blogs);
        } catch (Exception e) {
            log.error("Error getting published blogs: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/page")
    public ResponseEntity<Page<BlogDto>> getPublishedBlogsWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            log.debug("Getting published blogs with pagination: {}", pageable);
            Page<BlogDto> blogs = blogService.getPublishedBlogs(pageable);
            return ResponseEntity.ok(blogs);
        } catch (Exception e) {
            log.error("Error getting published blogs with pagination: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<BlogDto> getPublishedBlogBySlug(@PathVariable String slug) {
        try {
            log.debug("Getting published blog by slug: {}", slug);
            BlogDto blog = blogService.getPublishedBlogBySlug(slug);
            return ResponseEntity.ok(blog);
        } catch (RuntimeException e) {
            log.warn("Published blog not found with slug: {}", slug);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error getting published blog by slug {}: {}", slug, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Page<BlogDto>> searchPublishedBlogs(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            log.debug("Searching published blogs with keyword: {} and pagination: {}", keyword, pageable);
            Page<BlogDto> blogs = blogService.searchPublishedBlogs(keyword, pageable);
            return ResponseEntity.ok(blogs);
        } catch (Exception e) {
            log.error("Error searching published blogs: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<Page<BlogDto>> getPublishedBlogsByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            log.debug("Getting published blogs by category: {} with pagination: {}", category, pageable);
            Page<BlogDto> blogs = blogService.getPublishedBlogsByCategory(category, pageable);
            return ResponseEntity.ok(blogs);
        } catch (Exception e) {
            log.error("Error getting published blogs by category: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Admin endpoints for blog management
    
    @GetMapping("/admin")
    public ResponseEntity<List<BlogDto>> getAllBlogsForAdmin() {
        try {
            log.debug("Getting all blogs for admin");
            List<BlogDto> blogs = blogService.getAllBlogs();
            return ResponseEntity.ok(blogs);
        } catch (Exception e) {
            log.error("Error getting blogs for admin: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/admin/page")
    public ResponseEntity<Page<BlogDto>> getAllBlogsForAdminWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            log.debug("Getting all blogs for admin with pagination: {}", pageable);
            Page<BlogDto> blogs = blogService.getAllBlogs(pageable);
            return ResponseEntity.ok(blogs);
        } catch (Exception e) {
            log.error("Error getting blogs for admin with pagination: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/admin/{id}")
    public ResponseEntity<BlogDto> getBlogById(@PathVariable Long id) {
        try {
            log.debug("Getting blog by id: {}", id);
            BlogDto blog = blogService.getBlogById(id);
            return ResponseEntity.ok(blog);
        } catch (RuntimeException e) {
            log.warn("Blog not found with id: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error getting blog by id {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/admin/slug/{slug}")
    public ResponseEntity<BlogDto> getBlogBySlug(@PathVariable String slug) {
        try {
            log.debug("Getting blog by slug: {}", slug);
            BlogDto blog = blogService.getBlogBySlug(slug);
            return ResponseEntity.ok(blog);
        } catch (RuntimeException e) {
            log.warn("Blog not found with slug: {}", slug);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error getting blog by slug {}: {}", slug, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/admin/search")
    public ResponseEntity<Page<BlogDto>> searchAllBlogs(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            log.debug("Searching all blogs with keyword: {} and pagination: {}", keyword, pageable);
            Page<BlogDto> blogs = blogService.searchAllBlogs(keyword, pageable);
            return ResponseEntity.ok(blogs);
        } catch (Exception e) {
            log.error("Error searching all blogs: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/admin")
    public ResponseEntity<BlogDto> createBlog(@Valid @RequestBody BlogRequestDto blogRequestDto) {
        try {
            log.debug("Creating new blog with title: {}", blogRequestDto.getTitle());
            BlogDto createdBlog = blogService.createBlog(blogRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBlog);
        } catch (Exception e) {
            log.error("Error creating blog: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<BlogDto> updateBlog(@PathVariable Long id, @Valid @RequestBody BlogRequestDto blogRequestDto) {
        try {
            log.debug("Updating blog with id: {}", id);
            BlogDto updatedBlog = blogService.updateBlog(id, blogRequestDto);
            return ResponseEntity.ok(updatedBlog);
        } catch (RuntimeException e) {
            log.warn("Blog not found for update with id: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error updating blog with id {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> deleteBlog(@PathVariable Long id) {
        try {
            log.debug("Deleting blog with id: {}", id);
            blogService.deleteBlog(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.warn("Blog not found for deletion with id: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error deleting blog with id {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/admin/{id}/publish")
    public ResponseEntity<BlogDto> publishBlog(@PathVariable Long id) {
        try {
            log.debug("Publishing blog with id: {}", id);
            BlogDto publishedBlog = blogService.publishBlog(id);
            return ResponseEntity.ok(publishedBlog);
        } catch (RuntimeException e) {
            log.warn("Blog not found for publishing with id: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error publishing blog with id {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/admin/{id}/unpublish")
    public ResponseEntity<BlogDto> unpublishBlog(@PathVariable Long id) {
        try {
            log.debug("Unpublishing blog with id: {}", id);
            BlogDto unpublishedBlog = blogService.unpublishBlog(id);
            return ResponseEntity.ok(unpublishedBlog);
        } catch (RuntimeException e) {
            log.warn("Blog not found for unpublishing with id: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error unpublishing blog with id {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Utility endpoints
    
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getCategories() {
        try {
            log.debug("Getting all blog categories");
            List<String> categories = blogService.getCategories();
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            log.error("Error getting blog categories: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/authors")
    public ResponseEntity<List<String>> getAuthors() {
        try {
            log.debug("Getting all blog authors");
            List<String> authors = blogService.getAuthors();
            return ResponseEntity.ok(authors);
        } catch (Exception e) {
            log.error("Error getting blog authors: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/admin/slug-available")
    public ResponseEntity<Map<String, Boolean>> checkSlugAvailability(
            @RequestParam String slug,
            @RequestParam(required = false) Long excludeId) {
        try {
            log.debug("Checking slug availability: {}", slug);
            boolean available = excludeId != null ? 
                blogService.isSlugAvailable(slug, excludeId) : 
                blogService.isSlugAvailable(slug);
            
            Map<String, Boolean> response = new HashMap<>();
            response.put("available", available);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error checking slug availability: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}