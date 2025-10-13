package com.mssd.repository;

import com.mssd.entity.PortfolioItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PortfolioItemRepository extends JpaRepository<PortfolioItem, Long> {
    
    List<PortfolioItem> findByActiveTrue();
    
    @Query("SELECT p FROM PortfolioItem p WHERE p.active = true ORDER BY p.trainingDate DESC")
    List<PortfolioItem> findActivePortfolioItemsOrderByDateDesc();
    
    @Query("SELECT p FROM PortfolioItem p ORDER BY p.trainingDate DESC")
    List<PortfolioItem> findAllOrderByDateDesc();
}