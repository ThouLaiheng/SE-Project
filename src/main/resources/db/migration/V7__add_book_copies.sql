-- Add book copies to make books available for borrowing
-- This will add 3 copies for each existing book in the database

-- Insert book copies for all existing books
-- Each book will get 3 copies with unique copy codes
INSERT INTO book_copies (book_id, copy_code, available)
SELECT 
    b.id,
    CONCAT('COPY-', b.id, '-001'),
    TRUE
FROM books b
WHERE NOT EXISTS (
    SELECT 1 FROM book_copies bc 
    WHERE bc.book_id = b.id AND bc.copy_code = CONCAT('COPY-', b.id, '-001')
);

INSERT INTO book_copies (book_id, copy_code, available)
SELECT 
    b.id,
    CONCAT('COPY-', b.id, '-002'),
    TRUE
FROM books b
WHERE NOT EXISTS (
    SELECT 1 FROM book_copies bc 
    WHERE bc.book_id = b.id AND bc.copy_code = CONCAT('COPY-', b.id, '-002')
);

INSERT INTO book_copies (book_id, copy_code, available)
SELECT 
    b.id,
    CONCAT('COPY-', b.id, '-003'),
    TRUE
FROM books b
WHERE NOT EXISTS (
    SELECT 1 FROM book_copies bc 
    WHERE bc.book_id = b.id AND bc.copy_code = CONCAT('COPY-', b.id, '-003')
);
