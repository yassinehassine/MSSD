package com.mssd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mssd.repository.PortfolioRepository;
import com.mssd.repository.FormationRepository;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
public class HealthController {
    
    @Autowired
    private PortfolioRepository portfolioRepository;
    
    @Autowired
    private FormationRepository formationRepository;
    
    @GetMapping
    public Map<String, Object> healthCheck() {
        Map<String, Object> health = new HashMap<>();
        try {
            health.put("status", "UP");
            health.put("portfolioCount", portfolioRepository.count());
            health.put("formationCount", formationRepository.count());
            health.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            health.put("status", "DOWN");
            health.put("error", e.getMessage());
        }
        return health;
    }
}
