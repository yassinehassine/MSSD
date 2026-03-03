package com.mssd.repository;

import com.mssd.model.Theme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, Long> {
    
    List<Theme> findByActiveTrue();
    
    Optional<Theme> findBySlugAndActiveTrue(String slug);
    
    @Query("SELECT DISTINCT t FROM Theme t LEFT JOIN FETCH t.formations f WHERE t.active = true ORDER BY t.name ASC")
    List<Theme> findActiveThemesWithFormations();
    
    @Query("SELECT DISTINCT t FROM Theme t LEFT JOIN FETCH t.formations f WHERE t.active = true ORDER BY t.name ASC")
    List<Theme> findActiveThemesWithPublishedFormations();
    
    @Query("SELECT t FROM Theme t LEFT JOIN FETCH t.formations WHERE t.slug = :slug AND t.active = true")
    Optional<Theme> findBySlugWithFormations(@Param("slug") String slug);
}