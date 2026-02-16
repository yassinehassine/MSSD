-- Migration script to create calendar_reservations table
-- Run this script to add the new table for calendar event reservations

CREATE TABLE IF NOT EXISTS calendar_reservations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    client_name VARCHAR(255) NOT NULL,
    client_email VARCHAR(255) NOT NULL,
    client_phone VARCHAR(255),
    event_title VARCHAR(255) NOT NULL,
    event_description TEXT,
    event_date DATETIME NOT NULL,
    duration INT,
    location VARCHAR(255),
    status ENUM('PENDING', 'CONFIRMED', 'CANCELLED', 'COMPLETED') NOT NULL DEFAULT 'PENDING',
    admin_notes TEXT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_client_email (client_email),
    INDEX idx_event_date (event_date),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at)
);

-- Add some sample data for testing (optional)
/*
INSERT INTO calendar_reservations (client_name, client_email, client_phone, event_title, event_description, event_date, duration, location, status) VALUES
('Jean Dupont', 'jean.dupont@email.com', '0123456789', 'Conférence Marketing Digital', 'Présentation des dernières tendances en marketing digital', '2024-01-15 14:00:00', 120, 'Salle de conférence A', 'PENDING'),
('Marie Martin', 'marie.martin@email.com', '0987654321', 'Atelier Design Thinking', 'Session pratique de design thinking pour l\'innovation', '2024-01-20 09:00:00', 180, 'Salle créative B', 'CONFIRMED'),
('Pierre Durand', 'pierre.durand@email.com', NULL, 'Formation Leadership', 'Formation intensive sur les compétences de leadership', '2024-01-25 10:00:00', 240, 'Auditorium', 'PENDING');
*/