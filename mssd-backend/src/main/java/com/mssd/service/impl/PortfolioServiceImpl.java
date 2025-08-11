package com.mssd.service.impl;

import com.mssd.dto.PortfolioDto;
import com.mssd.mapper.PortfolioMapper;
import com.mssd.model.Portfolio;
import com.mssd.model.Formation;
import com.mssd.repository.PortfolioRepository;
import com.mssd.repository.FormationRepository;
import com.mssd.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PortfolioServiceImpl implements PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final FormationRepository formationRepository;
    private final PortfolioMapper portfolioMapper;

    @Override
    @Transactional(readOnly = true)
    public List<PortfolioDto> getAllPortfolios() {
        log.debug("Fetching all portfolios");
        try {
            List<Portfolio> portfolios = portfolioRepository.findAllPortfoliosWithFormation();
            return portfolios.stream()
                    .map(portfolioMapper::toDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching all portfolios: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch portfolios", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<PortfolioDto> getActivePortfolios() {
        log.debug("Fetching active portfolios");
        try {
            List<Portfolio> portfolios = portfolioRepository.findActivePortfoliosWithFormation();
            return portfolios.stream()
                    .map(portfolioMapper::toDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching active portfolios: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch active portfolios", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<PortfolioDto> getPortfoliosByCategory(String category) {
        log.debug("Fetching portfolios by category: {}", category);
        try {
            List<Portfolio> portfolios = portfolioRepository.findByFormationCategoryAndActiveTrue(category);
            return portfolios.stream()
                    .map(portfolioMapper::toDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching portfolios by category {}: {}", category, e.getMessage(), e);
            throw new RuntimeException("Failed to fetch portfolios by category", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<PortfolioDto> getPortfoliosByFormation(Long formationId) {
        log.debug("Fetching portfolios by formation ID: {}", formationId);
        try {
            List<Portfolio> portfolios = portfolioRepository.findByFormationIdAndActiveTrue(formationId);
            return portfolios.stream()
                    .map(portfolioMapper::toDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching portfolios by formation ID {}: {}", formationId, e.getMessage(), e);
            throw new RuntimeException("Failed to fetch portfolios by formation", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PortfolioDto getPortfolioById(Long id) {
        log.debug("Fetching portfolio by ID: {}", id);
        try {
            Portfolio portfolio = portfolioRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Portfolio not found with id: " + id));
            return portfolioMapper.toDto(portfolio);
        } catch (Exception e) {
            log.error("Error fetching portfolio by ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to fetch portfolio", e);
        }
    }

    @Override
    public PortfolioDto createPortfolio(PortfolioDto portfolioDto) {
        log.debug("Creating portfolio: {}", portfolioDto.getTitle());
        try {
            // Validate required fields
            validatePortfolioDto(portfolioDto);
            
            // Verify formation exists
            Formation formation = formationRepository.findById(portfolioDto.getFormationId())
                    .orElseThrow(() -> new IllegalArgumentException("Formation not found with id: " + portfolioDto.getFormationId()));
            
            // Set category from formation if not provided
            if (portfolioDto.getCategory() == null || portfolioDto.getCategory().trim().isEmpty()) {
                portfolioDto.setCategory(formation.getCategory());
            }
            
            // Convert DTO to entity
            Portfolio portfolio = portfolioMapper.toEntity(portfolioDto);
            portfolio.setCreatedAt(LocalDateTime.now());
            portfolio.setUpdatedAt(LocalDateTime.now());
            
            // Ensure active is set
            if (portfolio.getActive() == null) {
                portfolio.setActive(true);
            }
            
            // Ensure category is set
            if (portfolio.getCategory() == null || portfolio.getCategory().trim().isEmpty()) {
                portfolio.setCategory(formation.getCategory());
            }
            
            // Save portfolio
            Portfolio savedPortfolio = portfolioRepository.save(portfolio);
            log.info("Portfolio created successfully with ID: {}", savedPortfolio.getId());
            
            return portfolioMapper.toDto(savedPortfolio);
        } catch (IllegalArgumentException e) {
            log.error("Validation error creating portfolio: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error creating portfolio: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create portfolio", e);
        }
    }

    @Override
    public PortfolioDto updatePortfolio(Long id, PortfolioDto portfolioDto) {
        log.debug("Updating portfolio with ID: {}", id);
        try {
            // Find existing portfolio
            Portfolio existingPortfolio = portfolioRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Portfolio not found with id: " + id));
            
            // Validate required fields
            validatePortfolioDto(portfolioDto);
            
            // Verify formation exists if formationId is provided
            if (portfolioDto.getFormationId() != null) {
                Formation formation = formationRepository.findById(portfolioDto.getFormationId())
                        .orElseThrow(() -> new IllegalArgumentException("Formation not found with id: " + portfolioDto.getFormationId()));
                existingPortfolio.setFormation(formation);
            }
            
            // Update fields
            existingPortfolio.setTitle(portfolioDto.getTitle());
            existingPortfolio.setDescription(portfolioDto.getDescription());
            existingPortfolio.setImageUrl(portfolioDto.getImageUrl());
            existingPortfolio.setClientName(portfolioDto.getClientName());
            existingPortfolio.setProjectDate(portfolioDto.getProjectDate());
            existingPortfolio.setProjectUrl(portfolioDto.getProjectUrl());
            
            // Update category if provided, otherwise use formation category
            if (portfolioDto.getCategory() != null && !portfolioDto.getCategory().trim().isEmpty()) {
                existingPortfolio.setCategory(portfolioDto.getCategory());
            } else if (portfolioDto.getFormationId() != null) {
                Formation formation = formationRepository.findById(portfolioDto.getFormationId())
                        .orElseThrow(() -> new IllegalArgumentException("Formation not found with id: " + portfolioDto.getFormationId()));
                existingPortfolio.setCategory(formation.getCategory());
            }
            
            if (portfolioDto.getActive() != null) {
                existingPortfolio.setActive(portfolioDto.getActive());
            }
            
            existingPortfolio.setUpdatedAt(LocalDateTime.now());
            
            // Save updated portfolio
            Portfolio updatedPortfolio = portfolioRepository.save(existingPortfolio);
            log.info("Portfolio updated successfully with ID: {}", updatedPortfolio.getId());
            
            return portfolioMapper.toDto(updatedPortfolio);
        } catch (IllegalArgumentException e) {
            log.error("Validation error updating portfolio: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error updating portfolio with ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to update portfolio", e);
        }
    }

    @Override
    public void deletePortfolio(Long id) {
        log.debug("Deleting portfolio with ID: {}", id);
        try {
            if (!portfolioRepository.existsById(id)) {
                throw new RuntimeException("Portfolio not found with id: " + id);
            }
            portfolioRepository.deleteById(id);
            log.info("Portfolio deleted successfully with ID: {}", id);
        } catch (Exception e) {
            log.error("Error deleting portfolio with ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to delete portfolio", e);
        }
    }

    @Override
    public void deactivatePortfolio(Long id) {
        log.debug("Deactivating portfolio with ID: {}", id);
        try {
            Portfolio portfolio = portfolioRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Portfolio not found with id: " + id));
            portfolio.setActive(false);
            portfolio.setUpdatedAt(LocalDateTime.now());
            portfolioRepository.save(portfolio);
            log.info("Portfolio deactivated successfully with ID: {}", id);
        } catch (Exception e) {
            log.error("Error deactivating portfolio with ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to deactivate portfolio", e);
        }
    }

    @Override
    public void activatePortfolio(Long id) {
        log.debug("Activating portfolio with ID: {}", id);
        try {
            Portfolio portfolio = portfolioRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Portfolio not found with id: " + id));
            portfolio.setActive(true);
            portfolio.setUpdatedAt(LocalDateTime.now());
            portfolioRepository.save(portfolio);
            log.info("Portfolio activated successfully with ID: {}", id);
        } catch (Exception e) {
            log.error("Error activating portfolio with ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to activate portfolio", e);
        }
    }

    private void validatePortfolioDto(PortfolioDto portfolioDto) {
        if (portfolioDto == null) {
            throw new IllegalArgumentException("Portfolio data is required");
        }
        if (portfolioDto.getTitle() == null || portfolioDto.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Portfolio title is required");
        }
        if (portfolioDto.getFormationId() == null) {
            throw new IllegalArgumentException("Formation ID is required");
        }
    }
}
