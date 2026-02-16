package com.mssd.service.impl;

import com.mssd.model.Company;
import com.mssd.repository.CompanyRepository;
import com.mssd.service.CompanyService;
import com.mssd.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;

    @Override
    public Company getCompanyInfo() {
        return companyRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Company info not found"));
    }

    @Override
    public Company updateCompanyInfo(Company company) {
        Company existing = companyRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Company info not found"));
        company.setId(existing.getId());
        return companyRepository.save(company);
    }
} 