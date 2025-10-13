package com.mssd.controller;

import com.mssd.model.Theme;
import com.mssd.repository.ThemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/debug")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DebugController {
    
    private final ThemeRepository themeRepository;
    
    /**
     * Debug endpoint to check themes in database
     */
    @GetMapping("/themes")
    public ResponseEntity<Map<String, Object>> debugThemes() {
        Map<String, Object> result = new HashMap<>();
        
        // Get all themes (including inactive)
        List<Theme> allThemes = themeRepository.findAll();
        result.put("totalThemes", allThemes.size());
        result.put("allThemes", allThemes);
        
        // Get active themes only
        List<Theme> activeThemes = themeRepository.findByActiveTrue();
        result.put("activeThemes", activeThemes.size());
        result.put("activeThemesList", activeThemes);
        
        // Get themes with formations
        List<Theme> themesWithFormations = themeRepository.findActiveThemesWithFormations();
        result.put("themesWithFormations", themesWithFormations.size());
        result.put("themesWithFormationsList", themesWithFormations);
        
        return ResponseEntity.ok(result);
    }
}