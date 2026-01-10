-- ===============================
-- CONTACT / SUPPORT
-- ===============================

CREATE TABLE contacts (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,

                          user_id BIGINT NOT NULL,
                          subject VARCHAR(150) NOT NULL,
                          message TEXT NOT NULL,

                          status VARCHAR(30) NOT NULL DEFAULT 'OPEN',
                          admin_response TEXT,

                          created_at DATETIME,
                          responded_at DATETIME,

                          CONSTRAINT fk_contact_user FOREIGN KEY (user_id)
                              REFERENCES users(id)
);
