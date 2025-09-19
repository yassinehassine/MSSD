package com.mssd.config;

import com.mssd.model.*;
import com.mssd.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SeedDataConfig {
    private final CategoryRepository categoryRepository;
    private final FormationRepository formationRepository;
    private final CompanyRepository companyRepository;
    private final HighlightRepository highlightRepository;
    private final CalendarRepository calendarRepository;

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
                    Formation formation1 = new Formation();
                    formation1.setTitle("Leadership Essentials");
                    formation1.setSlug("leadership-essentials");
                    formation1.setDescription("Learn the basics of leadership.");
                    formation1.setCategory("Management");
                    formation1.setPrice(new BigDecimal("1200.00"));
                    formation1.setDuration("3 days");
                    formation1.setLevel(Formation.Level.BEGINNER);
                    formation1.setPublished(true);
                    
                    Formation formation2 = new Formation();
                    formation2.setTitle("Advanced Sales Techniques");
                    formation2.setSlug("advanced-sales-techniques");
                    formation2.setDescription("Master advanced sales skills.");
                    formation2.setCategory("Sales");
                    formation2.setPrice(new BigDecimal("1500.00"));
                    formation2.setDuration("2 days");
                    formation2.setLevel(Formation.Level.EXPERT);
                    formation2.setPublished(true);
                    
                    formationRepository.saveAll(List.of(formation1, formation2));
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
            
            // Seed Calendar Events
            if (calendarRepository.count() == 0) {
                log.info("Seeding calendar events...");
                LocalDateTime now = LocalDateTime.now();
                calendarRepository.saveAll(List.of(
                    new Calendar(null, "Leadership Workshop", "Learn essential leadership skills in this interactive workshop.", 
                               now.plusDays(7).withHour(9).withMinute(0), now.plusDays(7).withHour(17).withMinute(0), 
                               "Conference Room A", 20, 0, Calendar.CalendarStatus.AVAILABLE, null, null),
                    new Calendar(null, "Sales Training Session", "Advanced sales techniques and strategies for professionals.", 
                               now.plusDays(14).withHour(10).withMinute(0), now.plusDays(14).withHour(16).withMinute(0), 
                               "Training Center", 15, 0, Calendar.CalendarStatus.AVAILABLE, null, null),
                    new Calendar(null, "Team Building Event", "Fun team building activities to strengthen collaboration.", 
                               now.plusDays(21).withHour(14).withMinute(0), now.plusDays(21).withHour(18).withMinute(0), 
                               "Outdoor Garden", 30, 0, Calendar.CalendarStatus.AVAILABLE, null, null),
                    new Calendar(null, "Professional Development Seminar", "Career growth and skill development seminar.", 
                               now.plusDays(30).withHour(9).withMinute(0), now.plusDays(30).withHour(15).withMinute(0), 
                               "Auditorium", 50, 0, Calendar.CalendarStatus.AVAILABLE, null, null)
                ));
                log.info("Calendar events seeded successfully");
            } else {
                log.info("Calendar events already exist, skipping...");
            }
            
            log.info("Database seeding completed successfully!");
                
            } catch (Exception e) {
                log.error("Error during database seeding: ", e);
                throw e;
            }
        };
    }
} 