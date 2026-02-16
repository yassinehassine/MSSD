-- ============================================
-- Update User Passwords to BCrypt Hashed
-- ============================================
-- This script updates existing plain text passwords to BCrypt hashed passwords
-- Run this script if you have existing users with plain text passwords

-- Update admin user password from 'admin123' to BCrypt hashed version
UPDATE users 
SET password = '$2a$10$XptfskJDGleLq2UfL.H45e5wI0gJFLJ6VhSz/T8HlZP/2h9e9a2hS'
WHERE email = 'admin@mssd.com' AND password = 'admin123';

-- Update regular user password from 'user123' to BCrypt hashed version
UPDATE users 
SET password = '$2a$10$8qVVL8qQxvvNkQnO/x6Z.u8WwCmU5jN0wvYZKL5hYZNQ9dX1J4ksO'
WHERE email = 'user@mssd.com' AND password = 'user123';

-- Verify the update
SELECT id, username, email, role, 
       CASE 
           WHEN password LIKE '$2a$%' THEN '✓ Hashed'
           ELSE '✗ Plain text'
       END as password_status
FROM users;

-- ============================================
-- HOW TO HASH NEW PASSWORDS
-- ============================================
-- Use the backend API endpoint to hash passwords:
-- 
-- POST http://localhost:8080/api/auth/hash-password
-- Content-Type: application/json
-- 
-- {
--   "password": "yourPlainPassword"
-- }
-- 
-- Example using curl:
-- curl -X POST http://localhost:8080/api/auth/hash-password \
--   -H "Content-Type: application/json" \
--   -d '{"password":"myNewPassword"}'
-- 
-- Example using PowerShell:
-- $body = @{ password = "myNewPassword" } | ConvertTo-Json
-- Invoke-RestMethod -Uri "http://localhost:8080/api/auth/hash-password" -Method Post -Body $body -ContentType "application/json"
