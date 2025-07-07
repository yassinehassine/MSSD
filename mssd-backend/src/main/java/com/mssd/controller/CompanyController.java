package com.mssd.controller;

import com.mssd.model.Company;
import com.mssd.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;

    @GetMapping
    public Company getCompanyInfo() {
        return companyService.getCompanyInfo();
    }

    @PutMapping
    public ResponseEntity<Company> updateCompanyInfo(@Valid @RequestBody Company company) {
        return ResponseEntity.ok(companyService.updateCompanyInfo(company));
    }
} 