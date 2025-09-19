package com.mssd.repository;

import com.mssd.model.Formation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FormationRepository extends JpaRepository<Formation, Long> {
    
    Optional<Formation> findBySlug(String slug);
    
    List<Formation> findByCategory(String category);
    
    List<Formation> findByPublishedTrue();
    
    List<Formation> findByLevel(Formation.Level level);
    
    @Query("SELECT f FROM Formation f WHERE f.published = true AND f.category = :category")
    List<Formation> findPublishedByCategory(@Param("category") String category);
    
    @Query("SELECT f FROM Formation f WHERE f.published = true AND f.level = :level")
    List<Formation> findPublishedByLevel(@Param("level") Formation.Level level);
    
    List<Formation> findByThemeId(Long themeId);
    
    @Query("SELECT f FROM Formation f WHERE f.published = true AND f.theme.id = :themeId")
    List<Formation> findPublishedByThemeId(@Param("themeId") Long themeId);
    
    boolean existsBySlug(String slug);
} 