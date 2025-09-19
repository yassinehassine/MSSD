package com.mssd.mapper;

import com.mssd.dto.ThemeDto;
import com.mssd.dto.ThemeRequestDto;
import com.mssd.model.Theme;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ThemeMapper {
    
    private final FormationMapper formationMapper;
    
    public ThemeMapper(FormationMapper formationMapper) {
        this.formationMapper = formationMapper;
    }
    
    public ThemeDto toDto(Theme theme) {
        if (theme == null) {
            return null;
        }
        
        ThemeDto dto = new ThemeDto();
        dto.setId(theme.getId());
        dto.setName(theme.getName());
        dto.setSlug(theme.getSlug());
        dto.setDescription(theme.getDescription());
        dto.setImageUrl(theme.getImageUrl());
        dto.setPublished(theme.isPublished());
        dto.setCreatedAt(theme.getCreatedAt());
        dto.setUpdatedAt(theme.getUpdatedAt());
        
        if (theme.getFormations() != null) {
            dto.setFormations(theme.getFormations().stream()
                    .map(formationMapper::toDto)
                    .collect(Collectors.toList()));
        }
        
        return dto;
    }
    
    public Theme toEntity(ThemeRequestDto requestDto) {
        if (requestDto == null) {
            return null;
        }
        
        Theme theme = new Theme();
        theme.setName(requestDto.getName());
        theme.setSlug(requestDto.getSlug());
        theme.setDescription(requestDto.getDescription());
        theme.setImageUrl(requestDto.getImageUrl());
        theme.setPublished(requestDto.isPublished());
        
        return theme;
    }
    
    public void updateEntity(Theme theme, ThemeRequestDto requestDto) {
        if (theme == null || requestDto == null) {
            return;
        }
        
        theme.setName(requestDto.getName());
        theme.setSlug(requestDto.getSlug());
        theme.setDescription(requestDto.getDescription());
        theme.setImageUrl(requestDto.getImageUrl());
        theme.setPublished(requestDto.isPublished());
    }
}