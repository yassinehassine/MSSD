package com.mssd.controller;

import com.mssd.dto.PortfolioItemDto;
import com.mssd.service.PortfolioItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/portfolio-items")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PortfolioItemController {
    
    private final PortfolioItemService portfolioItemService;
    
    @GetMapping
    public ResponseEntity<List<PortfolioItemDto>> getAllPortfolioItems() {
        List<PortfolioItemDto> items = portfolioItemService.getAllPortfolioItems();
        return ResponseEntity.ok(items);
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<PortfolioItemDto>> getActivePortfolioItems() {
        List<PortfolioItemDto> items = portfolioItemService.getAllActivePortfolioItems();
        return ResponseEntity.ok(items);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PortfolioItemDto> getPortfolioItemById(@PathVariable Long id) {
        return portfolioItemService.getPortfolioItemById(id)
                .map(item -> ResponseEntity.ok(item))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<PortfolioItemDto> createPortfolioItem(@RequestBody PortfolioItemDto dto) {
        PortfolioItemDto created = portfolioItemService.createPortfolioItem(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<PortfolioItemDto> updatePortfolioItem(@PathVariable Long id, @RequestBody PortfolioItemDto dto) {
        try {
            PortfolioItemDto updated = portfolioItemService.updatePortfolioItem(id, dto);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePortfolioItem(@PathVariable Long id) {
        portfolioItemService.deletePortfolioItem(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/upload-logo")
    public ResponseEntity<Map<String, String>> uploadLogo(@RequestParam("file") MultipartFile file) {
        try {
            String filename = portfolioItemService.uploadLogo(file);
            Map<String, String> response = new HashMap<>();
            response.put("url", filename);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}