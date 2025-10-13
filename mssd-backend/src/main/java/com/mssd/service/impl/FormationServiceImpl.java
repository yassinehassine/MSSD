package com.mssd.service.impl;

import com.mssd.dto.FormationDto;
import com.mssd.dto.FormationRequestDto;
import com.mssd.mapper.FormationMapper;
import com.mssd.model.Formation;
import com.mssd.repository.FormationRepository;
import com.mssd.service.FormationService;
import com.mssd.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FormationServiceImpl implements FormationService {
    private final FormationRepository formationRepository;
    private final FormationMapper formationMapper;

    @Override
    public List<FormationDto> getAllFormations() {
        return formationRepository.findAll().stream()
                .map(formationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public FormationDto getFormationById(Long id) {
        Formation formation = formationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Formation not found with id: " + id));
        return formationMapper.toDto(formation);
    }

    @Override
    public FormationDto getFormationBySlug(String slug) {
        Formation formation = formationRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Formation not found with slug: " + slug));
        return formationMapper.toDto(formation);
    }

    @Override
    public List<FormationDto> getFormationsByCategory(String category) {
        return formationRepository.findByCategory(category).stream()
                .map(formationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<FormationDto> getPublishedFormations() {
        return formationRepository.findByPublishedTrue().stream()
                .map(formationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<FormationDto> getFormationsByLevel(String level) {
        try {
            Formation.Level levelEnum = Formation.Level.valueOf(level.toUpperCase());
            return formationRepository.findByLevel(levelEnum).stream()
                    .map(formationMapper::toDto)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid level: " + level + ". Valid levels are: BEGINNER, INTERMEDIATE, EXPERT");
        }
    }

    @Override
    public FormationDto createFormation(FormationRequestDto dto) {
        if (formationRepository.existsBySlug(dto.getSlug())) {
            throw new IllegalArgumentException("Formation with this slug already exists");
        }
        Formation formation = formationMapper.toEntity(dto);
        return formationMapper.toDto(formationRepository.save(formation));
    }

    @Override
    public FormationDto updateFormation(Long id, FormationRequestDto dto) {
        Formation formation = formationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Formation not found with id: " + id));
        formationMapper.updateEntity(formation, dto);
        return formationMapper.toDto(formationRepository.save(formation));
    }

    @Override
    public void deleteFormation(Long id) {
        if (!formationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Formation not found with id: " + id);
        }
        formationRepository.deleteById(id);
    }
} 