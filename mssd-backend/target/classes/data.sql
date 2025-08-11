-- First, handle schema updates if needed
SET SQL_MODE = 'TRADITIONAL';

-- Drop and recreate portfolio table with correct structure
DROP TABLE IF EXISTS portfolio;

CREATE TABLE portfolio (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    formation_id BIGINT NOT NULL,
    image_path VARCHAR(255),
    client_name VARCHAR(255),
    project_date DATE,
    project_url VARCHAR(255),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME NOT NULL,
    updated_at DATETIME,
    CONSTRAINT fk_portfolio_formation FOREIGN KEY (formation_id) REFERENCES formations(id)
);

-- Insert test admin user
INSERT INTO users (username, email, password, role, created_at) 
VALUES ('admin', 'admin@mssd.com', 'admin123', 'ADMIN', NOW())
ON DUPLICATE KEY UPDATE username = username;

-- Insert test regular user
INSERT INTO users (username, email, password, role, created_at) 
VALUES ('user', 'user@mssd.com', 'user123', 'USER', NOW())
ON DUPLICATE KEY UPDATE username = username;

-- Insert sample formations (required for portfolio foreign key)
INSERT INTO formations (id, title, slug, description, category, price, duration, level, published, created_at)
VALUES 
(1, 'Web Development Fundamentals', 'web-development-fundamentals', 'Learn the basics of web development with HTML, CSS, and JavaScript', 'Development', 299.99, '8 weeks', 'BEGINNER', true, NOW()),
(2, 'Mobile App Development', 'mobile-app-development', 'Build native and cross-platform mobile applications', 'Development', 499.99, '12 weeks', 'INTERMEDIATE', true, NOW()),
(3, 'Data Science and Analytics', 'data-science-analytics', 'Master data analysis, visualization, and machine learning', 'Analytics', 599.99, '16 weeks', 'EXPERT', true, NOW()),
(4, 'Digital Marketing Strategy', 'digital-marketing-strategy', 'Comprehensive digital marketing and sales strategies', 'Marketing', 399.99, '10 weeks', 'INTERMEDIATE', true, NOW()),
(5, 'Business Management Solutions', 'business-management-solutions', 'Enterprise management and consultation services', 'Management', 799.99, '20 weeks', 'EXPERT', true, NOW())
ON DUPLICATE KEY UPDATE title = title;

-- Insert sample portfolio items
INSERT INTO portfolio (title, description, formation_id, image_path, client_name, project_date, project_url, active, created_at, updated_at)
VALUES 
('E-Commerce Platform', 'Complete e-commerce solution with payment integration and inventory management', 1, 'uploads/app-1.jpg', 'TechCorp Inc', '2024-01-15', 'https://example-ecommerce.com', true, NOW(), NOW()),
('Mobile Banking App', 'Secure mobile banking application with biometric authentication', 2, 'uploads/app-2.jpg', 'SecureBank', '2024-02-20', 'https://securebank.com', true, NOW(), NOW()),
('Corporate Website', 'Modern corporate website with CMS and SEO optimization', 1, 'uploads/web-3.jpg', 'Global Solutions Ltd', '2024-03-10', 'https://globalsolutions.com', true, NOW(), NOW()),
('Inventory Management System', 'Real-time inventory tracking and management system', 3, 'uploads/app-3.jpg', 'LogiCorp', '2024-04-05', 'https://logicorp-inventory.com', true, NOW(), NOW()),
('Healthcare Portal', 'Patient management system with appointment scheduling', 1, 'uploads/web-2.jpg', 'MediCare Plus', '2024-05-12', 'https://medicareplus.com', true, NOW(), NOW()),
('Restaurant POS System', 'Point of sale system for restaurant management', 2, 'uploads/product-1.jpg', 'FoodChain Restaurants', '2024-06-18', 'https://foodchain-pos.com', true, NOW(), NOW());
