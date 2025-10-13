-- COMPREHENSIVE BLOG CLEANUP SCRIPT
-- This will completely remove all blog data from everywhere

USE MSSDD;

-- 1. Disable foreign key checks to avoid constraint issues
SET FOREIGN_KEY_CHECKS = 0;

-- 2. Truncate the blog table (this is more thorough than DELETE)
TRUNCATE TABLE blog;

-- 3. Alternative: If truncate doesn't work, force delete all
DELETE FROM blog WHERE 1=1;

-- 4. Reset auto-increment counter
ALTER TABLE blog AUTO_INCREMENT = 1;

-- 5. Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

-- 6. Verify the table is completely empty
SELECT COUNT(*) as total_blogs FROM blog;
SELECT * FROM blog LIMIT 10;

-- 7. Show table structure to confirm it's ready for new data
DESCRIBE blog;

-- Success message
SELECT 'Blog table is now completely empty and ready for testing!' as status;