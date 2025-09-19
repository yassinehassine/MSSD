package com.mssd.mapper;

import com.mssd.dto.FormationDto;
import com.mssd.dto.FormationRequestDto;
import com.mssd.model.Formation;
import org.springframework.stereotype.Component;

@Component
public class FormationMapper {
    
    public FormationDto toDto(Formation formation) {
        if (formation == null) {
            return null;
        }
        
        return new FormationDto(
            formation.getId(),
            formation.getTitle(),
            formation.getSlug(),
            formation.getDescription(),
            formation.getCategory(),
            formation.getPrice(),
            formation.getDuration(),
            formation.getImageUrl(),
            formation.getLevel(),
            formation.isPublished(),
            formation.getCreatedAt(),
            formation.getUpdatedAt(),
            formation.getTheme() != null ? formation.getTheme().getId() : null,
            formation.getTheme() != null ? formation.getTheme().getName() : null
        );
    }
    
    public Formation toEntity(FormationRequestDto dto) {
        if (dto == null) {
            return null;
        }
        
        Formation formation = new Formation();
        formation.setTitle(dto.getTitle());
        formation.setSlug(dto.getSlug());
        formation.setDescription(dto.getDescription());
        formation.setCategory(dto.getCategory());
        formation.setPrice(dto.getPrice());
        formation.setDuration(dto.getDuration());
        formation.setImageUrl(dto.getImageUrl());
        formation.setLevel(dto.getLevel());
        formation.setPublished(dto.isPublished());
        // Note: Theme relationship should be set in the service layer
        
        return formation;
    }
    
    public Formation updateEntity(Formation formation, FormationRequestDto dto) {
        if (dto == null) {
            return formation;
        }
        
        formation.setTitle(dto.getTitle());
        formation.setSlug(dto.getSlug());
        formation.setDescription(dto.getDescription());
        formation.setCategory(dto.getCategory());
        formation.setPrice(dto.getPrice());
        formation.setDuration(dto.getDuration());
        formation.setImageUrl(dto.getImageUrl());
        formation.setLevel(dto.getLevel());
        formation.setPublished(dto.isPublished());
        // Note: Theme relationship should be set in the service layer
        
        return formation;
    }
} 