-- ===============================
-- USERS & SECURITY
-- ===============================

CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(100) NOT NULL,
                       email VARCHAR(150) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       enabled BOOLEAN DEFAULT TRUE,
                       created_at DATETIME,
                       updated_at DATETIME
);

CREATE TABLE roles (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE user_roles (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            user_id BIGINT NOT NULL,
                            role_id BIGINT NOT NULL,
                            CONSTRAINT fk_ur_user FOREIGN KEY (user_id) REFERENCES users(id),
                            CONSTRAINT fk_ur_role FOREIGN KEY (role_id) REFERENCES roles(id),
                            CONSTRAINT uk_user_role UNIQUE (user_id, role_id)
);

CREATE TABLE refresh_tokens (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                token VARCHAR(255) NOT NULL UNIQUE,
                                user_id BIGINT NOT NULL UNIQUE,
                                expiry_date DATETIME NOT NULL,
                                CONSTRAINT fk_rt_user FOREIGN KEY (user_id) REFERENCES users(id)
);

-- ===============================
-- LIBRARY DOMAIN
-- ===============================

CREATE TABLE book_categories (
                                 id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 name VARCHAR(100) NOT NULL UNIQUE,
                                 description VARCHAR(255)
);

CREATE TABLE books (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       title VARCHAR(200) NOT NULL,
                       author VARCHAR(150) NOT NULL,
                       isbn VARCHAR(50) UNIQUE,
                       category_id BIGINT,
                       created_at DATETIME,
                       updated_at DATETIME,
                       CONSTRAINT fk_book_category FOREIGN KEY (category_id)
                           REFERENCES book_categories(id)
);

CREATE TABLE book_copies (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             book_id BIGINT NOT NULL,
                             copy_code VARCHAR(100) UNIQUE,
                             available BOOLEAN DEFAULT TRUE,
                             CONSTRAINT fk_copy_book FOREIGN KEY (book_id)
                                 REFERENCES books(id)
);

CREATE TABLE reservations (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              user_id BIGINT NOT NULL,
                              book_id BIGINT NOT NULL,
                              reservation_date DATETIME,
                              status VARCHAR(30),
                              CONSTRAINT fk_res_user FOREIGN KEY (user_id)
                                  REFERENCES users(id),
                              CONSTRAINT fk_res_book FOREIGN KEY (book_id)
                                  REFERENCES books(id)
);

-- ===============================
-- BORROW & TRACKING
-- ===============================

CREATE TABLE borrow_records (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                user_id BIGINT NOT NULL,
                                book_copy_id BIGINT NOT NULL,
                                borrow_date DATETIME,
                                due_date DATETIME,
                                return_date DATETIME,
                                status VARCHAR(30),
                                CONSTRAINT fk_br_user FOREIGN KEY (user_id)
                                    REFERENCES users(id),
                                CONSTRAINT fk_br_copy FOREIGN KEY (book_copy_id)
                                    REFERENCES book_copies(id)
);

CREATE TABLE fines (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       borrow_record_id BIGINT UNIQUE,
                       amount DECIMAL(10,2),
                       paid BOOLEAN DEFAULT FALSE,
                       CONSTRAINT fk_fine_br FOREIGN KEY (borrow_record_id)
                           REFERENCES borrow_records(id)
);

-- ===============================
-- SYSTEM
-- ===============================

CREATE TABLE notifications (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               user_id BIGINT NOT NULL,
                               message VARCHAR(255),
                               read_flag BOOLEAN DEFAULT FALSE,
                               created_at DATETIME,
                               CONSTRAINT fk_notify_user FOREIGN KEY (user_id)
                                   REFERENCES users(id)
);

CREATE TABLE audit_logs (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            user_id BIGINT,
                            action VARCHAR(255),
                            created_at DATETIME,
                            CONSTRAINT fk_audit_user FOREIGN KEY (user_id)
                                REFERENCES users(id)
);
