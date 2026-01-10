-- Add description and image_url columns to books
ALTER TABLE books
    ADD COLUMN description TEXT NULL,
    ADD COLUMN image_url VARCHAR(512) NULL;