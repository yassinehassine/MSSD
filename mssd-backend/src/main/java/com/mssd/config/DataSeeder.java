package com.mssd.config;

import com.mssd.model.*;
import com.mssd.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Database Seeder for MSSD Training Platform
 * Populates the database with clean, professional data
 * 
 * Usage:
 * - Activate with profile: spring.profiles.active=seed
 * - Or call manually from a controller/service
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataSeeder {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Bean
    @Profile("seed") // Only runs when 'seed' profile is active
    public CommandLineRunner seedDatabase(
            ThemeRepository themeRepository,
            FormationRepository formationRepository,
            BlogRepository blogRepository,
            PortfolioRepository portfolioRepository,
            CalendarRepository calendarRepository,
            UserRepository userRepository,
            ReviewRepository reviewRepository,
            CompanyRepository companyRepository,
            HighlightRepository highlightRepository,
            ContactRepository contactRepository,
            NewsletterRepository newsletterRepository
    ) {
        return args -> {
            log.info("🌱 Starting database seeding...");

            // Clear existing data (optional - remove if you want to preserve data)
            clearDatabase(themeRepository, formationRepository, blogRepository, 
                         portfolioRepository, calendarRepository, userRepository,
                         reviewRepository, companyRepository, highlightRepository,
                         contactRepository, newsletterRepository);

            // Seed in order of dependencies
            seedUsers(userRepository);
            seedCompany(companyRepository);
            seedHighlights(highlightRepository);
            List<Theme> themes = seedThemes(themeRepository);
            List<Formation> formations = seedFormations(formationRepository, themes);
            seedReviews(reviewRepository, formations);
            seedPortfolio(portfolioRepository, formations);
            seedCalendar(calendarRepository, formations);
            seedBlogs(blogRepository);
            seedNewsletters(newsletterRepository);

            log.info("✅ Database seeding completed successfully!");
        };
    }

    private void clearDatabase(
            ThemeRepository themeRepository,
            FormationRepository formationRepository,
            BlogRepository blogRepository,
            PortfolioRepository portfolioRepository,
            CalendarRepository calendarRepository,
            UserRepository userRepository,
            ReviewRepository reviewRepository,
            CompanyRepository companyRepository,
            HighlightRepository highlightRepository,
            ContactRepository contactRepository,
            NewsletterRepository newsletterRepository
    ) {
        log.info("🗑️  Clearing existing data...");
        
        reviewRepository.deleteAll();
        portfolioRepository.deleteAll();
        calendarRepository.deleteAll();
        formationRepository.deleteAll();
        themeRepository.deleteAll();
        blogRepository.deleteAll();
        contactRepository.deleteAll();
        newsletterRepository.deleteAll();
        highlightRepository.deleteAll();
        companyRepository.deleteAll();
        userRepository.deleteAll();
        
        log.info("✓ Database cleared");
    }

    private void seedUsers(UserRepository userRepository) {
        log.info("👤 Seeding users...");

        List<User> users = Arrays.asList(
                createUser("admin", "admin@mssd.com", "admin123", User.Role.ADMIN),
                createUser("manager", "manager@mssd.com", "manager123", User.Role.USER),
                createUser("instructor", "instructor@mssd.com", "instructor123", User.Role.USER)
        );

        userRepository.saveAll(users);
        log.info("✓ Created {} users", users.size());
    }

    private User createUser(String username, String email, String rawPassword, User.Role role) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(role);
        user.setCreatedAt(LocalDateTime.now());
        return user;
    }

    private void seedCompany(CompanyRepository companyRepository) {
        log.info("🏢 Seeding company information...");

        Company company = new Company();
        company.setMission("Notre mission est de développer les compétences et le potentiel de chaque professionnel pour créer des organisations performantes et épanouissantes.");
        company.setVision("Devenir le partenaire de référence en formation professionnelle et conseil stratégique en France et en Europe.");
        company.setCompanyValues("Excellence, Innovation, Engagement, Bienveillance, Résultats");
        company.setWhoWeAre("MSSD Training & Consulting est un leader en formation professionnelle avec plus de 15 ans d'expérience. Nous accompagnons les entreprises et les individus dans leur développement des compétences à travers des formations certifiantes de haute qualité.");
        company.setWhyChooseUs("Une expertise reconnue, des formateurs certifiés, des méthodes pédagogiques innovantes, un suivi personnalisé, et un taux de satisfaction client de 95%.");
        company.setMetaDescription("MSSD Training - Leader en formation professionnelle et conseil stratégique. Formations certifiantes en management, communication, vente, RH et gestion de projet.");

        companyRepository.save(company);
        log.info("✓ Company information created");
    }

    private void seedHighlights(HighlightRepository highlightRepository) {
        log.info("⭐ Seeding highlights...");

        List<Highlight> highlights = Arrays.asList(
                createHighlight("Formation Professionnelle", 
                    "Des formations certifiantes adaptées à vos besoins", 
                    "En savoir plus", "/formations",
                    "/assets/images/highlights/formation.jpg"),
                createHighlight("Expertise Reconnue", 
                    "Plus de 15 ans d'expérience dans la formation", 
                    "Notre approche", "/about",
                    "/assets/images/highlights/expertise.jpg"),
                createHighlight("Accompagnement Personnalisé", 
                    "Un suivi sur mesure pour chaque apprenant", 
                    "Contactez-nous", "/contact",
                    "/assets/images/highlights/accompagnement.jpg"),
                createHighlight("Résultats Garantis", 
                    "Taux de satisfaction de 95% de nos clients", 
                    "Nos réalisations", "/portfolio",
                    "/assets/images/highlights/resultats.jpg")
        );

        highlightRepository.saveAll(highlights);
        log.info("✓ Created {} highlights", highlights.size());
    }

    private Highlight createHighlight(String title, String subtitle, String ctaText, 
                                     String ctaLink, String imageUrl) {
        Highlight highlight = new Highlight();
        highlight.setTitle(title);
        highlight.setSubtitle(subtitle);
        highlight.setCtaText(ctaText);
        highlight.setCtaLink(ctaLink);
        highlight.setImageUrl(imageUrl);
        highlight.setVisible(true);
        return highlight;
    }

    private List<Theme> seedThemes(ThemeRepository themeRepository) {
        log.info("🎨 Seeding themes...");

        List<Theme> themes = Arrays.asList(
                createTheme("Management & Leadership", "management-leadership",
                    "Développez vos compétences en management et leadership d'équipe",
                    "/assets/icons/management.svg"),
                createTheme("Communication & Relation Client", "communication-relation-client",
                    "Maîtrisez l'art de la communication professionnelle",
                    "/assets/icons/communication.svg"),
                createTheme("Développement Personnel", "developpement-personnel",
                    "Renforcez votre efficacité personnelle et professionnelle",
                    "/assets/icons/personal-dev.svg"),
                createTheme("Vente & Négociation", "vente-negociation",
                    "Optimisez vos techniques de vente et de négociation",
                    "/assets/icons/sales.svg"),
                createTheme("Ressources Humaines", "ressources-humaines",
                    "Expertise en gestion RH et développement des talents",
                    "/assets/icons/hr.svg"),
                createTheme("Gestion de Projet", "gestion-projet",
                    "Pilotez vos projets avec méthode et efficacité",
                    "/assets/icons/project.svg")
        );

        List<Theme> savedThemes = themeRepository.saveAll(themes);
        log.info("✓ Created {} themes", savedThemes.size());
        return savedThemes;
    }

    private Theme createTheme(String name, String slug, String description, String iconUrl) {
        Theme theme = new Theme();
        theme.setName(name);
        theme.setSlug(slug);
        theme.setDescription(description);
        theme.setIconUrl(iconUrl);
        theme.setActive(true);
        theme.setPublished(true);
        return theme;
    }

    private List<Formation> seedFormations(FormationRepository formationRepository, List<Theme> themes) {
        log.info("📚 Seeding formations...");

        Theme management = themes.get(0);
        Theme communication = themes.get(1);
        Theme devPersonnel = themes.get(2);
        Theme vente = themes.get(3);
        Theme rh = themes.get(4);
        Theme projet = themes.get(5);

        List<Formation> formations = Arrays.asList(
                // Management & Leadership
                createFormation("Management d'Équipe - Les Fondamentaux", "management-equipe-fondamentaux",
                    "Acquérez les compétences essentielles pour manager une équipe avec efficacité. Cette formation couvre les bases du leadership, la communication managériale, et la gestion des performances.",
                    management, new BigDecimal("1500.00"), "3 jours", Formation.Level.BEGINNER),
                
                createFormation("Leadership Stratégique", "leadership-strategique",
                    "Développez votre vision stratégique et votre capacité à inspirer et mobiliser vos équipes vers l'excellence. Formation avancée pour managers expérimentés.",
                    management, new BigDecimal("2500.00"), "5 jours", Formation.Level.EXPERT),
                
                createFormation("Management Agile", "management-agile",
                    "Maîtrisez les méthodes agiles de management et apprenez à piloter des équipes dans un environnement en constante évolution.",
                    management, new BigDecimal("2000.00"), "4 jours", Formation.Level.INTERMEDIATE),

                // Communication & Relation Client
                createFormation("Communication Interpersonnelle", "communication-interpersonnelle",
                    "Développez vos compétences en communication pour améliorer vos relations professionnelles et personnelles. Techniques de PNL et d'écoute active.",
                    communication, new BigDecimal("1200.00"), "2 jours", Formation.Level.BEGINNER),
                
                createFormation("Excellence en Relation Client", "excellence-relation-client",
                    "Transformez votre service client en avantage concurrentiel. Gestion des réclamations, fidélisation, et satisfaction client.",
                    communication, new BigDecimal("1800.00"), "3 jours", Formation.Level.INTERMEDIATE),
                
                createFormation("Prise de Parole en Public", "prise-parole-public",
                    "Maîtrisez l'art de parler en public avec aisance et impact. Techniques de présentation, gestion du trac, et storytelling.",
                    communication, new BigDecimal("1500.00"), "2 jours", Formation.Level.INTERMEDIATE),

                // Développement Personnel
                createFormation("Gestion du Temps et des Priorités", "gestion-temps-priorites",
                    "Optimisez votre organisation personnelle et professionnelle. Méthodes éprouvées pour gagner en efficacité et réduire le stress.",
                    devPersonnel, new BigDecimal("900.00"), "2 jours", Formation.Level.BEGINNER),
                
                createFormation("Intelligence Émotionnelle", "intelligence-emotionnelle",
                    "Développez votre QE pour mieux comprendre et gérer vos émotions et celles des autres. Clé de la réussite professionnelle.",
                    devPersonnel, new BigDecimal("1600.00"), "3 jours", Formation.Level.INTERMEDIATE),
                
                createFormation("Gestion du Stress et Bien-être", "gestion-stress-bien-etre",
                    "Apprenez à gérer votre stress et à maintenir un équilibre vie professionnelle/vie personnelle sain.",
                    devPersonnel, new BigDecimal("1100.00"), "2 jours", Formation.Level.BEGINNER),

                // Vente & Négociation
                createFormation("Techniques de Vente Avancées", "techniques-vente-avancees",
                    "Maîtrisez les techniques de vente modernes pour augmenter votre taux de closing. Approche consultative et vente de valeur.",
                    vente, new BigDecimal("2200.00"), "4 jours", Formation.Level.INTERMEDIATE),
                
                createFormation("Négociation Commerciale", "negociation-commerciale",
                    "Développez vos compétences en négociation pour obtenir les meilleurs accords. Stratégies gagnant-gagnant et techniques d'influence.",
                    vente, new BigDecimal("1800.00"), "3 jours", Formation.Level.INTERMEDIATE),
                
                createFormation("Prospection et Développement Commercial", "prospection-developpement-commercial",
                    "Construisez un pipeline commercial solide. Techniques de prospection multicanal et gestion de la relation commerciale.",
                    vente, new BigDecimal("1500.00"), "3 jours", Formation.Level.BEGINNER),

                // Ressources Humaines
                createFormation("Recrutement et Intégration", "recrutement-integration",
                    "Maîtrisez le processus de recrutement de A à Z. Techniques d'entretien, assessment et onboarding réussi.",
                    rh, new BigDecimal("1700.00"), "3 jours", Formation.Level.INTERMEDIATE),
                
                createFormation("Gestion des Talents et des Compétences", "gestion-talents-competences",
                    "Développez une stratégie RH efficace pour attirer, développer et retenir les talents. GPEC et plans de carrière.",
                    rh, new BigDecimal("2000.00"), "4 jours", Formation.Level.EXPERT),
                
                createFormation("Droit du Travail pour Managers", "droit-travail-managers",
                    "Connaissances essentielles du droit du travail pour managers. Contrats, discipline, et gestion des situations délicates.",
                    rh, new BigDecimal("1400.00"), "2 jours", Formation.Level.INTERMEDIATE),

                // Gestion de Projet
                createFormation("Gestion de Projet - Fondamentaux", "gestion-projet-fondamentaux",
                    "Maîtrisez les bases de la gestion de projet. Planification, suivi, et clôture de projets avec succès.",
                    projet, new BigDecimal("1600.00"), "3 jours", Formation.Level.BEGINNER),
                
                createFormation("Certification PMP Preparation", "certification-pmp-preparation",
                    "Préparez-vous à l'examen de certification PMP du PMI. Formation intensive couvrant les 5 groupes de processus.",
                    projet, new BigDecimal("3500.00"), "10 jours", Formation.Level.EXPERT),
                
                createFormation("Méthode Scrum pour Chefs de Projet", "methode-scrum-chefs-projet",
                    "Adoptez Scrum pour gérer vos projets de manière agile. Rôles, événements, et artefacts Scrum en pratique.",
                    projet, new BigDecimal("1800.00"), "3 jours", Formation.Level.INTERMEDIATE)
        );

        List<Formation> savedFormations = formationRepository.saveAll(formations);
        log.info("✓ Created {} formations", savedFormations.size());
        return savedFormations;
    }

    private Formation createFormation(String title, String slug, String description, 
                                     Theme theme, BigDecimal price, String duration, 
                                     Formation.Level level) {
        Formation formation = new Formation();
        formation.setTitle(title);
        formation.setSlug(slug);
        formation.setDescription(description);
        formation.setCategory(theme.getName()); // Legacy field
        formation.setTheme(theme);
        formation.setPrice(price);
        formation.setDuration(duration);
        formation.setImageUrl("/assets/images/formations/" + slug + ".jpg");
        formation.setLevel(level);
        formation.setPublished(true);
        return formation;
    }

    private void seedReviews(ReviewRepository reviewRepository, List<Formation> formations) {
        log.info("⭐ Seeding reviews...");

        List<Review> reviews = Arrays.asList(
                createReview(formations.get(0), "Marie Dubois", 5,
                    "Formation exceptionnelle ! Les concepts sont clairement expliqués et directement applicables. Je recommande vivement."),
                
                createReview(formations.get(0), "Jean Martin", 4,
                    "Très bonne formation avec des exemples concrets. Le formateur est très pédagogue."),
                
                createReview(formations.get(1), "Sophie Laurent", 5,
                    "Une formation de haut niveau qui m'a permis de développer ma vision stratégique. Excellente expérience !"),
                
                createReview(formations.get(3), "Pierre Durand", 5,
                    "J'ai appris des techniques de communication que j'utilise quotidiennement. Merci !"),
                
                createReview(formations.get(4), "Claire Bernard", 4,
                    "Formation pratique et interactive. Les outils fournis sont très utiles."),
                
                createReview(formations.get(6), "Thomas Petit", 5,
                    "Excellente formation sur la gestion du temps. J'ai gagné en efficacité dès la première semaine !"),
                
                createReview(formations.get(9), "Isabelle Robert", 5,
                    "Formation très complète sur les techniques de vente. Mon taux de closing a augmenté de 30% !"),
                
                createReview(formations.get(12), "Marc Moreau", 4,
                    "Très bon contenu RH avec des cas pratiques pertinents. Formateur expérimenté.")
        );

        reviewRepository.saveAll(reviews);
        log.info("✓ Created {} reviews", reviews.size());
    }

    private Review createReview(Formation formation, String authorName, int rating, String comment) {
        return Review.builder()
                .formation(formation)
                .authorName(authorName)
                .rating(rating)
                .comment(comment)
                .createdAt(LocalDateTime.now().minusDays((long) (Math.random() * 90)))
                .build();
    }

    private void seedPortfolio(PortfolioRepository portfolioRepository, List<Formation> formations) {
        log.info("💼 Seeding portfolio...");

        List<Portfolio> portfolios = Arrays.asList(
                createPortfolio("Transformation Digitale - Groupe Total",
                    "Accompagnement de 50 managers dans leur transition vers le management agile et digital.",
                    formations.get(2), "Total Energies", LocalDate.of(2023, 9, 15)),
                
                createPortfolio("Programme Leadership - BNP Paribas",
                    "Formation de 30 cadres supérieurs aux pratiques de leadership stratégique moderne.",
                    formations.get(1), "BNP Paribas", LocalDate.of(2023, 6, 20)),
                
                createPortfolio("Excellence Client - Orange",
                    "Formation de 200 conseillers clientèle aux techniques d'excellence relationnelle.",
                    formations.get(4), "Orange", LocalDate.of(2023, 11, 10)),
                
                createPortfolio("Développement Commercial - Renault",
                    "Programme intensif pour 80 commerciaux sur les techniques de vente consultative.",
                    formations.get(9), "Renault Group", LocalDate.of(2023, 4, 5)),
                
                createPortfolio("Gestion de Projet Agile - Airbus",
                    "Certification Scrum Master pour 40 chefs de projet dans l'aéronautique.",
                    formations.get(17), "Airbus", LocalDate.of(2023, 8, 22)),
                
                createPortfolio("Management d'Équipe - SNCF",
                    "Formation de 100 managers de proximité aux fondamentaux du management.",
                    formations.get(0), "SNCF", LocalDate.of(2023, 3, 12)),
                
                createPortfolio("RH Stratégique - L'Oréal",
                    "Accompagnement de l'équipe RH dans la mise en place d'une GPEC efficace.",
                    formations.get(13), "L'Oréal", LocalDate.of(2023, 10, 18)),
                
                createPortfolio("Communication Interpersonnelle - Carrefour",
                    "Amélioration des compétences relationnelles de 150 collaborateurs.",
                    formations.get(3), "Carrefour", LocalDate.of(2023, 5, 30))
        );

        portfolioRepository.saveAll(portfolios);
        log.info("✓ Created {} portfolio items", portfolios.size());
    }

    private Portfolio createPortfolio(String title, String description, Formation formation,
                                     String clientName, LocalDate projectDate) {
        return Portfolio.builder()
                .title(title)
                .description(description)
                .formation(formation)
                .clientName(clientName)
                .projectDate(projectDate)
                .imageUrl("/assets/images/portfolio/" + clientName.toLowerCase().replaceAll(" ", "-") + ".jpg")
                .companyLogoUrl("/assets/images/clients/" + clientName.toLowerCase().replaceAll(" ", "-") + "-logo.png")
                .category("Formation Entreprise")
                .active(true)
                .build();
    }

    private void seedCalendar(CalendarRepository calendarRepository, List<Formation> formations) {
        log.info("📅 Seeding calendar events...");

        LocalDateTime now = LocalDateTime.now();

        List<Calendar> events = Arrays.asList(
                createCalendarEvent("Management d'Équipe - Session Mars", 
                    formations.get(0),
                    now.plusDays(15),
                    now.plusDays(18),
                    "Paris", 15),
                
                createCalendarEvent("Leadership Stratégique - Session Avril",
                    formations.get(1),
                    now.plusDays(30),
                    now.plusDays(35),
                    "Lyon", 12),
                
                createCalendarEvent("Communication Interpersonnelle - Session Mars",
                    formations.get(3),
                    now.plusDays(20),
                    now.plusDays(22),
                    "Paris", 20),
                
                createCalendarEvent("Techniques de Vente - Session Avril",
                    formations.get(9),
                    now.plusDays(40),
                    now.plusDays(44),
                    "Marseille", 18),
                
                createCalendarEvent("Gestion du Temps - Session Mars",
                    formations.get(6),
                    now.plusDays(10),
                    now.plusDays(12),
                    "Paris", 25),
                
                createCalendarEvent("Scrum Master - Session Mai",
                    formations.get(17),
                    now.plusDays(50),
                    now.plusDays(53),
                    "Toulouse", 16),
                
                createCalendarEvent("Excellence Relation Client - Session Avril",
                    formations.get(4),
                    now.plusDays(35),
                    now.plusDays(38),
                    "Bordeaux", 20),
                
                createCalendarEvent("Management Agile - Session Mai",
                    formations.get(2),
                    now.plusDays(55),
                    now.plusDays(59),
                    "Paris", 14)
        );

        calendarRepository.saveAll(events);
        log.info("✓ Created {} calendar events", events.size());
    }

    private Calendar createCalendarEvent(String title, Formation formation, 
                                        LocalDateTime startTime, LocalDateTime endTime,
                                        String location, int maxCapacity) {
        Calendar event = new Calendar();
        event.setTitle(title);
        event.setDescription("Session de formation : " + formation.getTitle());
        event.setStartTime(startTime);
        event.setEndTime(endTime);
        event.setLocation(location);
        event.setMaxCapacity(maxCapacity);
        event.setCurrentCapacity((int) (Math.random() * 5)); // Random current capacity 0-4
        event.setStatus(Calendar.CalendarStatus.AVAILABLE);
        return event;
    }

    private void seedBlogs(BlogRepository blogRepository) {
        log.info("📝 Seeding blog posts...");

        List<Blog> blogs = Arrays.asList(
                createBlog("Les 5 Compétences Clés du Manager de Demain",
                    "Le monde du travail évolue rapidement. Découvrez les compétences essentielles que tout manager doit développer pour réussir dans l'environnement professionnel moderne : intelligence émotionnelle, agilité, vision stratégique, capacité d'innovation et leadership inclusif.",
                    "https://www.youtube.com/watch?v=example1",
                    LocalDateTime.now().minusDays(5)),
                
                createBlog("Comment Améliorer sa Communication Interpersonnelle",
                    "La communication est au cœur de toute relation professionnelle réussie. Dans cet article, nous explorons les techniques de communication efficace : écoute active, communication non-violente, langage corporel et feedback constructif. Apprenez à transformer vos interactions professionnelles.",
                    "https://www.youtube.com/watch?v=example2",
                    LocalDateTime.now().minusDays(12)),
                
                createBlog("L'Intelligence Émotionnelle en Entreprise",
                    "L'intelligence émotionnelle est devenue une compétence cruciale dans le monde professionnel. Découvrez comment développer votre QE pour améliorer vos relations, votre leadership et votre efficacité au travail. Études de cas et exercices pratiques inclus.",
                    "https://www.youtube.com/watch?v=example3",
                    LocalDateTime.now().minusDays(18)),
                
                createBlog("Techniques de Vente pour 2024",
                    "Le monde de la vente a considérablement évolué. Exit les techniques agressives, place à la vente consultative et à l'approche centrée sur la valeur client. Découvrez les méthodes qui fonctionnent vraiment en 2024 : social selling, storytelling, et création de valeur.",
                    "https://www.youtube.com/watch?v=example4",
                    LocalDateTime.now().minusDays(25)),
                
                createBlog("Gestion du Stress : Guide Pratique",
                    "Le stress est l'un des défis majeurs du monde professionnel moderne. Ce guide complet vous propose des techniques éprouvées pour mieux gérer votre stress : respiration, mindfulness, organisation, et équilibre vie pro/perso. Transformez votre rapport au stress.",
                    "https://www.youtube.com/watch?v=example5",
                    LocalDateTime.now().minusDays(30)),
                
                createBlog("Leadership Agile : Principes et Pratiques",
                    "Le leadership agile révolutionne la façon dont nous manageons les équipes. Apprenez les principes fondamentaux de l'agilité appliqués au management : empowerment, collaboration, amélioration continue et adaptabilité. Cas d'entreprises qui ont réussi leur transformation.",
                    "https://www.youtube.com/watch?v=example6",
                    LocalDateTime.now().minusDays(40)),
                
                createBlog("Recrutement : Les Erreurs à Éviter",
                    "Un mauvais recrutement coûte cher à l'entreprise. Découvrez les erreurs les plus courantes en recrutement et comment les éviter : biais cognitifs, questions inadaptées, processus trop long, et mauvaise intégration. Conseils d'experts RH pour recruter efficacement.",
                    null,
                    LocalDateTime.now().minusDays(45)),
                
                createBlog("Gestion de Projet : Méthodologies Comparées",
                    "Waterfall, Agile, Scrum, Kanban, Lean... Il existe de nombreuses méthodologies de gestion de projet. Ce guide comparatif vous aide à choisir la méthode la plus adaptée à votre contexte, votre équipe et vos objectifs. Avantages et inconvénients de chaque approche.",
                    "https://www.youtube.com/watch?v=example7",
                    LocalDateTime.now().minusDays(50)),
                
                createBlog("L'Art de la Négociation Commerciale",
                    "La négociation est un art qui s'apprend et se perfectionne. Découvrez les stratégies gagnantes pour négocier avec succès : préparation, écoute, création de valeur, et techniques d'influence. Cas pratiques et exercices pour devenir un négociateur hors pair.",
                    null,
                    LocalDateTime.now().minusDays(55)),
                
                createBlog("Développer son Personal Branding",
                    "Votre marque personnelle est votre atout le plus précieux dans le monde professionnel moderne. Apprenez à construire et développer votre personal branding : identité professionnelle, présence en ligne, networking, et storytelling personnel.",
                    "https://www.youtube.com/watch?v=example8",
                    LocalDateTime.now().minusDays(60))
        );

        blogRepository.saveAll(blogs);
        log.info("✓ Created {} blog posts", blogs.size());
    }

    private Blog createBlog(String title, String description, String youtubeUrl, LocalDateTime publishDate) {
        Blog blog = new Blog();
        blog.setTitle(title);
        blog.setDescription(description);
        blog.setYoutubeUrl(youtubeUrl);
        blog.setImageUrl("/assets/images/blog/" + title.toLowerCase()
                .replaceAll("[^a-z0-9\\s]", "")
                .replaceAll("\\s+", "-") + ".jpg");
        blog.setPublishDate(publishDate);
        blog.setActive(true);
        return blog;
    }

    private void seedNewsletters(NewsletterRepository newsletterRepository) {
        log.info("📧 Seeding newsletter subscriptions...");

        List<Newsletter> newsletters = Arrays.asList(
                createNewsletter("jean.dupont@example.com"),
                createNewsletter("marie.martin@example.com"),
                createNewsletter("pierre.bernard@example.com"),
                createNewsletter("sophie.dubois@example.com"),
                createNewsletter("lucas.thomas@example.com")
        );

        newsletterRepository.saveAll(newsletters);
        log.info("✓ Created {} newsletter subscriptions", newsletters.size());
    }

    private Newsletter createNewsletter(String email) {
        Newsletter newsletter = new Newsletter();
        newsletter.setEmail(email);
        newsletter.setDateSubscribed(LocalDateTime.now().minusDays((long) (Math.random() * 30)));
        return newsletter;
    }
}
