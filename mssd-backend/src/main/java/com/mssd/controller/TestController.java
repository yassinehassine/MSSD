package com.mssd.controller;

import com.mssd.repository.CategoryRepository;
import com.mssd.repository.CompanyRepository;
import com.mssd.repository.FormationRepository;
import com.mssd.repository.HighlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {
    
    private final CategoryRepository categoryRepository;
    private final FormationRepository formationRepository;
    private final CompanyRepository companyRepository;
    private final HighlightRepository highlightRepository;
    
    @GetMapping("/database")
    public ResponseEntity<Map<String, Object>> testDatabase() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            long categoryCount = categoryRepository.count();
            long formationCount = formationRepository.count();
            long companyCount = companyRepository.count();
            long highlightCount = highlightRepository.count();
            
            result.put("status", "success");
            result.put("message", "Database connection successful");
            result.put("data", Map.of(
                "categories", categoryCount,
                "formations", formationCount,
                "company", companyCount,
                "highlights", highlightCount
            ));
            
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", "Database connection failed: " + e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }
} 