-- EMPTY BLOG TABLE FOR TESTING
-- This will remove all blog entries so you can add them yourself

USE MSSDD;

-- Clear all blog data
DELETE FROM blog;

-- Reset the auto-increment counter to start from 1
ALTER TABLE blog AUTO_INCREMENT = 1;

-- Verify the table is empty
SELECT COUNT(*) as blog_count FROM blog;

-- Show table structure to confirm it's ready
DESCRIBE blog;