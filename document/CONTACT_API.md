# Contact Management API Documentation

## Overview
The Contact Management System allows users to submit contact/support requests and enables administrators to manage and respond to these requests.

## Endpoints

### User Contact Endpoints

#### Create Contact Request
- **POST** `/api/contacts`
- **Description**: Create a new contact/support request
- **Parameters**:
  - `userId` (query parameter): ID of the user creating the request
- **Request Body**:
```json
{
  "subject": "Issue with book reservation",
  "message": "I'm having trouble reserving a specific book..."
}
```
- **Response**: Success response with contact details
- **Security**: Requires authentication

#### Get User's Contact Requests
- **GET** `/api/contacts/my?userId={userId}`
- **Description**: Retrieve all contact requests submitted by a specific user
- **Parameters**:
  - `userId` (query parameter): User ID to retrieve contacts for
- **Response**: List of contact responses
- **Security**: Requires authentication

### Admin Contact Endpoints

#### Get All Contact Requests
- **GET** `/api/admin/contacts`
- **Description**: Retrieve all contact requests in the system (Admin only)
- **Response**: List of all contact responses
- **Security**: Requires ADMIN or LIBRARIAN role

#### Update Contact Status
- **PUT** `/api/admin/contacts/{id}`
- **Description**: Update status and admin response of a contact request
- **Parameters**:
  - `id` (path parameter): Contact request ID
- **Request Body**:
```json
{
  "status": "RESOLVED",
  "adminResponse": "Your issue has been resolved. Please try again."
}
```
- **Response**: Success response with updated contact details
- **Security**: Requires ADMIN or LIBRARIAN role

#### Delete Contact Request
- **DELETE** `/api/admin/contacts/{id}`
- **Description**: Delete a contact request from the system
- **Parameters**:
  - `id` (path parameter): Contact request ID
- **Response**: Success confirmation
- **Security**: Requires ADMIN or LIBRARIAN role

## Contact Status Values
- `OPEN`: Newly created contact request
- `IN_PROGRESS`: Admin is working on the request
- `RESOLVED`: Issue has been resolved
- `CLOSED`: Request is closed

## Contact Response Format
```json
{
  "id": 1,
  "subject": "Issue with book reservation",
  "message": "I'm having trouble reserving a specific book...",
  "status": "OPEN",
  "adminResponse": null,
  "userName": "John Doe",
  "userEmail": "john.doe@example.com",
  "createdAt": "2025-12-22 10:30:00",
  "respondedAt": null
}
```

## Database Schema
The contacts table includes the following columns:
- `id`: Primary key (auto-increment)
- `user_id`: Foreign key to users table
- `subject`: Contact subject (max 150 characters)
- `message`: Contact message (TEXT)
- `status`: Contact status (ENUM)
- `admin_response`: Admin's response (TEXT)
- `created_at`: Creation timestamp (from BaseEntity)
- `updated_at`: Last update timestamp (from BaseEntity)
- `responded_at`: Timestamp when admin responded

## Error Handling
- **404 Not Found**: Contact or User not found
- **400 Bad Request**: Invalid input data
- **401 Unauthorized**: Authentication required
- **403 Forbidden**: Insufficient permissions
- **409 Conflict**: Data integrity violation

## Example Usage

### Creating a Contact Request
```bash
curl -X POST "http://localhost:8080/api/contacts?userId=1" \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "subject": "Book Return Issue",
    "message": "I returned a book but it still shows as checked out"
  }'
```

### Admin Responding to Contact
```bash
curl -X PUT "http://localhost:8080/api/admin/contacts/1" \
  -H "Authorization: Bearer <admin-token>" \
  -H "Content-Type: application/json" \
  -d '{
    "status": "RESOLVED",
    "adminResponse": "Thank you for reporting this. The issue has been fixed in our system."
  }'
```
