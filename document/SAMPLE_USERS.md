# Sample Users Documentation

This document reflects the current sample users created and updated by migrations V2, V4, and V6.

## Current Sample Users

Two users are present for development/testing:

### 1. Librarian (Promoted to Admin)
- **ID**: 1
- **Name**: Head Librarian  
- **Email**: librarian@lms.com
- **Password**: librarian123
- **Roles**: ADMIN, LIBRARIAN
- **Status**: Enabled

### 2. Student
- **ID**: 3
- **Name**: Demo Student
- **Email**: student@lms.com
- **Password**: student123
- **Role**: STUDENT  
- **Status**: Enabled

## Role Permissions

### ADMIN
- Full system access
- Manage users, books, categories, and system settings
- View reports and audit logs

### LIBRARIAN
- Manage books and categories
- Handle borrowing/returns, reservations, and fines
- View borrowing reports

### STUDENT
- Browse books and categories
- Borrow/return books and make reservations
- View personal borrowing history

## Security Notes

- Passwords use BCrypt (strength 10)
- Sample credentials are for dev/testing only
- In production, require password changes and policies

## Migration Details

- [V2__alter_users.sql](SE-Project/src/main/resources/db/migration/V2__alter_users.sql): Creates roles and initial sample users
- [V4__update_sample_users.sql](SE-Project/src/main/resources/db/migration/V4__update_sample_users.sql): Replaces admin with librarian (ID 1) and student (ID 3)
- [V6__promote_librarian_to_admin.sql](SE-Project/src/main/resources/db/migration/V6__promote_librarian_to_admin.sql): Grants ADMIN role to librarian

## Testing the Sample Users

Try logging in via the API:

```bash
# Login as Librarian (Admin privileges)
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"librarian@lms.com","password":"librarian123"}'

# Login as Student
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"student@lms.com","password":"student123"}'
```
