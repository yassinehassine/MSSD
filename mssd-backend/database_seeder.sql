-- ============================================================================
-- MSSD Training Platform - Database Seeder
-- Clean and Professional Data for Development/Testing
-- ============================================================================
-- Version: 1.0
-- Author: MSSD Development Team
-- Description: This script populates the database with realistic training data
-- ============================================================================

-- Clear existing data (CAUTION: This will delete all data!)
SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE calendar_reservations;
TRUNCATE TABLE reviews;
TRUNCATE TABLE portfolio;
TRUNCATE TABLE calendars;
TRUNCATE TABLE formations;
TRUNCATE TABLE themes;
TRUNCATE TABLE blog;
TRUNCATE TABLE contacts;
TRUNCATE TABLE newsletters;
TRUNCATE TABLE annex_requests;
TRUNCATE TABLE highlights;
TRUNCATE TABLE company;
TRUNCATE TABLE users;

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================================
-- 1. USERS
-- ============================================================================
-- Password: admin123, manager123, instructor123 (BCrypt hashed)
INSERT INTO users (username, email, password, role, created_at) VALUES
('admin', 'admin@mssd.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN', NOW()),
('manager', 'manager@mssd.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'USER', NOW()),
('instructor', 'instructor@mssd.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'USER', NOW());

-- ============================================================================
-- 2. COMPANY INFORMATION
-- ============================================================================
INSERT INTO company (mission, vision, company_values, who_we_are, why_choose_us, meta_description) VALUES
('Notre mission est de développer les compétences et le potentiel de chaque professionnel pour créer des organisations performantes et épanouissantes.',
 'Devenir le partenaire de référence en formation professionnelle et conseil stratégique en France et en Europe.',
 'Excellence, Innovation, Engagement, Bienveillance, Résultats',
 'MSSD Training & Consulting est un leader en formation professionnelle avec plus de 15 ans d\'expérience. Nous accompagnons les entreprises et les individus dans leur développement des compétences à travers des formations certifiantes de haute qualité.',
 'Une expertise reconnue, des formateurs certifiés, des méthodes pédagogiques innovantes, un suivi personnalisé, et un taux de satisfaction client de 95%.',
 'MSSD Training - Leader en formation professionnelle et conseil stratégique. Formations certifiantes en management, communication, vente, RH et gestion de projet.');

-- ============================================================================
-- 3. HIGHLIGHTS
-- ============================================================================
INSERT INTO highlights (title, subtitle, cta_text, cta_link, image_url, visible) VALUES
('Formation Professionnelle', 'Des formations certifiantes adaptées à vos besoins', 'En savoir plus', '/formations', '/assets/images/highlights/formation.jpg', true),
('Expertise Reconnue', 'Plus de 15 ans d\'expérience dans la formation', 'Notre approche', '/about', '/assets/images/highlights/expertise.jpg', true),
('Accompagnement Personnalisé', 'Un suivi sur mesure pour chaque apprenant', 'Contactez-nous', '/contact', '/assets/images/highlights/accompagnement.jpg', true),
('Résultats Garantis', 'Taux de satisfaction de 95% de nos clients', 'Nos réalisations', '/portfolio', '/assets/images/highlights/resultats.jpg', true);

-- ============================================================================
-- 4. THEMES
-- ============================================================================
INSERT INTO themes (name, slug, description, icon_url, active, published) VALUES
('Management & Leadership', 'management-leadership', 
 'Développez vos compétences en management et leadership d\'équipe',
 '/assets/icons/management.svg', true, true),

('Communication & Relation Client', 'communication-relation-client',
 'Maîtrisez l\'art de la communication professionnelle',
 '/assets/icons/communication.svg', true, true),

('Développement Personnel', 'developpement-personnel',
 'Renforcez votre efficacité personnelle et professionnelle',
 '/assets/icons/personal-dev.svg', true, true),

('Vente & Négociation', 'vente-negociation',
 'Optimisez vos techniques de vente et de négociation',
 '/assets/icons/sales.svg', true, true),

('Ressources Humaines', 'ressources-humaines',
 'Expertise en gestion RH et développement des talents',
 '/assets/icons/hr.svg', true, true),

('Gestion de Projet', 'gestion-projet',
 'Pilotez vos projets avec méthode et efficacité',
 '/assets/icons/project.svg', true, true);

-- ============================================================================
-- 5. FORMATIONS
-- ============================================================================

-- Management & Leadership (theme_id = 1)
INSERT INTO formations (title, slug, description, category, theme_id, price, duration, image_url, level, published, created_at, updated_at) VALUES
('Management d\'Équipe - Les Fondamentaux', 'management-equipe-fondamentaux',
 'Acquérez les compétences essentielles pour manager une équipe avec efficacité. Cette formation couvre les bases du leadership, la communication managériale, et la gestion des performances.',
 'Management & Leadership', 1, 1500.00, '3 jours', '/assets/images/formations/management-equipe-fondamentaux.jpg', 'BEGINNER', true, NOW(), NOW()),

('Leadership Stratégique', 'leadership-strategique',
 'Développez votre vision stratégique et votre capacité à inspirer et mobiliser vos équipes vers l\'excellence. Formation avancée pour managers expérimentés.',
 'Management & Leadership', 1, 2500.00, '5 jours', '/assets/images/formations/leadership-strategique.jpg', 'EXPERT', true, NOW(), NOW()),

('Management Agile', 'management-agile',
 'Maîtrisez les méthodes agiles de management et apprenez à piloter des équipes dans un environnement en constante évolution.',
 'Management & Leadership', 1, 2000.00, '4 jours', '/assets/images/formations/management-agile.jpg', 'INTERMEDIATE', true, NOW(), NOW());

-- Communication & Relation Client (theme_id = 2)
INSERT INTO formations (title, slug, description, category, theme_id, price, duration, image_url, level, published, created_at, updated_at) VALUES
('Communication Interpersonnelle', 'communication-interpersonnelle',
 'Développez vos compétences en communication pour améliorer vos relations professionnelles et personnelles. Techniques de PNL et d\'écoute active.',
 'Communication & Relation Client', 2, 1200.00, '2 jours', '/assets/images/formations/communication-interpersonnelle.jpg', 'BEGINNER', true, NOW(), NOW()),

('Excellence en Relation Client', 'excellence-relation-client',
 'Transformez votre service client en avantage concurrentiel. Gestion des réclamations, fidélisation, et satisfaction client.',
 'Communication & Relation Client', 2, 1800.00, '3 jours', '/assets/images/formations/excellence-relation-client.jpg', 'INTERMEDIATE', true, NOW(), NOW()),

('Prise de Parole en Public', 'prise-parole-public',
 'Maîtrisez l\'art de parler en public avec aisance et impact. Techniques de présentation, gestion du trac, et storytelling.',
 'Communication & Relation Client', 2, 1500.00, '2 jours', '/assets/images/formations/prise-parole-public.jpg', 'INTERMEDIATE', true, NOW(), NOW());

-- Développement Personnel (theme_id = 3)
INSERT INTO formations (title, slug, description, category, theme_id, price, duration, image_url, level, published, created_at, updated_at) VALUES
('Gestion du Temps et des Priorités', 'gestion-temps-priorites',
 'Optimisez votre organisation personnelle et professionnelle. Méthodes éprouvées pour gagner en efficacité et réduire le stress.',
 'Développement Personnel', 3, 900.00, '2 jours', '/assets/images/formations/gestion-temps-priorites.jpg', 'BEGINNER', true, NOW(), NOW()),

('Intelligence Émotionnelle', 'intelligence-emotionnelle',
 'Développez votre QE pour mieux comprendre et gérer vos émotions et celles des autres. Clé de la réussite professionnelle.',
 'Développement Personnel', 3, 1600.00, '3 jours', '/assets/images/formations/intelligence-emotionnelle.jpg', 'INTERMEDIATE', true, NOW(), NOW()),

('Gestion du Stress et Bien-être', 'gestion-stress-bien-etre',
 'Apprenez à gérer votre stress et à maintenir un équilibre vie professionnelle/vie personnelle sain.',
 'Développement Personnel', 3, 1100.00, '2 jours', '/assets/images/formations/gestion-stress-bien-etre.jpg', 'BEGINNER', true, NOW(), NOW());

-- Vente & Négociation (theme_id = 4)
INSERT INTO formations (title, slug, description, category, theme_id, price, duration, image_url, level, published, created_at, updated_at) VALUES
('Techniques de Vente Avancées', 'techniques-vente-avancees',
 'Maîtrisez les techniques de vente modernes pour augmenter votre taux de closing. Approche consultative et vente de valeur.',
 'Vente & Négociation', 4, 2200.00, '4 jours', '/assets/images/formations/techniques-vente-avancees.jpg', 'INTERMEDIATE', true, NOW(), NOW()),

('Négociation Commerciale', 'negociation-commerciale',
 'Développez vos compétences en négociation pour obtenir les meilleurs accords. Stratégies gagnant-gagnant et techniques d\'influence.',
 'Vente & Négociation', 4, 1800.00, '3 jours', '/assets/images/formations/negociation-commerciale.jpg', 'INTERMEDIATE', true, NOW(), NOW()),

('Prospection et Développement Commercial', 'prospection-developpement-commercial',
 'Construisez un pipeline commercial solide. Techniques de prospection multicanal et gestion de la relation commerciale.',
 'Vente & Négociation', 4, 1500.00, '3 jours', '/assets/images/formations/prospection-developpement-commercial.jpg', 'BEGINNER', true, NOW(), NOW());

-- Ressources Humaines (theme_id = 5)
INSERT INTO formations (title, slug, description, category, theme_id, price, duration, image_url, level, published, created_at, updated_at) VALUES
('Recrutement et Intégration', 'recrutement-integration',
 'Maîtrisez le processus de recrutement de A à Z. Techniques d\'entretien, assessment et onboarding réussi.',
 'Ressources Humaines', 5, 1700.00, '3 jours', '/assets/images/formations/recrutement-integration.jpg', 'INTERMEDIATE', true, NOW(), NOW()),

('Gestion des Talents et des Compétences', 'gestion-talents-competences',
 'Développez une stratégie RH efficace pour attirer, développer et retenir les talents. GPEC et plans de carrière.',
 'Ressources Humaines', 5, 2000.00, '4 jours', '/assets/images/formations/gestion-talents-competences.jpg', 'EXPERT', true, NOW(), NOW()),

('Droit du Travail pour Managers', 'droit-travail-managers',
 'Connaissances essentielles du droit du travail pour managers. Contrats, discipline, et gestion des situations délicates.',
 'Ressources Humaines', 5, 1400.00, '2 jours', '/assets/images/formations/droit-travail-managers.jpg', 'INTERMEDIATE', true, NOW(), NOW());

-- Gestion de Projet (theme_id = 6)
INSERT INTO formations (title, slug, description, category, theme_id, price, duration, image_url, level, published, created_at, updated_at) VALUES
('Gestion de Projet - Fondamentaux', 'gestion-projet-fondamentaux',
 'Maîtrisez les bases de la gestion de projet. Planification, suivi, et clôture de projets avec succès.',
 'Gestion de Projet', 6, 1600.00, '3 jours', '/assets/images/formations/gestion-projet-fondamentaux.jpg', 'BEGINNER', true, NOW(), NOW()),

('Certification PMP Preparation', 'certification-pmp-preparation',
 'Préparez-vous à l\'examen de certification PMP du PMI. Formation intensive couvrant les 5 groupes de processus.',
 'Gestion de Projet', 6, 3500.00, '10 jours', '/assets/images/formations/certification-pmp-preparation.jpg', 'EXPERT', true, NOW(), NOW()),

('Méthode Scrum pour Chefs de Projet', 'methode-scrum-chefs-projet',
 'Adoptez Scrum pour gérer vos projets de manière agile. Rôles, événements, et artefacts Scrum en pratique.',
 'Gestion de Projet', 6, 1800.00, '3 jours', '/assets/images/formations/methode-scrum-chefs-projet.jpg', 'INTERMEDIATE', true, NOW(), NOW());

-- ============================================================================
-- 6. REVIEWS
-- ============================================================================
INSERT INTO reviews (formation_id, author_name, rating, comment, created_at) VALUES
(1, 'Marie Dubois', 5, 
 'Formation exceptionnelle ! Les concepts sont clairement expliqués et directement applicables. Je recommande vivement.', 
 DATE_SUB(NOW(), INTERVAL 15 DAY)),

(1, 'Jean Martin', 4,
 'Très bonne formation avec des exemples concrets. Le formateur est très pédagogue.',
 DATE_SUB(NOW(), INTERVAL 20 DAY)),

(2, 'Sophie Laurent', 5,
 'Une formation de haut niveau qui m\'a permis de développer ma vision stratégique. Excellente expérience !',
 DATE_SUB(NOW(), INTERVAL 25 DAY)),

(4, 'Pierre Durand', 5,
 'J\'ai appris des techniques de communication que j\'utilise quotidiennement. Merci !',
 DATE_SUB(NOW(), INTERVAL 30 DAY)),

(5, 'Claire Bernard', 4,
 'Formation pratique et interactive. Les outils fournis sont très utiles.',
 DATE_SUB(NOW(), INTERVAL 35 DAY)),

(7, 'Thomas Petit', 5,
 'Excellente formation sur la gestion du temps. J\'ai gagné en efficacité dès la première semaine !',
 DATE_SUB(NOW(), INTERVAL 40 DAY)),

(10, 'Isabelle Robert', 5,
 'Formation très complète sur les techniques de vente. Mon taux de closing a augmenté de 30% !',
 DATE_SUB(NOW(), INTERVAL 45 DAY)),

(13, 'Marc Moreau', 4,
 'Très bon contenu RH avec des cas pratiques pertinents. Formateur expérimenté.',
 DATE_SUB(NOW(), INTERVAL 50 DAY));

-- ============================================================================
-- 7. PORTFOLIO
-- ============================================================================
INSERT INTO portfolio (title, description, formation_id, client_name, project_date, image_url, company_logo_url, category, active, created_at, updated_at) VALUES
('Transformation Digitale - Groupe Total',
 'Accompagnement de 50 managers dans leur transition vers le management agile et digital.',
 3, 'Total Energies', '2023-09-15', 
 '/assets/images/portfolio/total-energies.jpg',
 '/assets/images/clients/total-energies-logo.png',
 'Formation Entreprise', true, NOW(), NOW()),

('Programme Leadership - BNP Paribas',
 'Formation de 30 cadres supérieurs aux pratiques de leadership stratégique moderne.',
 2, 'BNP Paribas', '2023-06-20',
 '/assets/images/portfolio/bnp-paribas.jpg',
 '/assets/images/clients/bnp-paribas-logo.png',
 'Formation Entreprise', true, NOW(), NOW()),

('Excellence Client - Orange',
 'Formation de 200 conseillers clientèle aux techniques d\'excellence relationnelle.',
 5, 'Orange', '2023-11-10',
 '/assets/images/portfolio/orange.jpg',
 '/assets/images/clients/orange-logo.png',
 'Formation Entreprise', true, NOW(), NOW()),

('Développement Commercial - Renault',
 'Programme intensif pour 80 commerciaux sur les techniques de vente consultative.',
 10, 'Renault Group', '2023-04-05',
 '/assets/images/portfolio/renault-group.jpg',
 '/assets/images/clients/renault-group-logo.png',
 'Formation Entreprise', true, NOW(), NOW()),

('Gestion de Projet Agile - Airbus',
 'Certification Scrum Master pour 40 chefs de projet dans l\'aéronautique.',
 18, 'Airbus', '2023-08-22',
 '/assets/images/portfolio/airbus.jpg',
 '/assets/images/clients/airbus-logo.png',
 'Formation Entreprise', true, NOW(), NOW()),

('Management d\'Équipe - SNCF',
 'Formation de 100 managers de proximité aux fondamentaux du management.',
 1, 'SNCF', '2023-03-12',
 '/assets/images/portfolio/sncf.jpg',
 '/assets/images/clients/sncf-logo.png',
 'Formation Entreprise', true, NOW(), NOW()),

('RH Stratégique - L\'Oréal',
 'Accompagnement de l\'équipe RH dans la mise en place d\'une GPEC efficace.',
 14, 'L\'Oréal', '2023-10-18',
 '/assets/images/portfolio/loreal.jpg',
 '/assets/images/clients/loreal-logo.png',
 'Formation Entreprise', true, NOW(), NOW()),

('Communication Interpersonnelle - Carrefour',
 'Amélioration des compétences relationnelles de 150 collaborateurs.',
 4, 'Carrefour', '2023-05-30',
 '/assets/images/portfolio/carrefour.jpg',
 '/assets/images/clients/carrefour-logo.png',
 'Formation Entreprise', true, NOW(), NOW());

-- ============================================================================
-- 8. CALENDAR EVENTS
-- ============================================================================
INSERT INTO calendars (title, description, start_time, end_time, location, max_capacity, current_capacity, status, created_at, updated_at) VALUES
('Management d\'Équipe - Session Mars',
 'Session de formation : Management d\'Équipe - Les Fondamentaux',
 DATE_ADD(NOW(), INTERVAL 15 DAY),
 DATE_ADD(NOW(), INTERVAL 18 DAY),
 'Paris', 15, 3, 'AVAILABLE', NOW(), NOW()),

('Leadership Stratégique - Session Avril',
 'Session de formation : Leadership Stratégique',
 DATE_ADD(NOW(), INTERVAL 30 DAY),
 DATE_ADD(NOW(), INTERVAL 35 DAY),
 'Lyon', 12, 2, 'AVAILABLE', NOW(), NOW()),

('Communication Interpersonnelle - Session Mars',
 'Session de formation : Communication Interpersonnelle',
 DATE_ADD(NOW(), INTERVAL 20 DAY),
 DATE_ADD(NOW(), INTERVAL 22 DAY),
 'Paris', 20, 5, 'AVAILABLE', NOW(), NOW()),

('Techniques de Vente - Session Avril',
 'Session de formation : Techniques de Vente Avancées',
 DATE_ADD(NOW(), INTERVAL 40 DAY),
 DATE_ADD(NOW(), INTERVAL 44 DAY),
 'Marseille', 18, 4, 'AVAILABLE', NOW(), NOW()),

('Gestion du Temps - Session Mars',
 'Session de formation : Gestion du Temps et des Priorités',
 DATE_ADD(NOW(), INTERVAL 10 DAY),
 DATE_ADD(NOW(), INTERVAL 12 DAY),
 'Paris', 25, 8, 'AVAILABLE', NOW(), NOW()),

('Scrum Master - Session Mai',
 'Session de formation : Méthode Scrum pour Chefs de Projet',
 DATE_ADD(NOW(), INTERVAL 50 DAY),
 DATE_ADD(NOW(), INTERVAL 53 DAY),
 'Toulouse', 16, 1, 'AVAILABLE', NOW(), NOW()),

('Excellence Relation Client - Session Avril',
 'Session de formation : Excellence en Relation Client',
 DATE_ADD(NOW(), INTERVAL 35 DAY),
 DATE_ADD(NOW(), INTERVAL 38 DAY),
 'Bordeaux', 20, 6, 'AVAILABLE', NOW(), NOW()),

('Management Agile - Session Mai',
 'Session de formation : Management Agile',
 DATE_ADD(NOW(), INTERVAL 55 DAY),
 DATE_ADD(NOW(), INTERVAL 59 DAY),
 'Paris', 14, 3, 'AVAILABLE', NOW(), NOW());

-- ============================================================================
-- 9. BLOG POSTS
-- ============================================================================
INSERT INTO blog (title, description, youtube_url, image_url, publish_date, active, created_at, updated_at) VALUES
('Les 5 Compétences Clés du Manager de Demain',
 'Le monde du travail évolue rapidement. Découvrez les compétences essentielles que tout manager doit développer pour réussir dans l\'environnement professionnel moderne : intelligence émotionnelle, agilité, vision stratégique, capacité d\'innovation et leadership inclusif.',
 'https://www.youtube.com/watch?v=example1',
 '/assets/images/blog/les-5-competences-cles-du-manager-de-demain.jpg',
 DATE_SUB(NOW(), INTERVAL 5 DAY), true, NOW(), NOW()),

('Comment Améliorer sa Communication Interpersonnelle',
 'La communication est au cœur de toute relation professionnelle réussie. Dans cet article, nous explorons les techniques de communication efficace : écoute active, communication non-violente, langage corporel et feedback constructif.',
 'https://www.youtube.com/watch?v=example2',
 '/assets/images/blog/comment-ameliorer-sa-communication-interpersonnelle.jpg',
 DATE_SUB(NOW(), INTERVAL 12 DAY), true, NOW(), NOW()),

('L\'Intelligence Émotionnelle en Entreprise',
 'L\'intelligence émotionnelle est devenue une compétence cruciale dans le monde professionnel. Découvrez comment développer votre QE pour améliorer vos relations, votre leadership et votre efficacité au travail.',
 'https://www.youtube.com/watch?v=example3',
 '/assets/images/blog/lintelligence-emotionnelle-en-entreprise.jpg',
 DATE_SUB(NOW(), INTERVAL 18 DAY), true, NOW(), NOW()),

('Techniques de Vente pour 2024',
 'Le monde de la vente a considérablement évolué. Exit les techniques agressives, place à la vente consultative et à l\'approche centrée sur la valeur client. Découvrez les méthodes qui fonctionnent vraiment en 2024.',
 'https://www.youtube.com/watch?v=example4',
 '/assets/images/blog/techniques-de-vente-pour-2024.jpg',
 DATE_SUB(NOW(), INTERVAL 25 DAY), true, NOW(), NOW()),

('Gestion du Stress : Guide Pratique',
 'Le stress est l\'un des défis majeurs du monde professionnel moderne. Ce guide complet vous propose des techniques éprouvées pour mieux gérer votre stress : respiration, mindfulness, organisation, et équilibre vie pro/perso.',
 'https://www.youtube.com/watch?v=example5',
 '/assets/images/blog/gestion-du-stress-guide-pratique.jpg',
 DATE_SUB(NOW(), INTERVAL 30 DAY), true, NOW(), NOW()),

('Leadership Agile : Principes et Pratiques',
 'Le leadership agile révolutionne la façon dont nous manageons les équipes. Apprenez les principes fondamentaux de l\'agilité appliqués au management.',
 'https://www.youtube.com/watch?v=example6',
 '/assets/images/blog/leadership-agile-principes-et-pratiques.jpg',
 DATE_SUB(NOW(), INTERVAL 40 DAY), true, NOW(), NOW()),

('Recrutement : Les Erreurs à Éviter',
 'Un mauvais recrutement coûte cher à l\'entreprise. Découvrez les erreurs les plus courantes en recrutement et comment les éviter.',
 NULL,
 '/assets/images/blog/recrutement-les-erreurs-a-eviter.jpg',
 DATE_SUB(NOW(), INTERVAL 45 DAY), true, NOW(), NOW()),

('Gestion de Projet : Méthodologies Comparées',
 'Waterfall, Agile, Scrum, Kanban, Lean... Il existe de nombreuses méthodologies de gestion de projet. Ce guide comparatif vous aide à choisir la méthode la plus adaptée.',
 'https://www.youtube.com/watch?v=example7',
 '/assets/images/blog/gestion-de-projet-methodologies-comparees.jpg',
 DATE_SUB(NOW(), INTERVAL 50 DAY), true, NOW(), NOW()),

('L\'Art de la Négociation Commerciale',
 'La négociation est un art qui s\'apprend et se perfectionne. Découvrez les stratégies gagnantes pour négocier avec succès.',
 NULL,
 '/assets/images/blog/lart-de-la-negociation-commerciale.jpg',
 DATE_SUB(NOW(), INTERVAL 55 DAY), true, NOW(), NOW()),

('Développer son Personal Branding',
 'Votre marque personnelle est votre atout le plus précieux dans le monde professionnel moderne. Apprenez à construire et développer votre personal branding.',
 'https://www.youtube.com/watch?v=example8',
 '/assets/images/blog/developper-son-personal-branding.jpg',
 DATE_SUB(NOW(), INTERVAL 60 DAY), true, NOW(), NOW());

-- ============================================================================
-- 10. NEWSLETTER SUBSCRIPTIONS
-- ============================================================================
INSERT INTO newsletters (email, date_subscribed) VALUES
('jean.dupont@example.com', DATE_SUB(NOW(), INTERVAL 10 DAY)),
('marie.martin@example.com', DATE_SUB(NOW(), INTERVAL 15 DAY)),
('pierre.bernard@example.com', DATE_SUB(NOW(), INTERVAL 20 DAY)),
('sophie.dubois@example.com', DATE_SUB(NOW(), INTERVAL 5 DAY)),
('lucas.thomas@example.com', DATE_SUB(NOW(), INTERVAL 25 DAY));

-- ============================================================================
-- VERIFICATION QUERIES
-- ============================================================================
-- Uncomment to verify data insertion

-- SELECT 'Users' as Table_Name, COUNT(*) as Count FROM users
-- UNION ALL
-- SELECT 'Themes', COUNT(*) FROM themes
-- UNION ALL
-- SELECT 'Formations', COUNT(*) FROM formations
-- UNION ALL
-- SELECT 'Reviews', COUNT(*) FROM reviews
-- UNION ALL
-- SELECT 'Portfolio', COUNT(*) FROM portfolio
-- UNION ALL
-- SELECT 'Calendar', COUNT(*) FROM calendars
-- UNION ALL
-- SELECT 'Blog', COUNT(*) FROM blog
-- UNION ALL
-- SELECT 'Newsletters', COUNT(*) FROM newsletters
-- UNION ALL
-- SELECT 'Highlights', COUNT(*) FROM highlights
-- UNION ALL
-- SELECT 'Companies', COUNT(*) FROM company;

-- ============================================================================
-- END OF SEEDER
-- ============================================================================
