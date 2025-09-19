package com.mssd.mapper;

import com.mssd.dto.PortfolioDto;
import com.mssd.model.Portfolio;
import com.mssd.model.Formation;
import com.mssd.repository.FormationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PortfolioMapper {

    private final FormationRepository formationRepository;

    public PortfolioDto toDto(Portfolio portfolio) {
        if (portfolio == null) {
            return null;
        }
        
        return PortfolioDto.builder()
            .id(portfolio.getId())
            .title(portfolio.getTitle())
            .description(portfolio.getDescription())
            .formationId(portfolio.getFormation() != null ? portfolio.getFormation().getId() : null)
            .formationName(portfolio.getFormation() != null ? portfolio.getFormation().getTitle() : null)
            .formationCategory(portfolio.getFormation() != null ? portfolio.getFormation().getCategory() : null)
            .category(portfolio.getCategory())
            .imageUrl(portfolio.getImageUrl())
            .companyLogo(portfolio.getCompanyLogo())
            .clientName(portfolio.getClientName())
            .projectDate(portfolio.getProjectDate())
            .projectUrl(portfolio.getProjectUrl())
            .active(portfolio.getActive())
            .createdAt(portfolio.getCreatedAt())
            .updatedAt(portfolio.getUpdatedAt())
            .build();
    }

    public Portfolio toEntity(PortfolioDto dto) {
        if (dto == null) {
            return null;
        }
        
        Portfolio portfolio = new Portfolio();
        // Only set ID if it's not null (for updates)
        if (dto.getId() != null) {
            portfolio.setId(dto.getId());
        }
        portfolio.setTitle(dto.getTitle());
        portfolio.setDescription(dto.getDescription());
        
        // Set formation if formationId is provided
        if (dto.getFormationId() != null) {
            Formation formation = formationRepository.findById(dto.getFormationId())
                .orElseThrow(() -> new RuntimeException("Formation not found with id: " + dto.getFormationId()));
            portfolio.setFormation(formation);
        }
        
        portfolio.setImageUrl(dto.getImageUrl());
        portfolio.setCompanyLogo(dto.getCompanyLogo());
        portfolio.setClientName(dto.getClientName());
        portfolio.setProjectDate(dto.getProjectDate());
        portfolio.setProjectUrl(dto.getProjectUrl());
        portfolio.setCategory(dto.getCategory() != null ? dto.getCategory() : "General");
        portfolio.setActive(dto.getActive() != null ? dto.getActive() : true);
        
        // Don't set created/updated dates here - let the service handle them
        if (dto.getCreatedAt() != null) {
            portfolio.setCreatedAt(dto.getCreatedAt());
        }
        if (dto.getUpdatedAt() != null) {
            portfolio.setUpdatedAt(dto.getUpdatedAt());
        }
        
        return portfolio;
    }
}
