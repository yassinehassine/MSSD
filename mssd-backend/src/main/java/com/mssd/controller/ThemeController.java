package com.mssd.controller;

import com.mssd.dto.ThemeDto;
import com.mssd.dto.ThemeCreateUpdateDto;
import com.mssd.service.ThemeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/themes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ThemeController {
    
    private final ThemeService themeService;
    
    /**
     * Get all active themes
     * GET /api/themes
     */
    @GetMapping
    public ResponseEntity<List<ThemeDto>> getAllThemes() {
        List<ThemeDto> themes = themeService.getAllActiveThemes();
        return ResponseEntity.ok(themes);
    }
    
    /**
     * Get all themes with their formations
     * GET /api/themes/with-formations
     */
    @GetMapping("/with-formations")
    public ResponseEntity<List<ThemeDto>> getThemesWithFormations() {
        List<ThemeDto> themes = themeService.getThemesWithFormations();
        return ResponseEntity.ok(themes);
    }

    /**
     * Get theme with formations by slug
     * GET /api/themes/{slug}/formations
     */
    @GetMapping("/{slug}/formations")
    public ResponseEntity<ThemeDto> getThemeWithFormationsBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(themeService.getThemeWithFormationsBySlug(slug));
    }

    /**
     * Create a new theme
     * POST /api/themes
     */
    @PostMapping
    public ResponseEntity<ThemeDto> createTheme(@RequestBody @jakarta.validation.Valid ThemeCreateUpdateDto dto) {
        return ResponseEntity.ok(themeService.createTheme(dto));
    }

    /**
     * Update existing theme
     * PUT /api/themes/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ThemeDto> updateTheme(@PathVariable Long id, @RequestBody @jakarta.validation.Valid ThemeCreateUpdateDto dto) {
        return ResponseEntity.ok(themeService.updateTheme(id, dto));
    }

    /**
     * Delete a theme (only if no formations)
     * DELETE /api/themes/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        themeService.deleteTheme(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get all themes including inactive ones (Admin only)
     * GET /api/themes/admin
     */
    @GetMapping("/admin")
    public ResponseEntity<List<ThemeDto>> getAllThemesAdmin() {
        List<ThemeDto> themes = themeService.getAllThemesAdmin();
        return ResponseEntity.ok(themes);
    }
}