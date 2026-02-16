package com.mssd.mapper;

import com.mssd.dto.FormationDto;
import com.mssd.dto.FormationRequestDto;
import com.mssd.dto.ThemeDto;
import com.mssd.model.Formation;
import com.mssd.model.Theme;
import com.mssd.repository.ThemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class FormationMapper {
    
    @Autowired
    private ThemeRepository themeRepository;
    
    public FormationDto toDto(Formation formation) {
        if (formation == null) {
            return null;
        }
        
        ThemeDto themeDto = null;
        if (formation.getTheme() != null) {
            Theme theme = formation.getTheme();
            themeDto = new ThemeDto();
            themeDto.setId(theme.getId());
            themeDto.setName(theme.getName());
            themeDto.setSlug(theme.getSlug());
            themeDto.setDescription(theme.getDescription());
            themeDto.setIconUrl(theme.getIconUrl());
            themeDto.setActive(theme.isActive());
            // formations list not needed in formation context
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
            themeDto
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
        
        // Set theme if themeId is provided
        if (dto.getThemeId() != null) {
            Theme theme = themeRepository.findById(dto.getThemeId())
                .orElseThrow(() -> new IllegalArgumentException("Theme not found with id: " + dto.getThemeId()));
            formation.setTheme(theme);
        }
        
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
        
        // Update theme if themeId is provided
        if (dto.getThemeId() != null) {
            Theme theme = themeRepository.findById(dto.getThemeId())
                .orElseThrow(() -> new IllegalArgumentException("Theme not found with id: " + dto.getThemeId()));
            formation.setTheme(theme);
        } else {
            formation.setTheme(null);
        }
        
        return formation;
    }
} 