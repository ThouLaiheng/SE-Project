# Sample Users Documentation

This file documents the sample users created by the V2__alter_users.sql migration.

## Sample Users Created

The migration creates three sample users with different roles for testing purposes:

### 1. Administrator User
- **ID**: 1
- **Name**: System Administrator  
- **Email**: admin@lms.com
- **Password**: admin123
- **Role**: ADMIN
- **Status**: Enabled

### 2. Librarian User
- **ID**: 2
- **Name**: Head Librarian
- **Email**: librarian@lms.com  
- **Password**: librarian123
- **Role**: LIBRARIAN
- **Status**: Enabled

### 3. Student User
- **ID**: 3
- **Name**: Demo Student
- **Email**: student@lms.com
- **Password**: student123
- **Role**: STUDENT  
- **Status**: Enabled

## Role Permissions

### ADMIN
- Full system access
- Can manage users, books, categories, and system settings
- Can view all reports and audit logs

### LIBRARIAN
- Can manage books and categories
- Can handle book borrowing and returns
- Can manage reservations and fines
- Can view borrowing reports

### STUDENT
- Can browse books and categories
- Can borrow and return books
- Can make reservations
- Can view personal borrowing history

## Security Notes

- All passwords are BCrypt encrypted with strength 10
- The sample passwords are for development/testing only
- In production, ensure users change default passwords
- Consider implementing password policy requirements

## Migration Details

The V2__alter_users.sql migration:
1. Creates the required roles (ADMIN, LIBRARIAN, STUDENT)
2. Inserts the sample users with encrypted passwords
3. Assigns appropriate roles to each user
4. Uses `INSERT IGNORE` to prevent conflicts if users already exist
5. Clears existing user roles before reassigning them

## Testing the Sample Users

You can test the API endpoints with these users:

```bash
# Login as Admin
curl -X POST http://localhost:9090/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@lms.com","password":"admin123"}'

# Login as Librarian  
curl -X POST http://localhost:9090/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"librarian@lms.com","password":"librarian123"}'

# Login as Student
curl -X POST http://localhost:9090/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"student@lms.com","password":"student123"}'
```
