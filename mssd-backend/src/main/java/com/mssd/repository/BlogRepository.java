package com.mssd.repository;

import com.mssd.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
    
    // Find all active blog posts ordered by publish date (newest first)
    List<Blog> findByActiveTrueOrderByPublishDateDesc();
    
    // Find all blog posts (for admin) ordered by creation date (newest first)
    List<Blog> findAllByOrderByCreatedAtDesc();
    
    // Find active blog posts with pagination support
    @Query("SELECT b FROM Blog b WHERE b.active = true ORDER BY b.publishDate DESC")
    List<Blog> findActiveBlogs();
    
    // Search blogs by title or description (for admin)
    @Query("SELECT b FROM Blog b WHERE " +
           "LOWER(b.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(b.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "ORDER BY b.createdAt DESC")
    List<Blog> searchBlogs(@Param("searchTerm") String searchTerm);
    
    // Find blogs published within a date range
    @Query("SELECT b FROM Blog b WHERE b.active = true AND " +
           "b.publishDate BETWEEN :startDate AND :endDate " +
           "ORDER BY b.publishDate DESC")
    List<Blog> findBlogsByDateRange(@Param("startDate") LocalDateTime startDate, 
                                   @Param("endDate") LocalDateTime endDate);
    
    // Count active blogs
    long countByActiveTrue();
    
    // Find recent blogs (last 5)
    @Query("SELECT b FROM Blog b WHERE b.active = true ORDER BY b.publishDate DESC")
    List<Blog> findTop5ByActiveTrueOrderByPublishDateDesc();
}