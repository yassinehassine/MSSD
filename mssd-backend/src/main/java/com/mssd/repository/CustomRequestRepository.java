package com.mssd.repository;

import com.mssd.model.CustomRequest;
import com.mssd.model.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CustomRequestRepository extends JpaRepository<CustomRequest, Long> {
    
    List<CustomRequest> findByStatus(RequestStatus status);
    
    List<CustomRequest> findByCompanyNameContainingIgnoreCase(String companyName);
    
    List<CustomRequest> findByEmail(String email);
    
    List<CustomRequest> findByDateSubmittedBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT cr FROM CustomRequest cr WHERE cr.dateSubmitted >= :since ORDER BY cr.dateSubmitted DESC")
    List<CustomRequest> findRecentRequests(@Param("since") LocalDateTime since);
    
    @Query("SELECT cr FROM CustomRequest cr WHERE cr.status = 'PENDING' ORDER BY cr.dateSubmitted ASC")
    List<CustomRequest> findPendingRequests();
    
    @Query("SELECT cr FROM CustomRequest cr WHERE cr.budget >= :minBudget ORDER BY cr.budget DESC")
    List<CustomRequest> findRequestsByMinBudget(@Param("minBudget") java.math.BigDecimal minBudget);
    
    long countByStatus(RequestStatus status);
} 