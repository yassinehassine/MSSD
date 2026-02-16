-- Blog table migration script
-- This script handles the blog table creation/migration

-- Check if blog table exists and alter it if needed
-- First, try to add the missing columns if table exists without them

-- Drop the blog table if it exists (to ensure clean state)
DROP TABLE IF EXISTS blog;

-- Create the blog table with proper structure
CREATE TABLE blog (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    youtube_url VARCHAR(500),
    image_url VARCHAR(500),  
    publish_date DATETIME,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME NOT NULL,
    updated_at DATETIME
);

-- Insert sample blog data
INSERT INTO blog (title, description, youtube_url, image_url, publish_date, active, created_at, updated_at)
VALUES 
('Formation Développement Web - Retour d\'Expérience', 'Découvrez les témoignages des participants de notre formation en développement web et leurs projets réalisés', 'https://www.youtube.com/watch?v=dQw4w9WgXcQ', 'uploads/blog-web-dev.jpg', '2024-09-15 10:00:00', true, NOW(), NOW()),
('Projet Mobile Banking - Étude de Cas', 'Analyse complète du développement de l\'application mobile banking pour notre client IBM', NULL, 'uploads/blog-mobile-app.jpg', '2024-09-10 14:30:00', true, NOW(), NOW()),
('Workshop Data Science - Highlights', 'Les moments forts de notre workshop Data Science avec des exercices pratiques en machine learning', 'https://www.youtube.com/watch?v=ScMzIvxBSi4', NULL, '2024-09-05 09:15:00', true, NOW(), NOW()),
('Success Story: E-Commerce Platform', 'Comment nous avons aidé Microsoft à développer leur nouvelle plateforme e-commerce en 8 semaines', NULL, 'uploads/blog-ecommerce.jpg', '2024-08-28 16:45:00', true, NOW(), NOW()),
('Conférence Marketing Digital 2024', 'Résumé de notre participation à la conférence marketing digital avec les dernières tendances SEO', 'https://www.youtube.com/watch?v=kJQP7kiw5Fk', 'uploads/blog-marketing.jpg', '2024-08-20 11:20:00', true, NOW(), NOW());