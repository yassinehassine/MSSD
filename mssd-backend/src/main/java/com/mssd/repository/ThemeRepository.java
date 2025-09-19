package com.mssd.repository;

import com.mssd.model.Theme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, Long> {
    
    Optional<Theme> findBySlug(String slug);
    
    List<Theme> findByPublishedTrue();
    
    @Query("SELECT t FROM Theme t WHERE t.published = true ORDER BY t.name ASC")
    List<Theme> findAllPublishedOrderByName();
    
    boolean existsBySlug(String slug);
}