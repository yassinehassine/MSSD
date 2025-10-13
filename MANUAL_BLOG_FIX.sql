-- MANUAL DATABASE FIX FOR BLOG SYSTEM - SAFE VERSION
-- Run these commands directly in your MySQL database

-- USE MSSDD database
USE MSSDD;

-- First, check if blog table exists and what columns it has
DESCRIBE blog;

-- Method 1: If blog table exists but missing columns, add them
-- Disable foreign key checks temporarily
SET FOREIGN_KEY_CHECKS = 0;

-- Clean up any bad data first
DELETE FROM blog WHERE created_at IS NULL OR created_at = '0000-00-00 00:00:00';

-- Add missing columns if they don't exist
ALTER TABLE blog ADD COLUMN IF NOT EXISTS created_at DATETIME NULL;
ALTER TABLE blog ADD COLUMN IF NOT EXISTS updated_at DATETIME NULL;

-- Update existing rows with valid timestamps
UPDATE blog SET created_at = COALESCE(publish_date, NOW()) WHERE created_at IS NULL;
UPDATE blog SET updated_at = COALESCE(publish_date, NOW()) WHERE updated_at IS NULL;

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

-- Method 2: If you prefer to start fresh (DANGER: This will delete all blog data)
-- Uncomment the lines below ONLY if you want to completely recreate the blog table
-- SET FOREIGN_KEY_CHECKS = 0;
-- DROP TABLE IF EXISTS blog;
-- SET FOREIGN_KEY_CHECKS = 1;
-- 
-- CREATE TABLE blog (
--     id BIGINT AUTO_INCREMENT PRIMARY KEY,
--     title VARCHAR(255) NOT NULL,
--     description TEXT,
--     youtube_url VARCHAR(500),
--     image_url VARCHAR(500),
--     publish_date DATETIME,
--     active BOOLEAN NOT NULL DEFAULT TRUE,
--     created_at DATETIME,
--     updated_at DATETIME
-- );

-- Insert sample blog posts for testing (safe - won't duplicate)
INSERT IGNORE INTO blog (title, description, youtube_url, image_url, publish_date, active, created_at, updated_at)
VALUES 
('Formation Développement Web - Retour d\'Expérience', 'Découvrez les témoignages des participants de notre formation en développement web et leurs projets réalisés', 'https://www.youtube.com/watch?v=dQw4w9WgXcQ', 'uploads/blog-web-dev.jpg', '2024-09-15 10:00:00', true, NOW(), NOW()),
('Projet Mobile Banking - Étude de Cas', 'Analyse complète du développement de l\'application mobile banking pour notre client IBM', NULL, 'uploads/blog-mobile-app.jpg', '2024-09-10 14:30:00', true, NOW(), NOW()),
('Workshop Data Science - Highlights', 'Les moments forts de notre workshop Data Science avec des exercices pratiques en machine learning', 'https://www.youtube.com/watch?v=ScMzIvxBSi4', NULL, '2024-09-05 09:15:00', true, NOW(), NOW()),
('Success Story: E-Commerce Platform', 'Comment nous avons aidé Microsoft à développer leur nouvelle plateforme e-commerce en 8 semaines', NULL, 'uploads/blog-ecommerce.jpg', '2024-08-28 16:45:00', true, NOW(), NOW()),
('Conférence Marketing Digital 2024', 'Résumé de notre participation à la conférence marketing digital avec les dernières tendances SEO', 'https://www.youtube.com/watch?v=kJQP7kiw5Fk', 'uploads/blog-marketing.jpg', '2024-08-20 11:20:00', true, NOW(), NOW());

-- Verify the table structure
DESCRIBE blog;

-- Check the data
SELECT id, title, created_at, updated_at FROM blog;