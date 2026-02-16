package com.mssd.repository;

import com.mssd.model.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    
    List<Portfolio> findByActiveTrue();
    
    List<Portfolio> findByActiveTrueOrderByCreatedAtDesc();
    
    @Query("SELECT p FROM Portfolio p JOIN FETCH p.formation WHERE p.active = true ORDER BY p.createdAt DESC")
    List<Portfolio> findActivePortfoliosWithFormation();
    
    @Query("SELECT p FROM Portfolio p JOIN FETCH p.formation ORDER BY p.createdAt DESC")
    List<Portfolio> findAllPortfoliosWithFormation();
    
    @Query("SELECT p FROM Portfolio p JOIN FETCH p.formation f WHERE f.category = :category AND p.active = true ORDER BY p.createdAt DESC")
    List<Portfolio> findByFormationCategoryAndActiveTrue(@Param("category") String category);
    
    @Query("SELECT p FROM Portfolio p JOIN FETCH p.formation f WHERE f.id = :formationId AND p.active = true ORDER BY p.createdAt DESC")
    List<Portfolio> findByFormationIdAndActiveTrue(@Param("formationId") Long formationId);
}
