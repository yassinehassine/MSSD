package com.mssd.controller;

import com.mssd.dto.ThemeCreateUpdateDto;
import com.mssd.model.Theme;
import com.mssd.repository.ThemeRepository;
import com.mssd.service.ThemeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/fix")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FixController {
    
    private final ThemeRepository themeRepository;
    private final ThemeService themeService;
    
    /**
     * Fix themes to make sure they are active and have proper data
     */
    @PostMapping("/themes")
    public ResponseEntity<Map<String, Object>> fixThemes() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // Get all themes
            List<Theme> allThemes = themeRepository.findAll();
            result.put("totalThemesBefore", allThemes.size());
            
            // Create missing themes if needed
            if (allThemes.isEmpty()) {
                // Create default themes
                Theme softSkills = new Theme();
                softSkills.setName("Soft Skills");
                softSkills.setSlug("soft-skills");
                softSkills.setDescription("Compétences interpersonnelles et savoir-être");
                softSkills.setActive(true);
                
                Theme qualite = new Theme();
                qualite.setName("Qualité & Certification");
                qualite.setSlug("qualite-certification");
                qualite.setDescription("Standards de qualité et processus de certification");
                qualite.setActive(true);
                
                Theme marketing = new Theme();
                marketing.setName("Marketing Digital");
                marketing.setSlug("marketing-digital");
                marketing.setDescription("Stratégies de marketing numérique");
                marketing.setActive(true);
                
                themeRepository.saveAll(List.of(softSkills, qualite, marketing));
                result.put("themesCreated", 3);
            } else {
                // Make sure existing themes are active
                int updatedCount = 0;
                for (Theme theme : allThemes) {
                    if (!theme.isActive()) {
                        theme.setActive(true);
                        themeRepository.save(theme);
                        updatedCount++;
                    }
                    // Fix slug if empty
                    if (theme.getSlug() == null || theme.getSlug().isEmpty()) {
                        String slug = theme.getName().toLowerCase()
                                .replaceAll("[^a-z0-9\\s-]", "")
                                .replaceAll("\\s+", "-")
                                .trim();
                        theme.setSlug(slug);
                        themeRepository.save(theme);
                        updatedCount++;
                    }
                }
                result.put("themesUpdated", updatedCount);
            }
            
            // Final count
            List<Theme> finalThemes = themeRepository.findAll();
            List<Theme> activeThemes = themeRepository.findByActiveTrue();
            
            result.put("totalThemesAfter", finalThemes.size());
            result.put("activeThemes", activeThemes.size());
            result.put("success", true);
            result.put("message", "Themes fixed successfully");
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        return ResponseEntity.ok(result);
    }
}