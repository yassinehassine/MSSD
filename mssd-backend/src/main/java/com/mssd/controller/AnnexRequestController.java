package com.mssd.controller;

import com.mssd.dto.AnnexRequestDto;
import com.mssd.dto.AnnexRequestResponseDto;
import com.mssd.model.AnnexRequest.RequestStatus;
import com.mssd.service.AnnexRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/annex-requests")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AnnexRequestController {
    
    private final AnnexRequestService annexRequestService;
    
    /**
     * Submit a new training request
     * POST /api/annex-requests
     */
    @PostMapping
    public ResponseEntity<AnnexRequestResponseDto> createRequest(@Valid @RequestBody AnnexRequestDto requestDto) {
        AnnexRequestResponseDto response = annexRequestService.createRequest(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * Get all training requests (for admin)
     * GET /api/annex-requests
     */
    @GetMapping
    public ResponseEntity<List<AnnexRequestResponseDto>> getAllRequests() {
        List<AnnexRequestResponseDto> requests = annexRequestService.getAllRequests();
        return ResponseEntity.ok(requests);
    }
    
    /**
     * Get training request by ID
     * GET /api/annex-requests/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<AnnexRequestResponseDto> getRequestById(@PathVariable Long id) {
        AnnexRequestResponseDto request = annexRequestService.getRequestById(id);
        return ResponseEntity.ok(request);
    }
    
    /**
     * Get training requests by email
     * GET /api/annex-requests/by-email/{email}
     */
    @GetMapping("/by-email/{email}")
    public ResponseEntity<List<AnnexRequestResponseDto>> getRequestsByEmail(@PathVariable String email) {
        List<AnnexRequestResponseDto> requests = annexRequestService.getRequestsByEmail(email);
        return ResponseEntity.ok(requests);
    }
    
    /**
     * Update request status (for admin)
     * PUT /api/annex-requests/{id}/status
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<AnnexRequestResponseDto> updateRequestStatus(
            @PathVariable Long id, 
            @RequestParam RequestStatus status) {
        AnnexRequestResponseDto updatedRequest = annexRequestService.updateRequestStatus(id, status);
        return ResponseEntity.ok(updatedRequest);
    }
}