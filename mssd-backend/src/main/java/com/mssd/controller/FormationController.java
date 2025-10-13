package com.mssd.controller;

import com.mssd.dto.FormationDto;
import com.mssd.dto.FormationRequestDto;
import com.mssd.service.FormationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/formations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FormationController {
    private final FormationService formationService;

    /**
     * Get all formations
     * GET /api/formations
     */
    @GetMapping
    public ResponseEntity<List<FormationDto>> getAllFormations() {
        List<FormationDto> formations = formationService.getAllFormations();
        return ResponseEntity.ok(formations);
    }

    /**
     * Get formation by ID
     * GET /api/formations/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<FormationDto> getFormationById(@PathVariable Long id) {
        FormationDto formation = formationService.getFormationById(id);
        return ResponseEntity.ok(formation);
    }

    /**
     * Get formation by slug
     * GET /api/formations/slug/{slug}
     */
    @GetMapping("/slug/{slug}")
    public ResponseEntity<FormationDto> getFormationBySlug(@PathVariable String slug) {
        FormationDto formation = formationService.getFormationBySlug(slug);
        return ResponseEntity.ok(formation);
    }

    /**
     * Get formations by category
     * GET /api/formations/category/{category}
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<FormationDto>> getFormationsByCategory(@PathVariable String category) {
        List<FormationDto> formations = formationService.getFormationsByCategory(category);
        return ResponseEntity.ok(formations);
    }

    /**
     * Create a new formation
     * POST /api/formations
     */
    @PostMapping
    public ResponseEntity<FormationDto> createFormation(@Valid @RequestBody FormationRequestDto formationRequestDto) {
        FormationDto createdFormation = formationService.createFormation(formationRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFormation);
    }

    /**
     * Update an existing formation
     * PUT /api/formations/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<FormationDto> updateFormation(
            @PathVariable Long id,
            @Valid @RequestBody FormationRequestDto formationRequestDto) {
        FormationDto updatedFormation = formationService.updateFormation(id, formationRequestDto);
        return ResponseEntity.ok(updatedFormation);
    }

    /**
     * Delete a formation
     * DELETE /api/formations/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFormation(@PathVariable Long id) {
        formationService.deleteFormation(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get published formations only
     * GET /api/formations/published
     */
    @GetMapping("/published")
    public ResponseEntity<List<FormationDto>> getPublishedFormations() {
        List<FormationDto> publishedFormations = formationService.getPublishedFormations();
        return ResponseEntity.ok(publishedFormations);
    }

    /**
     * Get formations by level
     * GET /api/formations/level/{level}
     */
    @GetMapping("/level/{level}")
    public ResponseEntity<List<FormationDto>> getFormationsByLevel(@PathVariable String level) {
        List<FormationDto> filteredFormations = formationService.getFormationsByLevel(level);
        return ResponseEntity.ok(filteredFormations);
    }
} 