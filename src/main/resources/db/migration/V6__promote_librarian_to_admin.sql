-- V6: Promote Librarian to ADMIN
-- Ensure librarian has ADMIN privileges while keeping existing roles

-- Make sure the ADMIN role exists
INSERT INTO roles (name)
SELECT 'ADMIN'
WHERE NOT EXISTS (
    SELECT 1 FROM roles r WHERE r.name = 'ADMIN'
);

-- Grant ADMIN role to the librarian user (email: librarian@lms.com)
-- Avoid duplicates if already assigned
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
JOIN roles r ON r.name = 'ADMIN'
WHERE u.email = 'librarian@lms.com'
  AND NOT EXISTS (
      SELECT 1 FROM user_roles ur
      WHERE ur.user_id = u.id AND ur.role_id = r.id
  );

-- Note: We intentionally do not update users.role column here,
-- as Hibernate manages that field and the application primarily
-- authorizes via the roles/user_roles relationship.
