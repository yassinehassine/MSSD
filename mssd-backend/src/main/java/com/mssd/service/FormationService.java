package com.mssd.service;

import com.mssd.dto.FormationDto;
import com.mssd.dto.FormationRequestDto;

import java.util.List;

public interface FormationService {
    List<FormationDto> getAllFormations();
    FormationDto getFormationById(Long id);
    FormationDto getFormationBySlug(String slug);
    List<FormationDto> getFormationsByCategory(String category);
    List<FormationDto> getPublishedFormations();
    List<FormationDto> getFormationsByLevel(String level);
    FormationDto createFormation(FormationRequestDto dto);
    FormationDto updateFormation(Long id, FormationRequestDto dto);
    void deleteFormation(Long id);
} 