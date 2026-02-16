package com.mssd.repository;

import com.mssd.model.Theme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, Long> {
    
    List<Theme> findByActiveTrue();
    
    @Query("SELECT DISTINCT t FROM Theme t LEFT JOIN FETCH t.formations f WHERE t.active = true ORDER BY t.name ASC")
    List<Theme> findActiveThemesWithFormations();
    
    @Query("SELECT t FROM Theme t LEFT JOIN FETCH t.formations f WHERE t.active = true AND f.published = true ORDER BY t.name ASC")
    List<Theme> findActiveThemesWithPublishedFormations();
}