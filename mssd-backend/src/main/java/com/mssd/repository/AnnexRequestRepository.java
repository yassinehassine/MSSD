package com.mssd.repository;

import com.mssd.model.AnnexRequest;
import com.mssd.model.AnnexRequest.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnexRequestRepository extends JpaRepository<AnnexRequest, Long> {
    
    List<AnnexRequest> findByStatusOrderByCreatedAtDesc(RequestStatus status);
    
    List<AnnexRequest> findByCompanyNameContainingIgnoreCaseOrderByCreatedAtDesc(String companyName);
    
    @Query("SELECT ar FROM AnnexRequest ar WHERE ar.email = :email ORDER BY ar.createdAt DESC")
    List<AnnexRequest> findByEmailOrderByCreatedAtDesc(@Param("email") String email);
    
    List<AnnexRequest> findAllByOrderByCreatedAtDesc();
    
    @Query("SELECT ar FROM AnnexRequest ar LEFT JOIN FETCH ar.formation f WHERE ar.id = :id")
    AnnexRequest findByIdWithFormation(@Param("id") Long id);
}