package com.mssd.controller;

import com.mssd.dto.CustomRequestDto;
import com.mssd.dto.CustomRequestResponseDto;
import com.mssd.dto.CustomRequestUpdateDto;
import com.mssd.model.RequestStatus;
import com.mssd.service.CustomRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/custom-requests")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CustomRequestController {
    private final CustomRequestService customRequestService;

    // Public endpoint for submitting custom requests
    @PostMapping("/submit")
    public ResponseEntity<CustomRequestResponseDto> submitCustomRequest(@Valid @RequestBody CustomRequestDto customRequestDto) {
        CustomRequestResponseDto response = customRequestService.submitCustomRequest(customRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Admin endpoints for managing custom requests
    @GetMapping
    public ResponseEntity<List<CustomRequestResponseDto>> getAllCustomRequests() {
        List<CustomRequestResponseDto> requests = customRequestService.getAllCustomRequests();
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomRequestResponseDto> getCustomRequestById(@PathVariable Long id) {
        CustomRequestResponseDto request = customRequestService.getCustomRequestById(id);
        return ResponseEntity.ok(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomRequestResponseDto> updateCustomRequest(
            @PathVariable Long id,
            @Valid @RequestBody CustomRequestUpdateDto updateDto) {
        CustomRequestResponseDto updatedRequest = customRequestService.updateCustomRequest(id, updateDto);
        return ResponseEntity.ok(updatedRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomRequest(@PathVariable Long id) {
        customRequestService.deleteCustomRequest(id);
        return ResponseEntity.noContent().build();
    }

    // Filtering and search endpoints
    @GetMapping("/status/{status}")
    public ResponseEntity<List<CustomRequestResponseDto>> getCustomRequestsByStatus(
            @PathVariable RequestStatus status) {
        List<CustomRequestResponseDto> requests = customRequestService.getCustomRequestsByStatus(status);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/company")
    public ResponseEntity<List<CustomRequestResponseDto>> getCustomRequestsByCompanyName(
            @RequestParam String companyName) {
        List<CustomRequestResponseDto> requests = customRequestService.getCustomRequestsByCompanyName(companyName);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/email")
    public ResponseEntity<List<CustomRequestResponseDto>> getCustomRequestsByEmail(
            @RequestParam String email) {
        List<CustomRequestResponseDto> requests = customRequestService.getCustomRequestsByEmail(email);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<CustomRequestResponseDto>> getCustomRequestsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<CustomRequestResponseDto> requests = customRequestService.getCustomRequestsByDateRange(start, end);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<CustomRequestResponseDto>> getRecentCustomRequests(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime since) {
        List<CustomRequestResponseDto> requests = customRequestService.getRecentCustomRequests(since);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<CustomRequestResponseDto>> getPendingCustomRequests() {
        List<CustomRequestResponseDto> requests = customRequestService.getPendingCustomRequests();
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/budget")
    public ResponseEntity<List<CustomRequestResponseDto>> getCustomRequestsByMinBudget(
            @RequestParam BigDecimal minBudget) {
        List<CustomRequestResponseDto> requests = customRequestService.getCustomRequestsByMinBudget(minBudget);
        return ResponseEntity.ok(requests);
    }

    // Statistics endpoints
    @GetMapping("/stats/count-by-status/{status}")
    public ResponseEntity<Map<String, Object>> getCustomRequestCountByStatus(
            @PathVariable RequestStatus status) {
        long count = customRequestService.getCustomRequestCountByStatus(status);
        return ResponseEntity.ok(Map.of("status", status, "count", count));
    }

    @GetMapping("/stats/total")
    public ResponseEntity<Map<String, Object>> getTotalCustomRequestCount() {
        long totalCount = customRequestService.getTotalCustomRequestCount();
        return ResponseEntity.ok(Map.of("totalCount", totalCount));
    }

    @GetMapping("/stats/summary")
    public ResponseEntity<Map<String, Object>> getCustomRequestSummary() {
        long totalCount = customRequestService.getTotalCustomRequestCount();
        long pendingCount = customRequestService.getCustomRequestCountByStatus(RequestStatus.PENDING);
        long approvedCount = customRequestService.getCustomRequestCountByStatus(RequestStatus.APPROVED);
        long rejectedCount = customRequestService.getCustomRequestCountByStatus(RequestStatus.REJECTED);
        long inProgressCount = customRequestService.getCustomRequestCountByStatus(RequestStatus.IN_PROGRESS);
        long completedCount = customRequestService.getCustomRequestCountByStatus(RequestStatus.COMPLETED);

        return ResponseEntity.ok(Map.of(
                "totalCount", totalCount,
                "pendingCount", pendingCount,
                "approvedCount", approvedCount,
                "rejectedCount", rejectedCount,
                "inProgressCount", inProgressCount,
                "completedCount", completedCount
        ));
    }
} 