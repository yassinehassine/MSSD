-- Schema update for Portfolio table
-- This script should be run to migrate the existing database schema

-- Step 1: Drop the old category column if it exists
ALTER TABLE portfolio DROP COLUMN IF EXISTS category;

-- Step 2: Drop the technologies column as it's no longer needed
ALTER TABLE portfolio DROP COLUMN IF EXISTS technologies;

-- Step 3: Add formation_id column if it doesn't exist
ALTER TABLE portfolio ADD COLUMN IF NOT EXISTS formation_id BIGINT;

-- Step 4: Add foreign key constraint for formation_id
ALTER TABLE portfolio ADD CONSTRAINT IF NOT EXISTS fk_portfolio_formation 
    FOREIGN KEY (formation_id) REFERENCES formations(id);

-- Step 5: Ensure image_path column exists (renamed from image_url)
ALTER TABLE portfolio ADD COLUMN IF NOT EXISTS image_path VARCHAR(255);

-- Step 6: Drop image_url column if it exists
ALTER TABLE portfolio DROP COLUMN IF EXISTS image_url;

-- Step 7: Ensure project_date is DATE type (not DATETIME)
ALTER TABLE portfolio MODIFY COLUMN project_date DATE;
