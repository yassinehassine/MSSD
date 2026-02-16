package com.mssd.service;

import com.mssd.dto.PortfolioItemDto;
import com.mssd.entity.PortfolioItem;
import com.mssd.repository.PortfolioItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PortfolioItemService {
    
    private final PortfolioItemRepository portfolioItemRepository;
    private final FileStorageService fileStorageService;
    
    public List<PortfolioItemDto> getAllActivePortfolioItems() {
        return portfolioItemRepository.findActivePortfolioItemsOrderByDateDesc()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<PortfolioItemDto> getAllPortfolioItems() {
        return portfolioItemRepository.findAllOrderByDateDesc()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public Optional<PortfolioItemDto> getPortfolioItemById(Long id) {
        return portfolioItemRepository.findById(id)
                .map(this::convertToDto);
    }
    
    public PortfolioItemDto createPortfolioItem(PortfolioItemDto dto) {
        PortfolioItem entity = convertToEntity(dto);
        entity.setId(null); // Ensure it's a new entity
        PortfolioItem saved = portfolioItemRepository.save(entity);
        return convertToDto(saved);
    }
    
    public PortfolioItemDto updatePortfolioItem(Long id, PortfolioItemDto dto) {
        return portfolioItemRepository.findById(id)
                .map(existing -> {
                    dto.setId(id);
                    PortfolioItem updated = convertToEntity(dto);
                    updated.setCreatedAt(existing.getCreatedAt()); // Preserve creation time
                    return convertToDto(portfolioItemRepository.save(updated));
                })
                .orElseThrow(() -> new RuntimeException("Portfolio item not found with id: " + id));
    }
    
    public void deletePortfolioItem(Long id) {
        portfolioItemRepository.deleteById(id);
    }
    
    public String uploadLogo(MultipartFile file) throws IOException {
        return fileStorageService.storeFile(file);
    }
    
    private PortfolioItemDto convertToDto(PortfolioItem entity) {
        PortfolioItemDto dto = new PortfolioItemDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
    
    private PortfolioItem convertToEntity(PortfolioItemDto dto) {
        PortfolioItem entity = new PortfolioItem();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }
}