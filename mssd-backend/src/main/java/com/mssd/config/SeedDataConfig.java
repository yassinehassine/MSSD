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
    private final ThemeRepository themeRepository;
    private final CompanyRepository companyRepository;
    private final HighlightRepository highlightRepository;
    private final CalendarRepository calendarRepository;

    @Bean
    public CommandLineRunner seedData() {
        return args -> {
            log.info("Starting database seeding...");
            
            try {
                // Seed Categories (legacy support)
                if (categoryRepository.count() == 0) {
                    log.info("Seeding categories...");
                    categoryRepository.saveAll(List.of(
                            new Category(null, "Management", "management"),
                            new Category(null, "Sales", "sales"),
                            new Category(null, "Soft Skills", "soft-skills"),
                            new Category(null, "Development", "development"),
                            new Category(null, "Marketing", "marketing"),
                            new Category(null, "Analytics", "analytics")
                    ));
                    log.info("Categories seeded successfully");
                } else {
                    log.info("Categories already exist, skipping...");
                }
                
                // Seed Themes (new theme-based structure)
                List<Theme> themes = null;
                if (themeRepository.count() == 0) {
                    log.info("Seeding themes...");
                    themes = List.of(
                            createTheme("Développement Web", "developpement-web", 
                                      "Technologies et frameworks pour le développement web moderne", 
                                      null, true),
                            createTheme("Applications Mobiles", "applications-mobiles", 
                                      "Développement d'applications natives et cross-platform", 
                                      null, true),
                            createTheme("Data Science & Analytics", "data-science-analytics", 
                                      "Analyse de données, visualisation et apprentissage automatique", 
                                      null, true),
                            createTheme("Marketing Digital", "marketing-digital", 
                                      "Stratégies de marketing numérique et communication", 
                                      null, true),
                            createTheme("Management & Leadership", "management-leadership", 
                                      "Gestion d'équipe et leadership en entreprise", 
                                      null, true)
                    );
                    themeRepository.saveAll(themes);
                    log.info("Themes seeded successfully");
                } else {
                    log.info("Themes already exist, loading existing themes...");
                    themes = themeRepository.findByActiveTrue();
                }
                
                // Seed Formations with proper theme relationships
                if (formationRepository.count() == 0) {
                    log.info("Seeding formations...");
                    List<Formation> formations = List.of(
                            // Web Development Theme
                            createFormation("Développement Web Fundamentals", "web-development-fundamentals",
                                    "Apprenez les bases du développement web avec HTML, CSS, et JavaScript",
                                    "Development", new BigDecimal("299.99"), "8 semaines", 
                                    Formation.Level.BEGINNER, findThemeBySlug(themes, "developpement-web")),
                            
                            createFormation("React.js Avancé", "react-js-avance",
                                    "Développement d'applications web avec React.js et ses écosystèmes",
                                    "Development", new BigDecimal("449.99"), "10 semaines",
                                    Formation.Level.INTERMEDIATE, findThemeBySlug(themes, "developpement-web")),
                            
                            // Mobile Applications Theme
                            createFormation("Développement d'Applications Mobiles", "mobile-app-development",
                                    "Construisez des applications mobiles natives et cross-platform",
                                    "Development", new BigDecimal("499.99"), "12 semaines",
                                    Formation.Level.INTERMEDIATE, findThemeBySlug(themes, "applications-mobiles")),
                            
                            createFormation("Flutter Development", "flutter-development",
                                    "Création d'applications mobiles cross-platform avec Flutter",
                                    "Development", new BigDecimal("529.99"), "14 semaines",
                                    Formation.Level.INTERMEDIATE, findThemeBySlug(themes, "applications-mobiles")),
                            
                            // Data Science Theme
                            createFormation("Data Science et Analytique", "data-science-analytics",
                                    "Maîtrisez l'analyse de données, la visualisation et l'apprentissage automatique",
                                    "Analytics", new BigDecimal("599.99"), "16 semaines",
                                    Formation.Level.EXPERT, findThemeBySlug(themes, "data-science-analytics")),
                            
                            createFormation("Machine Learning Pratique", "machine-learning-pratique",
                                    "Application pratique du machine learning en entreprise",
                                    "Analytics", new BigDecimal("699.99"), "18 semaines",
                                    Formation.Level.EXPERT, findThemeBySlug(themes, "data-science-analytics")),
                            
                            // Digital Marketing Theme
                            createFormation("Stratégie Marketing Digital", "digital-marketing-strategy",
                                    "Stratégies complètes de marketing numérique et de vente",
                                    "Marketing", new BigDecimal("399.99"), "10 semaines",
                                    Formation.Level.INTERMEDIATE, findThemeBySlug(themes, "marketing-digital")),
                            
                            createFormation("SEO et Content Marketing", "seo-content-marketing",
                                    "Optimisation SEO et stratégies de contenu digital",
                                    "Marketing", new BigDecimal("349.99"), "8 semaines",
                                    Formation.Level.BEGINNER, findThemeBySlug(themes, "marketing-digital")),
                            
                            // Management Theme
                            createFormation("Solutions de Gestion d'Entreprise", "business-management-solutions",
                                    "Services de gestion d'entreprise et de consultation",
                                    "Management", new BigDecimal("799.99"), "20 semaines",
                                    Formation.Level.EXPERT, findThemeBySlug(themes, "management-leadership")),
                            
                            createFormation("Leadership et Gestion d'Équipe", "leadership-gestion-equipe",
                                    "Développement du leadership et management d'équipe",
                                    "Management", new BigDecimal("599.99"), "12 semaines",
                                    Formation.Level.INTERMEDIATE, findThemeBySlug(themes, "management-leadership"))
                    );
                    formationRepository.saveAll(formations);
                    log.info("Formations seeded successfully");
                } else {
                    log.info("Formations already exist, skipping...");
                }
                
                // Seed Company with dynamic content
                if (companyRepository.count() == 0) {
                    log.info("Seeding company data...");
                    Company company = new Company();
                    company.setMission("Nous accompagnons les entreprises dans leur transformation digitale et le développement des compétences de leurs équipes.");
                    company.setVision("Être le partenaire de référence en formation professionnelle et conseil en management.");
                    company.setCompanyValues("Excellence, Innovation, Proximité, Résultats");
                    company.setWhoWeAre("Experts en formation professionnelle et conseil en management, nous proposons des solutions sur mesure pour accompagner votre croissance.");
                    company.setWhyChooseUs("Une approche personnalisée, des formateurs experts, des méthodes pédagogiques innovantes et un suivi post-formation.");
                    company.setMetaDescription("MSSD - Formation professionnelle et conseil en management. Solutions sur mesure pour entreprises.");
                    companyRepository.save(company);
                    log.info("Company data seeded successfully");
                } else {
                    log.info("Company data already exists, skipping...");
                }
                
                // Seed Highlights with dynamic content
                if (highlightRepository.count() == 0) {
                    log.info("Seeding highlights...");
                    highlightRepository.saveAll(List.of(
                            createHighlight("Formations d'Excellence", 
                                    "Développez vos compétences avec nos formations certifiantes", 
                                    "Découvrir nos formations", "/annexes", true),
                            createHighlight("Accompagnement Personnalisé", 
                                    "Des solutions sur mesure adaptées à vos besoins spécifiques", 
                                    "Demander un devis", "/annexes/request", true),
                            createHighlight("Expertise Reconnue", 
                                    "Plus de 10 ans d'expérience et des milliers d'apprenants satisfaits", 
                                    "En savoir plus", "/about", true)
                    ));
                    log.info("Highlights seeded successfully");
                } else {
                    log.info("Highlights already exist, skipping...");
                }
                
                // Seed Calendar Events with realistic training sessions
                if (calendarRepository.count() == 0) {
                    log.info("Seeding calendar events...");
                    LocalDateTime now = LocalDateTime.now();
                    calendarRepository.saveAll(List.of(
                        createCalendarEvent("Atelier Leadership Transformationnel", 
                                "Développez votre leadership et apprenez à inspirer vos équipes dans un environnement en constante évolution.", 
                                now.plusDays(7), 9, 17, "Centre de Formation MSSD", 20),
                        
                        createCalendarEvent("Formation Marketing Digital Intensif", 
                                "Maîtrisez les stratégies digitales : SEO, SEM, réseaux sociaux, content marketing et analytics.", 
                                now.plusDays(14), 9, 16, "Salle de Conférence A", 15),
                        
                        createCalendarEvent("Workshop Développement Web Modern", 
                                "Formation pratique aux dernières technologies web : React, Node.js, et déploiement cloud.", 
                                now.plusDays(21), 10, 18, "Laboratoire Informatique", 12),
                        
                        createCalendarEvent("Séminaire Data Science & IA", 
                                "Introduction pratique à l'analyse de données et aux algorithmes d'intelligence artificielle pour entreprises.", 
                                now.plusDays(28), 9, 15, "Auditorium Principal", 25),
                        
                        createCalendarEvent("Formation Gestion de Projet Agile", 
                                "Méthodes agiles, Scrum, et outils de collaboration pour optimiser la gestion de vos projets.", 
                                now.plusDays(35), 8, 16, "Salle de Formation B", 18),
                        
                        createCalendarEvent("Masterclass Communication Commerciale", 
                                "Techniques avancées de vente, négociation et relation client pour booster vos performances commerciales.", 
                                now.plusDays(42), 14, 17, "Centre d'Affaires", 30)
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
    
    // Helper Methods for Dynamic Content Creation
    
    private Theme createTheme(String name, String slug, String description, String iconUrl, boolean active) {
        Theme theme = new Theme();
        theme.setName(name);
        theme.setSlug(slug);
        theme.setDescription(description);
        theme.setIconUrl(iconUrl);
        theme.setActive(active);
        return theme;
    }
    
    private Formation createFormation(String title, String slug, String description, String category, 
                                    BigDecimal price, String duration, Formation.Level level, Theme theme) {
        Formation formation = new Formation();
        formation.setTitle(title);
        formation.setSlug(slug);
        formation.setDescription(description);
        formation.setCategory(category); // Keep for backward compatibility
        formation.setPrice(price);
        formation.setDuration(duration);
        formation.setLevel(level);
        formation.setPublished(true);
        formation.setTheme(theme);
        return formation;
    }
    
    private Highlight createHighlight(String title, String subtitle, String buttonText, String buttonUrl, boolean active) {
        Highlight highlight = new Highlight();
        highlight.setTitle(title);
        highlight.setSubtitle(subtitle);
        // Map to existing fields: ctaText, ctaLink, visible
        highlight.setCtaText(buttonText);
        highlight.setCtaLink(buttonUrl);
        highlight.setVisible(active);
        return highlight;
    }
    
    private Calendar createCalendarEvent(String title, String description, LocalDateTime baseDate, 
                                       int startHour, int endHour, String location, int capacity) {
        Calendar event = new Calendar();
        event.setTitle(title);
        event.setDescription(description);
        // Adjust to field names: startTime, endTime
        event.setStartTime(baseDate.withHour(startHour).withMinute(0).withSecond(0));
        event.setEndTime(baseDate.withHour(endHour).withMinute(0).withSecond(0));
        event.setLocation(location);
        event.setMaxCapacity(capacity);
        event.setCurrentCapacity(0);
        event.setStatus(Calendar.CalendarStatus.AVAILABLE);
        return event;
    }
    
    private Theme findThemeBySlug(List<Theme> themes, String slug) {
        return themes.stream()
                .filter(theme -> theme.getSlug().equals(slug))
                .findFirst()
                .orElse(null);
    }
} 