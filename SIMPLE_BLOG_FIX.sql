-- SIMPLE IMMEDIATE FIX FOR BLOG SYSTEM
-- This approach just works with the existing table structure

USE MSSDD;

-- Check current blog table structure
DESCRIBE blog;

-- If the table has basic columns but missing created_at/updated_at, this query will work
-- Clean up any problematic data
DELETE FROM blog WHERE 
    (created_at IS NOT NULL AND created_at = '0000-00-00 00:00:00') 
    OR (updated_at IS NOT NULL AND updated_at = '0000-00-00 00:00:00');

-- If created_at/updated_at columns exist but have bad data, fix them
UPDATE blog SET 
    created_at = COALESCE(publish_date, NOW()),
    updated_at = COALESCE(publish_date, NOW())
WHERE created_at IS NULL OR updated_at IS NULL;

-- Insert sample data (safe insert)
INSERT IGNORE INTO blog (title, description, youtube_url, image_url, publish_date, active)
VALUES 
('Formation Développement Web - Retour d\'Expérience', 'Découvrez les témoignages des participants de notre formation en développement web et leurs projets réalisés', 'https://www.youtube.com/watch?v=dQw4w9WgXcQ', 'uploads/blog-web-dev.jpg', '2024-09-15 10:00:00', true),
('Projet Mobile Banking - Étude de Cas', 'Analyse complète du développement de l\'application mobile banking pour notre client IBM', NULL, 'uploads/blog-mobile-app.jpg', '2024-09-10 14:30:00', true),
('Workshop Data Science - Highlights', 'Les moments forts de notre workshop Data Science avec des exercices pratiques en machine learning', 'https://www.youtube.com/watch?v=ScMzIvxBSi4', NULL, '2024-09-05 09:15:00', true);

-- Show final state
SELECT * FROM blog LIMIT 5;