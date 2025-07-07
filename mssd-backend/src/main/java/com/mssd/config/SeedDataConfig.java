package com.mssd.config;

import com.mssd.model.*;
import com.mssd.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SeedDataConfig {
    private final CategoryRepository categoryRepository;
    private final FormationRepository formationRepository;
    private final CompanyRepository companyRepository;
    private final HighlightRepository highlightRepository;

    @Bean
    public CommandLineRunner seedData() {
        return args -> {
            log.info("Starting database seeding...");
            
            try {
                // Seed Categories
                if (categoryRepository.count() == 0) {
                    log.info("Seeding categories...");
                    categoryRepository.saveAll(List.of(
                            new Category(null, "Management", "management"),
                            new Category(null, "Sales", "sales"),
                            new Category(null, "Soft Skills", "soft-skills")
                    ));
                    log.info("Categories seeded successfully");
                } else {
                    log.info("Categories already exist, skipping...");
                }
                
                // Seed Formations
                if (formationRepository.count() == 0) {
                    log.info("Seeding formations...");
                    formationRepository.saveAll(List.of(
                            new Formation(null, "Leadership Essentials", "leadership-essentials", "Learn the basics of leadership.", "Management", new BigDecimal("1200.00"), "3 days", null, Formation.Level.BEGINNER, true, null, null),
                            new Formation(null, "Advanced Sales Techniques", "advanced-sales-techniques", "Master advanced sales skills.", "Sales", new BigDecimal("1500.00"), "2 days", null, Formation.Level.EXPERT, true, null, null)
                    ));
                    log.info("Formations seeded successfully");
                } else {
                    log.info("Formations already exist, skipping...");
                }
                
                // Seed Company
                if (companyRepository.count() == 0) {
                    log.info("Seeding company data...");
                    Company company = new Company();
                    company.setMission("Our mission is to empower professionals.");
                    company.setVision("Our vision is to be the leader in training.");
                    company.setCompanyValues("Integrity, Excellence, Innovation");
                    company.setWhoWeAre("We are a team of experts.");
                    company.setWhyChooseUs("Because we deliver results.");
                    company.setMetaDescription("MSSD professional training and consulting");
                    companyRepository.save(company);
                    log.info("Company data seeded successfully");
                } else {
                    log.info("Company data already exists, skipping...");
                }
                
                // Seed Highlights
                if (highlightRepository.count() == 0) {
                    log.info("Seeding highlights...");
                    highlightRepository.saveAll(List.of(
                            new Highlight(null, "Welcome to MSSD", "Your partner in growth", "See Formations", "/formations", null, true),
                            new Highlight(null, "Why Choose Us?", "Expert trainers and real results", "Contact Us", "/contact", null, true)
                    ));
                    log.info("Highlights seeded successfully");
                } else {
                    log.info("Highlights already exist, skipping...");
                }
                
                log.info("Database seeding completed successfully!");
                
            } catch (Exception e) {
                log.error("Error during database seeding: ", e);
                throw e;
            }
        };
    }
} 