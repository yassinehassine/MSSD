package com.mssd.service;

import com.mssd.model.Company;

public interface CompanyService {
    Company getCompanyInfo();
    Company updateCompanyInfo(Company company);
} 