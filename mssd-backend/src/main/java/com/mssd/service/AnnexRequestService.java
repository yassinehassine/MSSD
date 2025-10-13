package com.mssd.service;

import com.mssd.dto.AnnexRequestDto;
import com.mssd.dto.AnnexRequestResponseDto;
import com.mssd.exception.ResourceNotFoundException;
import com.mssd.model.AnnexRequest;
import com.mssd.model.Formation;
import com.mssd.repository.AnnexRequestRepository;
import com.mssd.repository.FormationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AnnexRequestService {
    
    private final AnnexRequestRepository annexRequestRepository;
    private final FormationRepository formationRepository;
    
    public AnnexRequestResponseDto createRequest(AnnexRequestDto requestDto) {
        AnnexRequest request = new AnnexRequest();
        request.setCompanyName(requestDto.getCompanyName());
        request.setEmail(requestDto.getEmail());
        request.setPhone(requestDto.getPhone());
        request.setCustom(requestDto.getIsCustom());
        request.setCustomDescription(requestDto.getCustomDescription());
        request.setNumParticipants(requestDto.getNumParticipants());
        request.setModality(requestDto.getModality());
        request.setPreferredDate(requestDto.getPreferredDate());
        request.setNotes(requestDto.getNotes());
        
        // Handle formation selection for existing training
        if (!requestDto.getIsCustom() && requestDto.getFormationId() != null) {
            Formation formation = formationRepository.findById(requestDto.getFormationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Formation not found with id: " + requestDto.getFormationId()));
            request.setFormation(formation);
        }
        
        AnnexRequest savedRequest = annexRequestRepository.save(request);
        return convertToResponseDto(savedRequest);
    }
    
    @Transactional(readOnly = true)
    public List<AnnexRequestResponseDto> getAllRequests() {
        List<AnnexRequest> requests = annexRequestRepository.findAllByOrderByCreatedAtDesc();
        return requests.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public AnnexRequestResponseDto getRequestById(Long id) {
        AnnexRequest request = annexRequestRepository.findByIdWithFormation(id);
        if (request == null) {
            throw new ResourceNotFoundException("Annex request not found with id: " + id);
        }
        return convertToResponseDto(request);
    }
    
    @Transactional(readOnly = true)
    public List<AnnexRequestResponseDto> getRequestsByEmail(String email) {
        List<AnnexRequest> requests = annexRequestRepository.findByEmailOrderByCreatedAtDesc(email);
        return requests.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }
    
    public AnnexRequestResponseDto updateRequestStatus(Long id, AnnexRequest.RequestStatus status) {
        AnnexRequest request = annexRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Annex request not found with id: " + id));
        
        request.setStatus(status);
        AnnexRequest updatedRequest = annexRequestRepository.save(request);
        return convertToResponseDto(updatedRequest);
    }
    
    private AnnexRequestResponseDto convertToResponseDto(AnnexRequest request) {
        AnnexRequestResponseDto dto = new AnnexRequestResponseDto();
        dto.setId(request.getId());
        dto.setCompanyName(request.getCompanyName());
        dto.setEmail(request.getEmail());
        dto.setPhone(request.getPhone());
        dto.setIsCustom(request.isCustom());
        dto.setCustomDescription(request.getCustomDescription());
        dto.setNumParticipants(request.getNumParticipants());
        dto.setModality(request.getModality());
        dto.setPreferredDate(request.getPreferredDate());
        dto.setNotes(request.getNotes());
        dto.setStatus(request.getStatus());
        dto.setCreatedAt(request.getCreatedAt());
        dto.setUpdatedAt(request.getUpdatedAt());
        
        if (request.getFormation() != null) {
            dto.setFormationId(request.getFormation().getId());
            dto.setFormationTitle(request.getFormation().getTitle());
        }
        
        return dto;
    }
}