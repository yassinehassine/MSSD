package com.mssd.controller;

import com.mssd.dto.ThemeDto;
import com.mssd.dto.ThemeRequestDto;
import com.mssd.service.ThemeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/themes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ThemeController {
    
    private final ThemeService themeService;

    @GetMapping
    public ResponseEntity<List<ThemeDto>> getAllThemes() {
        List<ThemeDto> themes = themeService.getAllThemes();
        return ResponseEntity.ok(themes);
    }

    @GetMapping("/published")
    public ResponseEntity<List<ThemeDto>> getPublishedThemes() {
        List<ThemeDto> themes = themeService.getPublishedThemes();
        return ResponseEntity.ok(themes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ThemeDto> getThemeById(@PathVariable Long id) {
        ThemeDto theme = themeService.getThemeById(id);
        return ResponseEntity.ok(theme);
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<ThemeDto> getThemeBySlug(@PathVariable String slug) {
        ThemeDto theme = themeService.getThemeBySlug(slug);
        return ResponseEntity.ok(theme);
    }

    @PostMapping
    public ResponseEntity<ThemeDto> createTheme(@Valid @RequestBody ThemeRequestDto themeRequestDto) {
        ThemeDto createdTheme = themeService.createTheme(themeRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTheme);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ThemeDto> updateTheme(
            @PathVariable Long id,
            @Valid @RequestBody ThemeRequestDto themeRequestDto) {
        ThemeDto updatedTheme = themeService.updateTheme(id, themeRequestDto);
        return ResponseEntity.ok(updatedTheme);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        themeService.deleteTheme(id);
        return ResponseEntity.noContent().build();
    }
}