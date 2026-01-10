-- V4: Update sample users - change admin to librarian, remove old librarian
-- This migration updates the user structure based on new requirements

-- Clear existing users and their roles
DELETE FROM user_roles WHERE user_id IN (1, 2, 3);
DELETE FROM users WHERE id IN (1, 2, 3);

-- User 1: Librarian (formerly admin) - password: librarian123
INSERT INTO users (id, name, email, password, enabled, created_at, updated_at)
VALUES (1, 'Head Librarian', 'librarian@lms.com',
        '$2a$10$nUnFDIJ1xsQ3/gi//b9Nvew9WdfEjmMYqlQdJDv8DyUtKUZLeSB1y',
        TRUE, NOW(), NOW());

-- User 3: Student - password: student123
INSERT INTO users (id, name, email, password, enabled, created_at, updated_at)
VALUES (3, 'Demo Student', 'student@lms.com',
        '$2a$10$sIrNOSmLZYbN/uQvsVpHn.y.9owHtL5P9peP5oddWQEbgyuL71u1.',
        TRUE, NOW(), NOW());

-- Assign LIBRARIAN role to User ID 1
INSERT INTO user_roles (user_id, role_id)
SELECT 1, r.id FROM roles r WHERE r.name = 'LIBRARIAN';

-- Assign STUDENT role to User ID 3
INSERT INTO user_roles (user_id, role_id)
SELECT 3, r.id FROM roles r WHERE r.name = 'STUDENT';
