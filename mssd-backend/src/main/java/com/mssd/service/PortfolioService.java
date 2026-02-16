package com.mssd.service;

import com.mssd.dto.PortfolioDto;

import java.util.List;

public interface PortfolioService {
    
    List<PortfolioDto> getAllPortfolios();
    
    List<PortfolioDto> getActivePortfolios();
    
    List<PortfolioDto> getPortfoliosByCategory(String category);
    
    List<PortfolioDto> getPortfoliosByFormation(Long formationId);
    
    PortfolioDto getPortfolioById(Long id);
    
    PortfolioDto createPortfolio(PortfolioDto portfolioDto);
    
    PortfolioDto updatePortfolio(Long id, PortfolioDto portfolioDto);
    
    void deletePortfolio(Long id);
    
    void deactivatePortfolio(Long id);
    
    void activatePortfolio(Long id);
}
