-- Migration to assign specific roles to first three users
-- V2__alter_users.sql

-- Insert default roles if they don't exist
INSERT IGNORE INTO roles (name) VALUES
('ADMIN'),
('LIBRARIAN'),
('STUDENT');

-- Create sample users if they don't exist
-- User 1: Admin User (password: admin123)
INSERT IGNORE INTO users (id, name, email, password, enabled, created_at, updated_at)
VALUES (1, 'System Administrator', 'admin@lms.com',
        '$2a$10$DOwTQX/jQ7zqpq7p5VUW3eoBnpX5K8oV5qV5K8V5k8oV5kV5k8V5k',
        TRUE, NOW(), NOW());

-- User 2: Librarian User (password: librarian123)
INSERT IGNORE INTO users (id, name, email, password, enabled, created_at, updated_at)
VALUES (2, 'Head Librarian', 'librarian@lms.com',
        '$2a$10$FOwTQX/jQ7zqpq7p5VUW3eoBnpX5K8oV5qV5K8V5k8oV5kV5k8V5k',
        TRUE, NOW(), NOW());

-- User 3: Student User (password: student123)
INSERT IGNORE INTO users (id, name, email, password, enabled, created_at, updated_at)
VALUES (3, 'Demo Student', 'student@lms.com',
        '$2a$10$HOwTQX/jQ7zqpq7p5VUW3eoBnpX5K8oV5qV5K8V5k8oV5kV5k8V5k',
        TRUE, NOW(), NOW());

-- Clear existing user roles for first 3 users (if any exist)
DELETE FROM user_roles WHERE user_id IN (1, 2, 3);

-- Assign ADMIN role to User ID 1
INSERT INTO user_roles (user_id, role_id)
SELECT 1, r.id FROM roles r WHERE r.name = 'ADMIN'
AND EXISTS (SELECT 1 FROM users WHERE id = 1);

-- Assign LIBRARIAN role to User ID 2
INSERT INTO user_roles (user_id, role_id)
SELECT 2, r.id FROM roles r WHERE r.name = 'LIBRARIAN'
AND EXISTS (SELECT 1 FROM users WHERE id = 2);

-- Assign STUDENT role to User ID 3
INSERT INTO user_roles (user_id, role_id)
SELECT 3, r.id FROM roles r WHERE r.name = 'STUDENT'
AND EXISTS (SELECT 1 FROM users WHERE id = 3);
