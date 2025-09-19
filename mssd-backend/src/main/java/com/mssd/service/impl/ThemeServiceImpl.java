package com.mssd.service.impl;

import com.mssd.dto.ThemeDto;
import com.mssd.dto.ThemeRequestDto;
import com.mssd.exception.ResourceNotFoundException;
import com.mssd.mapper.ThemeMapper;
import com.mssd.model.Theme;
import com.mssd.repository.ThemeRepository;
import com.mssd.service.ThemeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ThemeServiceImpl implements ThemeService {
    
    private final ThemeRepository themeRepository;
    private final ThemeMapper themeMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ThemeDto> getAllThemes() {
        return themeRepository.findAll().stream()
                .map(themeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ThemeDto> getPublishedThemes() {
        return themeRepository.findAllPublishedOrderByName().stream()
                .map(themeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ThemeDto getThemeById(Long id) {
        Theme theme = themeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theme not found with id: " + id));
        return themeMapper.toDto(theme);
    }

    @Override
    @Transactional(readOnly = true)
    public ThemeDto getThemeBySlug(String slug) {
        Theme theme = themeRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Theme not found with slug: " + slug));
        return themeMapper.toDto(theme);
    }

    @Override
    public ThemeDto createTheme(ThemeRequestDto themeRequestDto) {
        if (themeRepository.existsBySlug(themeRequestDto.getSlug())) {
            throw new IllegalArgumentException("Theme with slug '" + themeRequestDto.getSlug() + "' already exists");
        }
        
        Theme theme = themeMapper.toEntity(themeRequestDto);
        Theme savedTheme = themeRepository.save(theme);
        return themeMapper.toDto(savedTheme);
    }

    @Override
    public ThemeDto updateTheme(Long id, ThemeRequestDto themeRequestDto) {
        Theme existingTheme = themeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theme not found with id: " + id));
        
        // Check slug uniqueness (excluding current theme)
        if (!existingTheme.getSlug().equals(themeRequestDto.getSlug()) && 
            themeRepository.existsBySlug(themeRequestDto.getSlug())) {
            throw new IllegalArgumentException("Theme with slug '" + themeRequestDto.getSlug() + "' already exists");
        }
        
        themeMapper.updateEntity(existingTheme, themeRequestDto);
        Theme updatedTheme = themeRepository.save(existingTheme);
        return themeMapper.toDto(updatedTheme);
    }

    @Override
    public void deleteTheme(Long id) {
        if (!themeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Theme not found with id: " + id);
        }
        themeRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsBySlug(String slug) {
        return themeRepository.existsBySlug(slug);
    }
}