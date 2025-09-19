package com.mssd.repository;

import com.mssd.model.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
    
    // For public access - only published and active blogs
    List<Blog> findByPublishedTrueAndActiveTrueOrderByPublishedAtDesc();
    
    Page<Blog> findByPublishedTrueAndActiveTrueOrderByPublishedAtDesc(Pageable pageable);
    
    // For admin access - all blogs
    List<Blog> findByActiveTrueOrderByCreatedAtDesc();
    
    Page<Blog> findByActiveTrueOrderByCreatedAtDesc(Pageable pageable);
    
    // Find by slug for public access
    Optional<Blog> findBySlugAndPublishedTrueAndActiveTrue(String slug);
    
    // Find by slug for admin access
    Optional<Blog> findBySlugAndActiveTrue(String slug);
    
    // Search functionality for public
    @Query("SELECT b FROM Blog b WHERE b.published = true AND b.active = true AND " +
           "(LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.content) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.excerpt) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "ORDER BY b.publishedAt DESC")
    Page<Blog> searchPublishedBlogs(@Param("keyword") String keyword, Pageable pageable);
    
    // Search functionality for admin
    @Query("SELECT b FROM Blog b WHERE b.active = true AND " +
           "(LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.content) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.excerpt) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.author) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "ORDER BY b.createdAt DESC")
    Page<Blog> searchAllBlogs(@Param("keyword") String keyword, Pageable pageable);
    
    // Filter by category for public
    Page<Blog> findByPublishedTrueAndActiveTrueAndCategoryOrderByPublishedAtDesc(String category, Pageable pageable);
    
    // Filter by category for admin
    Page<Blog> findByActiveTrueAndCategoryOrderByCreatedAtDesc(String category, Pageable pageable);
    
    // Filter by author for admin
    Page<Blog> findByActiveTrueAndAuthorContainingIgnoreCaseOrderByCreatedAtDesc(String author, Pageable pageable);
    
    // Get distinct categories
    @Query("SELECT DISTINCT b.category FROM Blog b WHERE b.active = true ORDER BY b.category")
    List<String> findDistinctCategories();
    
    // Get distinct authors
    @Query("SELECT DISTINCT b.author FROM Blog b WHERE b.active = true AND b.author IS NOT NULL ORDER BY b.author")
    List<String> findDistinctAuthors();
    
    // Check if slug exists (for validation)
    boolean existsBySlugAndActiveTrue(String slug);
    
    // Check if slug exists excluding current blog (for updates)
    boolean existsBySlugAndActiveTrueAndIdNot(String slug, Long id);
}