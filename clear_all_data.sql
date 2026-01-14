-- Clear all data to allow book deletion
-- Execute in order to respect foreign key constraints

-- 1. Delete all reservations
DELETE FROM reservations;

-- 2. Delete all borrow records
DELETE FROM borrow_records;

-- 3. Delete all book copies
DELETE FROM book_copies;

-- Now you can delete books without constraint violations
