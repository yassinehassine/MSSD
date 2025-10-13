-- First, handle schema updates if needed
SET SQL_MODE = 'TRADITIONAL';

-- Drop and recreate portfolio table with correct structure
DROP TABLE IF EXISTS portfolio;

-- Create themes table
CREATE TABLE IF NOT EXISTS themes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    slug VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    icon_url VARCHAR(500),
    active BOOLEAN NOT NULL DEFAULT TRUE
);

-- Create annex_requests table
CREATE TABLE IF NOT EXISTS annex_requests (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    company_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    formation_id BIGINT,
    is_custom BOOLEAN NOT NULL DEFAULT FALSE,
    custom_description TEXT,
    num_participants INTEGER NOT NULL,
    modality VARCHAR(20) NOT NULL,
    preferred_date VARCHAR(255) NOT NULL,
    notes TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    created_at DATETIME NOT NULL,
    updated_at DATETIME,
    CONSTRAINT fk_annex_formation FOREIGN KEY (formation_id) REFERENCES formations(id)
);

CREATE TABLE portfolio (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    formation_id BIGINT NOT NULL,
    image_url VARCHAR(500),
    client_name VARCHAR(255),
    project_date DATE,
    project_url VARCHAR(255),
    company_logo_url VARCHAR(500),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME NOT NULL,
    updated_at DATETIME,
    CONSTRAINT fk_portfolio_formation FOREIGN KEY (formation_id) REFERENCES formations(id)
);

-- Blog table migration - safer approach
-- Create blog table if it doesn't exist (without dropping)
CREATE TABLE IF NOT EXISTS blog (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    youtube_url VARCHAR(500),
    image_url VARCHAR(500),
    publish_date DATETIME,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

-- Try to add missing columns safely
-- Note: These will fail silently if columns already exist
ALTER TABLE blog ADD COLUMN created_at DATETIME NULL;
ALTER TABLE blog ADD COLUMN updated_at DATETIME NULL;

-- Insert sample themes
INSERT INTO themes (id, name, slug, description, icon_url, active)
VALUES 
(1, 'Développement Web', 'developpement-web', 'Technologies et frameworks pour le développement web moderne', NULL, true),
(2, 'Applications Mobiles', 'applications-mobiles', 'Développement d\'applications natives et cross-platform', NULL, true),
(3, 'Data Science & Analytics', 'data-science-analytics', 'Analyse de données, visualisation et apprentissage automatique', NULL, true),
(4, 'Marketing Digital', 'marketing-digital', 'Stratégies de marketing numérique et communication', NULL, true),
(5, 'Management & Leadership', 'management-leadership', 'Gestion d\'équipe et leadership en entreprise', NULL, true)
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- Insert test admin user
INSERT INTO users (username, email, password, role, created_at) 
VALUES ('admin', 'admin@mssd.com', 'admin123', 'ADMIN', NOW())
ON DUPLICATE KEY UPDATE username = username;

-- Insert test regular user
INSERT INTO users (username, email, password, role, created_at) 
VALUES ('user', 'user@mssd.com', 'user123', 'USER', NOW())
ON DUPLICATE KEY UPDATE username = username;

-- Insert sample formations with theme relationships
INSERT INTO formations (id, title, slug, description, category, price, duration, level, published, theme_id, created_at)
VALUES 
(1, 'Développement Web Fundamentals', 'web-development-fundamentals', 'Apprenez les bases du développement web avec HTML, CSS, et JavaScript', 'Development', 299.99, '8 semaines', 'BEGINNER', true, 1, NOW()),
(2, 'Développement d\'Applications Mobiles', 'mobile-app-development', 'Construisez des applications mobiles natives et cross-platform', 'Development', 499.99, '12 semaines', 'INTERMEDIATE', true, 2, NOW()),
(3, 'Data Science et Analytique', 'data-science-analytics', 'Maîtrisez l\'analyse de données, la visualisation et l\'apprentissage automatique', 'Analytics', 599.99, '16 semaines', 'EXPERT', true, 3, NOW()),
(4, 'Stratégie Marketing Digital', 'digital-marketing-strategy', 'Stratégies complètes de marketing numérique et de vente', 'Marketing', 399.99, '10 semaines', 'INTERMEDIATE', true, 4, NOW()),
(5, 'Solutions de Gestion d\'Entreprise', 'business-management-solutions', 'Services de gestion d\'entreprise et de consultation', 'Management', 799.99, '20 semaines', 'EXPERT', true, 5, NOW()),
(6, 'React.js Avancé', 'react-js-avance', 'Développement d\'applications web avec React.js et ses écosystèmes', 'Development', 449.99, '10 semaines', 'INTERMEDIATE', true, 1, NOW()),
(7, 'Flutter Development', 'flutter-development', 'Création d\'applications mobiles cross-platform avec Flutter', 'Development', 529.99, '14 semaines', 'INTERMEDIATE', true, 2, NOW()),
(8, 'Machine Learning Pratique', 'machine-learning-pratique', 'Application pratique du machine learning en entreprise', 'Analytics', 699.99, '18 semaines', 'EXPERT', true, 3, NOW()),
(9, 'SEO et Content Marketing', 'seo-content-marketing', 'Optimisation SEO et stratégies de contenu digital', 'Marketing', 349.99, '8 semaines', 'BEGINNER', true, 4, NOW()),
(10, 'Leadership et Gestion d\'Équipe', 'leadership-gestion-equipe', 'Développement du leadership et management d\'équipe', 'Management', 599.99, '12 semaines', 'INTERMEDIATE', true, 5, NOW())
ON DUPLICATE KEY UPDATE title = VALUES(title), theme_id = VALUES(theme_id);

-- Insert sample portfolio items
INSERT INTO portfolio (title, description, formation_id, image_url, client_name, project_date, project_url, company_logo_url, active, created_at, updated_at)
VALUES 
('E-Commerce Platform', 'Complete e-commerce solution with payment integration and inventory management', 1, 'uploads/app-1.jpg', 'Microsoft', '2024-01-15', 'https://example-ecommerce.com', 'https://logo.clearbit.com/microsoft.com', true, NOW(), NOW()),
('Mobile Banking App', 'Secure mobile banking application with biometric authentication', 2, 'uploads/app-2.jpg', 'IBM', '2024-02-20', 'https://securebank.com', 'https://logo.clearbit.com/ibm.com', true, NOW(), NOW()),
('Corporate Website', 'Modern corporate website with CMS and SEO optimization', 1, 'uploads/web-3.jpg', 'Google', '2024-03-10', 'https://globalsolutions.com', 'https://logo.clearbit.com/google.com', true, NOW(), NOW()),
('Inventory Management System', 'Real-time inventory tracking and management system', 3, 'uploads/app-3.jpg', 'Amazon', '2024-04-05', 'https://logicorp-inventory.com', 'https://logo.clearbit.com/amazon.com', true, NOW(), NOW()),
('Healthcare Portal', 'Patient management system with appointment scheduling', 1, 'uploads/web-2.jpg', 'Apple', '2024-05-12', 'https://medicareplus.com', 'https://logo.clearbit.com/apple.com', true, NOW(), NOW()),
('Restaurant POS System', 'Point of sale system for restaurant management', 2, 'uploads/product-1.jpg', 'Netflix', '2024-06-18', 'https://foodchain-pos.com', 'https://logo.clearbit.com/netflix.com', true, NOW(), NOW()),
('AI Analytics Dashboard', 'Machine learning powered business analytics platform', 1, 'uploads/web-1.jpg', 'Tesla', '2024-07-10', 'https://tesla-analytics.com', 'https://logo.clearbit.com/tesla.com', true, NOW(), NOW()),
('Social Media Platform', 'Custom social networking solution for enterprises', 2, 'uploads/app-4.jpg', 'Meta', '2024-08-15', 'https://meta-social.com', 'https://logo.clearbit.com/meta.com', true, NOW(), NOW());

-- Blog table is ready for manual data entry
-- Sample blog data removed - you can add your own blogs through the admin interface

-- Create new portfolio_items table for infinite scroll portfolio
CREATE TABLE IF NOT EXISTS portfolio_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    company_name VARCHAR(255) NOT NULL,
    training_title VARCHAR(255) NOT NULL,
    training_date DATE NOT NULL,
    logo_url VARCHAR(500),
    description TEXT,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME NOT NULL DEFAULT NOW(),
    updated_at DATETIME DEFAULT NOW()
);

-- Insert sample portfolio items with company logos
INSERT INTO portfolio_items (company_name, training_title, training_date, logo_url, description, active, created_at)
VALUES 
('Microsoft', 'Leadership & Management Excellence', '2024-01-15', 'https://logo.clearbit.com/microsoft.com', 'Programme de formation en leadership pour les équipes de direction', true, NOW()),
('Google', 'Digital Transformation Strategy', '2024-02-10', 'https://logo.clearbit.com/google.com', 'Formation sur la stratégie de transformation numérique', true, NOW()),
('Apple', 'Agile Project Management', '2024-01-28', 'https://logo.clearbit.com/apple.com', 'Méthodologies agiles pour la gestion de projets technologiques', true, NOW()),
('Amazon', 'Data Analytics & Business Intelligence', '2024-03-05', 'https://logo.clearbit.com/amazon.com', 'Formation avancée en analyse de données et BI', true, NOW()),
('Tesla', 'Innovation & Creative Problem Solving', '2024-02-22', 'https://logo.clearbit.com/tesla.com', 'Techniques d\'innovation et résolution créative de problèmes', true, NOW()),
('Meta', 'Digital Marketing & Social Media', '2024-03-18', 'https://logo.clearbit.com/meta.com', 'Stratégies de marketing digital et réseaux sociaux', true, NOW()),
('Netflix', 'Customer Experience Excellence', '2024-01-08', 'https://logo.clearbit.com/netflix.com', 'Amélioration de l\'expérience client et satisfaction', true, NOW()),
('IBM', 'AI & Machine Learning Foundations', '2024-04-02', 'https://logo.clearbit.com/ibm.com', 'Bases de l\'intelligence artificielle et machine learning', true, NOW()),
('Salesforce', 'Sales Performance Optimization', '2024-02-14', 'https://logo.clearbit.com/salesforce.com', 'Optimisation des performances commerciales', true, NOW()),
('Adobe', 'Creative Design & Brand Management', '2024-03-25', 'https://logo.clearbit.com/adobe.com', 'Design créatif et gestion de marque', true, NOW()),
('Spotify', 'Team Building & Communication', '2024-01-30', 'https://logo.clearbit.com/spotify.com', 'Cohésion d\'équipe et communication efficace', true, NOW()),
('Uber', 'Operations & Process Improvement', '2024-04-10', 'https://logo.clearbit.com/uber.com', 'Amélioration des opérations et des processus', true, NOW()),
('Airbnb', 'Hospitality & Service Excellence', '2024-02-28', 'https://logo.clearbit.com/airbnb.com', 'Excellence du service et hospitalité', true, NOW()),
('LinkedIn', 'Professional Development & Networking', '2024-03-12', 'https://logo.clearbit.com/linkedin.com', 'Développement professionnel et réseautage', true, NOW()),
('Zoom', 'Remote Work & Virtual Collaboration', '2024-01-22', 'https://logo.clearbit.com/zoom.us', 'Travail à distance et collaboration virtuelle', true, NOW())
ON DUPLICATE KEY UPDATE company_name = VALUES(company_name);
