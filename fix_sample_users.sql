-- Fix sample users passwords
-- Run this in your MySQL client/workbench

USE lms_db;

-- Update passwords for sample users using a known working password hash
-- This uses the password hash from user 4 (Heng@gmail.com) which works
-- So all three users will have the same password as Heng@gmail.com

UPDATE users
SET password = '$2a$12$AIRM1Pk2FUZPHLN7NSHMYerdWei0S.VOLeNwa5OnGdH/j4LmC6BqK'
WHERE id IN (1, 2, 3);

-- Verify user roles exist
SELECT u.id, u.email, r.name as role
FROM users u
LEFT JOIN user_roles ur ON u.id = ur.user_id
LEFT JOIN roles r ON ur.role_id = r.id
WHERE u.id IN (1, 2, 3);
