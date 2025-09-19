package com.mssd.service;

import com.mssd.dto.ThemeDto;
import com.mssd.dto.ThemeRequestDto;

import java.util.List;

public interface ThemeService {
    List<ThemeDto> getAllThemes();
    List<ThemeDto> getPublishedThemes();
    ThemeDto getThemeById(Long id);
    ThemeDto getThemeBySlug(String slug);
    ThemeDto createTheme(ThemeRequestDto themeRequestDto);
    ThemeDto updateTheme(Long id, ThemeRequestDto themeRequestDto);
    void deleteTheme(Long id);
    boolean existsBySlug(String slug);
}