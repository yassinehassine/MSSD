-- COMPLETELY CLEAR BLOG TABLE
-- This will remove any remaining blog entries

USE MSSDD;

-- Delete all remaining blog entries
DELETE FROM blog;

-- Reset auto-increment to start from 1
ALTER TABLE blog AUTO_INCREMENT = 1;

-- Verify table is completely empty
SELECT COUNT(*) as remaining_blogs FROM blog;

-- Show any remaining data (should be empty)
SELECT * FROM blog;