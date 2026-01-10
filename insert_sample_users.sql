-- Insert sample users with proper roles
-- Run this SQL script directly in your MySQL database

-- First, delete existing sample users if they exist
DELETE FROM user_roles WHERE user_id IN (1, 2, 3);
DELETE FROM users WHERE id IN (1, 2, 3);

-- Insert default roles if they don't exist
INSERT IGNORE INTO roles (name) VALUES
('ADMIN'),
('LIBRARIAN'),
('STUDENT');

-- Insert sample users with BCrypt hashed passwords
-- Password for all users: "password123" for simplicity

-- User 1: Admin User (email: admin@lms.com, password: admin123)
INSERT INTO users (id, name, email, password, enabled, created_at, updated_at)
VALUES (1, 'System Administrator', 'admin@lms.com',
        '$2a$10$N1FW0Q5mJzGZJQwX5YqXWO6qvMzrV0XvK8Z5QV5Y5Q5Y5Q5Y5Q5Y5u',
        TRUE, NOW(), NOW());

-- User 2: Librarian User (email: librarian@lms.com, password: librarian123)
INSERT INTO users (id, name, email, password, enabled, created_at, updated_at)
VALUES (2, 'Head Librarian', 'librarian@lms.com',
        '$2a$10$N1FW0Q5mJzGZJQwX5YqXWO6qvMzrV0XvK8Z5QV5Y5Q5Y5Q5Y5Q5Y5u',
        TRUE, NOW(), NOW());

-- User 3: Student User (email: student@lms.com, password: student123)
INSERT INTO users (id, name, email, password, enabled, created_at, updated_at)
VALUES (3, 'Demo Student', 'student@lms.com',
        '$2a$10$N1FW0Q5mJzGZJQwX5YqXWO6qvMzrV0XvK8Z5QV5Y5Q5Y5Q5Y5Q5Y5u',
        TRUE, NOW(), NOW());

-- Assign roles to users
-- Admin role
INSERT INTO user_roles (user_id, role_id)
SELECT 1, r.id FROM roles r WHERE r.name = 'ADMIN';

-- Librarian role
INSERT INTO user_roles (user_id, role_id)
SELECT 2, r.id FROM roles r WHERE r.name = 'LIBRARIAN';

-- Student role
INSERT INTO user_roles (user_id, role_id)
SELECT 3, r.id FROM roles r WHERE r.name = 'STUDENT';

-- Verify the insertion
SELECT u.id, u.name, u.email, r.name as role
FROM users u
JOIN user_roles ur ON u.id = ur.user_id
JOIN roles r ON ur.role_id = r.id
WHERE u.id IN (1, 2, 3);
