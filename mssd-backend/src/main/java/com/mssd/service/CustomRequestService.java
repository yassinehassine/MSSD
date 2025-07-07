package com.mssd.service;

import com.mssd.dto.CustomRequestDto;
import com.mssd.dto.CustomRequestResponseDto;
import com.mssd.dto.CustomRequestUpdateDto;
import com.mssd.model.RequestStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface CustomRequestService {
    
    // CRUD Operations
    List<CustomRequestResponseDto> getAllCustomRequests();
    CustomRequestResponseDto getCustomRequestById(Long id);
    CustomRequestResponseDto submitCustomRequest(CustomRequestDto dto);
    CustomRequestResponseDto updateCustomRequest(Long id, CustomRequestUpdateDto updateDto);
    void deleteCustomRequest(Long id);
    
    // Filtering and Search
    List<CustomRequestResponseDto> getCustomRequestsByStatus(RequestStatus status);
    List<CustomRequestResponseDto> getCustomRequestsByCompanyName(String companyName);
    List<CustomRequestResponseDto> getCustomRequestsByEmail(String email);
    List<CustomRequestResponseDto> getCustomRequestsByDateRange(LocalDateTime start, LocalDateTime end);
    List<CustomRequestResponseDto> getRecentCustomRequests(LocalDateTime since);
    List<CustomRequestResponseDto> getPendingCustomRequests();
    List<CustomRequestResponseDto> getCustomRequestsByMinBudget(BigDecimal minBudget);
    
    // Statistics
    long getCustomRequestCountByStatus(RequestStatus status);
    long getTotalCustomRequestCount();
}
 