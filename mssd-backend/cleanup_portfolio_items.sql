-- SQL Script to clean up portfolio_items table
-- Remove extra columns added by the old Portfolio entity

USE MSSDD;

-- Drop foreign key constraint first
ALTER TABLE portfolio_items DROP FOREIGN KEY IF EXISTS FK5e9tkoslejhu8bhvdpqf44igt;

-- Drop the extra columns that were added by the old Portfolio entity
ALTER TABLE portfolio_items DROP COLUMN IF EXISTS title;
ALTER TABLE portfolio_items DROP COLUMN IF EXISTS formation_id;
ALTER TABLE portfolio_items DROP COLUMN IF EXISTS client_name;
ALTER TABLE portfolio_items DROP COLUMN IF EXISTS image_url;
ALTER TABLE portfolio_items DROP COLUMN IF EXISTS project_date;
ALTER TABLE portfolio_items DROP COLUMN IF EXISTS project_url;
ALTER TABLE portfolio_items DROP COLUMN IF EXISTS company_logo_url;
ALTER TABLE portfolio_items DROP COLUMN IF EXISTS category;

-- Verify the table structure
DESCRIBE portfolio_items;
