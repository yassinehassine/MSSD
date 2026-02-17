-- Migration: Add start_date column to formations table
-- This migration adds a start_date field to track when a formation begins

-- Check if column exists, and if not, add it
ALTER TABLE formations 
ADD COLUMN IF NOT EXISTS start_date DATETIME NULL AFTER image_url;

-- Optional: Add an index for better query performance on start_date
CREATE INDEX IF NOT EXISTS idx_formations_start_date ON formations(start_date);

-- Optional: Update existing formations with a default start date (commented out)
-- UPDATE formations SET start_date = NOW() WHERE start_date IS NULL;
