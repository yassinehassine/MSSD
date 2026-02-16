package com.mssd.controller;

import com.mssd.dto.PortfolioDto;
import com.mssd.dto.FormationDto;
import com.mssd.service.PortfolioService;
import com.mssd.service.FormationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/portfolio")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
@Slf4j
public class PortfolioController {

    private final PortfolioService portfolioService;
    private final FormationService formationService;

    @GetMapping
    public ResponseEntity<List<PortfolioDto>> getAllActivePortfolios() {
        try {
            log.debug("Getting all active portfolios");
            List<PortfolioDto> portfolios = portfolioService.getActivePortfolios();
            return ResponseEntity.ok(portfolios);
        } catch (Exception e) {
            log.error("Error getting active portfolios: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/admin")
    public ResponseEntity<List<PortfolioDto>> getAllPortfoliosForAdmin() {
        try {
            log.debug("Getting all portfolios for admin");
            List<PortfolioDto> portfolios = portfolioService.getAllPortfolios();
            return ResponseEntity.ok(portfolios);
        } catch (Exception e) {
            log.error("Error getting portfolios for admin: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PortfolioDto> getPortfolioById(@PathVariable Long id) {
        try {
            log.debug("Getting portfolio by ID: {}", id);
            PortfolioDto portfolio = portfolioService.getPortfolioById(id);
            return ResponseEntity.ok(portfolio);
        } catch (RuntimeException e) {
            log.error("Portfolio not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error getting portfolio by ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<PortfolioDto>> getPortfoliosByCategory(@PathVariable String category) {
        try {
            log.debug("Getting portfolios by category: {}", category);
            List<PortfolioDto> portfolios = portfolioService.getPortfoliosByCategory(category);
            return ResponseEntity.ok(portfolios);
        } catch (Exception e) {
            log.error("Error getting portfolios by category {}: {}", category, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/formation/{formationId}")
    public ResponseEntity<List<PortfolioDto>> getPortfoliosByFormation(@PathVariable Long formationId) {
        try {
            log.debug("Getting portfolios by formation ID: {}", formationId);
            List<PortfolioDto> portfolios = portfolioService.getPortfoliosByFormation(formationId);
            return ResponseEntity.ok(portfolios);
        } catch (Exception e) {
            log.error("Error getting portfolios by formation ID {}: {}", formationId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<PortfolioDto> createPortfolio(@Valid @RequestBody PortfolioDto portfolioDto) {
        try {
            log.debug("Creating portfolio: {}", portfolioDto.getTitle());
            log.debug("Formation ID: {}, Type: {}", portfolioDto.getFormationId(), 
                     portfolioDto.getFormationId() != null ? portfolioDto.getFormationId().getClass().getSimpleName() : "null");
            
            PortfolioDto createdPortfolio = portfolioService.createPortfolio(portfolioDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPortfolio);
        } catch (IllegalArgumentException e) {
            log.error("Validation error creating portfolio: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error creating portfolio: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PortfolioDto> updatePortfolio(@PathVariable Long id, @Valid @RequestBody PortfolioDto portfolioDto) {
        try {
            log.debug("Updating portfolio with ID: {}", id);
            PortfolioDto updatedPortfolio = portfolioService.updatePortfolio(id, portfolioDto);
            return ResponseEntity.ok(updatedPortfolio);
        } catch (IllegalArgumentException e) {
            log.error("Validation error updating portfolio: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            log.error("Portfolio not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error updating portfolio with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePortfolio(@PathVariable Long id) {
        try {
            log.debug("Deleting portfolio with ID: {}", id);
            portfolioService.deletePortfolio(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Portfolio not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error deleting portfolio with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivatePortfolio(@PathVariable Long id) {
        try {
            log.debug("Deactivating portfolio with ID: {}", id);
            portfolioService.deactivatePortfolio(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            log.error("Portfolio not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error deactivating portfolio with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<Void> activatePortfolio(@PathVariable Long id) {
        try {
            log.debug("Activating portfolio with ID: {}", id);
            portfolioService.activatePortfolio(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            log.error("Portfolio not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error activating portfolio with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/formations")
    public ResponseEntity<List<FormationDto>> getFormations() {
        try {
            log.debug("Getting all formations");
            List<FormationDto> formations = formationService.getAllFormations();
            return ResponseEntity.ok(formations);
        } catch (Exception e) {
            log.error("Error getting formations: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

