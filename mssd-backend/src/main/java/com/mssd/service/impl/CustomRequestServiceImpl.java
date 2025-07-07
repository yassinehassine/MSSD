package com.mssd.service.impl;

import com.mssd.dto.CustomRequestDto;
import com.mssd.dto.CustomRequestResponseDto;
import com.mssd.dto.CustomRequestUpdateDto;
import com.mssd.exception.ResourceNotFoundException;
import com.mssd.mapper.CustomRequestMapper;
import com.mssd.model.CustomRequest;
import com.mssd.model.RequestStatus;
import com.mssd.repository.CustomRequestRepository;
import com.mssd.service.CustomRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomRequestServiceImpl implements CustomRequestService {
    private final CustomRequestRepository customRequestRepository;
    private final CustomRequestMapper customRequestMapper;

    @Override
    public List<CustomRequestResponseDto> getAllCustomRequests() {
        return customRequestRepository.findAll().stream()
                .map(customRequestMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public CustomRequestResponseDto getCustomRequestById(Long id) {
        CustomRequest customRequest = customRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Custom request not found with id: " + id));
        return customRequestMapper.toResponseDto(customRequest);
    }

    @Override
    public CustomRequestResponseDto submitCustomRequest(CustomRequestDto dto) {
        CustomRequest customRequest = customRequestMapper.toEntity(dto);
        CustomRequest savedRequest = customRequestRepository.save(customRequest);
        return customRequestMapper.toResponseDto(savedRequest);
    }

    @Override
    public CustomRequestResponseDto updateCustomRequest(Long id, CustomRequestUpdateDto updateDto) {
        CustomRequest customRequest = customRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Custom request not found with id: " + id));
        
        customRequestMapper.updateEntity(customRequest, updateDto);
        CustomRequest updatedRequest = customRequestRepository.save(customRequest);
        return customRequestMapper.toResponseDto(updatedRequest);
    }

    @Override
    public void deleteCustomRequest(Long id) {
        if (!customRequestRepository.existsById(id)) {
            throw new ResourceNotFoundException("Custom request not found with id: " + id);
        }
        customRequestRepository.deleteById(id);
    }

    @Override
    public List<CustomRequestResponseDto> getCustomRequestsByStatus(RequestStatus status) {
        return customRequestRepository.findByStatus(status).stream()
                .map(customRequestMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomRequestResponseDto> getCustomRequestsByCompanyName(String companyName) {
        return customRequestRepository.findByCompanyNameContainingIgnoreCase(companyName).stream()
                .map(customRequestMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomRequestResponseDto> getCustomRequestsByEmail(String email) {
        return customRequestRepository.findByEmail(email).stream()
                .map(customRequestMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomRequestResponseDto> getCustomRequestsByDateRange(LocalDateTime start, LocalDateTime end) {
        return customRequestRepository.findByDateSubmittedBetween(start, end).stream()
                .map(customRequestMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomRequestResponseDto> getRecentCustomRequests(LocalDateTime since) {
        return customRequestRepository.findRecentRequests(since).stream()
                .map(customRequestMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomRequestResponseDto> getPendingCustomRequests() {
        return customRequestRepository.findPendingRequests().stream()
                .map(customRequestMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomRequestResponseDto> getCustomRequestsByMinBudget(BigDecimal minBudget) {
        return customRequestRepository.findRequestsByMinBudget(minBudget).stream()
                .map(customRequestMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public long getCustomRequestCountByStatus(RequestStatus status) {
        return customRequestRepository.countByStatus(status);
    }

    @Override
    public long getTotalCustomRequestCount() {
        return customRequestRepository.count();
    }
} 