-- Fix portfolio_items table structure
-- This will drop the old table and recreate it with the correct PortfolioItem structure

USE MSSDD;

-- Drop the old table completely
DROP TABLE IF EXISTS portfolio_items;

-- Recreate with correct structure matching PortfolioItem entity
CREATE TABLE portfolio_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    company_name VARCHAR(255) NOT NULL,
    training_title VARCHAR(255) NOT NULL,
    training_date DATE NOT NULL,
    logo_url VARCHAR(500),
    description TEXT,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Verify the structure
DESCRIBE portfolio_items;

SELECT 'Portfolio items table has been recreated successfully!' AS Status;
