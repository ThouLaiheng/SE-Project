-- Check and fix user roles
-- Run this in MySQL Workbench

USE lms_db;

-- First, check current user roles
SELECT u.id, u.email, r.name as role
FROM users u
LEFT JOIN user_roles ur ON u.id = ur.user_id
LEFT JOIN roles r ON ur.role_id = r.id
WHERE u.id IN (1, 2, 3);

-- If roles are missing, delete and recreate them
DELETE FROM user_roles WHERE user_id IN (1, 2, 3);

-- Assign ADMIN role to user 1
INSERT INTO user_roles (user_id, role_id)
SELECT 1, id FROM roles WHERE name = 'ADMIN';

-- Assign LIBRARIAN role to user 2
INSERT INTO user_roles (user_id, role_id)
SELECT 2, id FROM roles WHERE name = 'LIBRARIAN';

-- Assign STUDENT role to user 3
INSERT INTO user_roles (user_id, role_id)
SELECT 3, id FROM roles WHERE name = 'STUDENT';

-- Verify the roles are assigned
SELECT u.id, u.email, r.name as role
FROM users u
JOIN user_roles ur ON u.id = ur.user_id
JOIN roles r ON ur.role_id = r.id
WHERE u.id IN (1, 2, 3);
