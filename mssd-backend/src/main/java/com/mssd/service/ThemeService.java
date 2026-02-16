package com.mssd.service;

import com.mssd.dto.ThemeDto;
import com.mssd.dto.FormationSummaryDto;
import com.mssd.dto.ThemeCreateUpdateDto;
import com.mssd.exception.ResourceNotFoundException;
import com.mssd.model.Theme;
import com.mssd.repository.ThemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ThemeService {
    
    private final ThemeRepository themeRepository;
    
    public List<ThemeDto> getAllActiveThemes() {
        List<Theme> themes = themeRepository.findByActiveTrue();
        return themes.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<ThemeDto> getThemesWithFormations() {
        List<Theme> themes = themeRepository.findActiveThemesWithFormations();
        return themes.stream()
                .map(this::convertToDtoWithFormations)
                .collect(Collectors.toList());
    }

    public ThemeDto getThemeById(Long id) {
        Theme theme = themeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theme not found with id: " + id));
        return convertToDto(theme);
    }

    public ThemeDto getThemeBySlug(String slug) {
        // Basic linear search using existing repository methods; could add dedicated query later
        return themeRepository.findByActiveTrue().stream()
                .filter(t -> t.getSlug().equalsIgnoreCase(slug))
                .findFirst()
                .map(this::convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Theme not found with slug: " + slug));
    }

    public ThemeDto getThemeWithFormationsBySlug(String slug) {
        // Load themes with formations then filter
        return themeRepository.findActiveThemesWithPublishedFormations().stream()
                .filter(t -> t.getSlug().equalsIgnoreCase(slug))
                .findFirst()
                .map(this::convertToDtoWithFormations)
                .orElseThrow(() -> new ResourceNotFoundException("Theme not found with slug: " + slug));
    }

    @Transactional
    public ThemeDto createTheme(ThemeCreateUpdateDto dto) {
        Theme theme = new Theme();
        applyDtoToEntity(dto, theme);
        Theme saved = themeRepository.save(theme);
        return convertToDto(saved);
    }

    @Transactional
    public ThemeDto updateTheme(Long id, ThemeCreateUpdateDto dto) {
        Theme theme = themeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theme not found with id: " + id));
        applyDtoToEntity(dto, theme);
        Theme updated = themeRepository.save(theme);
        return convertToDto(updated);
    }

    @Transactional
    public void deleteTheme(Long id) {
        Theme theme = themeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theme not found with id: " + id));
        if (theme.getFormations() != null && !theme.getFormations().isEmpty()) {
            throw new IllegalStateException("Cannot delete theme that still has formations. Reassign or remove formations first.");
        }
        themeRepository.delete(theme);
    }
    
    private ThemeDto convertToDto(Theme theme) {
        ThemeDto dto = new ThemeDto();
        dto.setId(theme.getId());
        dto.setName(theme.getName());
        dto.setSlug(theme.getSlug());
        dto.setDescription(theme.getDescription());
        dto.setIconUrl(theme.getIconUrl());
        dto.setActive(theme.isActive());
        return dto;
    }
    
    private ThemeDto convertToDtoWithFormations(Theme theme) {
        ThemeDto dto = convertToDto(theme);
        
        List<FormationSummaryDto> formationDtos = new ArrayList<>();
        if (theme.getFormations() != null) {
            formationDtos = theme.getFormations().stream()
                    .filter(formation -> formation.isPublished())
                    .map(formation -> {
                        FormationSummaryDto formationDto = new FormationSummaryDto();
                        formationDto.setId(formation.getId());
                        formationDto.setTitle(formation.getTitle());
                        formationDto.setSlug(formation.getSlug());
                        formationDto.setDescription(formation.getDescription());
                        formationDto.setPrice(formation.getPrice());
                        formationDto.setDuration(formation.getDuration());
                        formationDto.setImageUrl(formation.getImageUrl());
                        formationDto.setLevel(formation.getLevel());
                        formationDto.setThemeId(theme.getId());
                        formationDto.setThemeName(theme.getName());
                        return formationDto;
                    })
                    .collect(Collectors.toList());
        }
                
        dto.setFormations(formationDtos);
        return dto;
    }

    @Transactional(readOnly = true)
    public List<ThemeDto> getAllThemesAdmin() {
        return themeRepository.findAll(Sort.by(Sort.Direction.ASC, "name")).stream()
                .map(this::convertToDtoWithFormations)
                .collect(Collectors.toList());
    }

    private void applyDtoToEntity(ThemeCreateUpdateDto dto, Theme theme) {
        theme.setName(dto.getName());
        theme.setSlug(dto.getSlug());
        theme.setDescription(dto.getDescription());
        theme.setIconUrl(dto.getIconUrl());
        if (dto.getActive() != null) {
            theme.setActive(dto.getActive());
        }
    }
}