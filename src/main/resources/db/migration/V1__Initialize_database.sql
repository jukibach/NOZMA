CREATE SCHEMA s_account;

--- User table ---
CREATE TABLE IF NOT EXISTS s_account.t_users
(
    id           BIGSERIAL PRIMARY KEY,
    first_name   VARCHAR(20) NOT NULL,
    middle_name  VARCHAR(20),
    last_name    VARCHAR(20) NOT NULL,
    phone_number VARCHAR(11) NOT NULL UNIQUE,
    age          INT         NOT NULL,
    status       VARCHAR(12) NOT NULL DEFAULT 'ACTIVE',
    created_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by   VARCHAR(20) NOT NULL DEFAULT 'system',
    updated_by   VARCHAR(20) NOT NULL DEFAULT 'system'

);
-- Indexes for performance optimization with High Cardinality
CREATE INDEX idx_t_users_id ON s_account.t_users (id);
CREATE INDEX idx_t_users_first_name ON s_account.t_users (first_name);
CREATE INDEX idx_t_users_last_name ON s_account.t_users (last_name);
CREATE INDEX idx_t_users_phone_number ON s_account.t_users (phone_number);

--- Account table ---
CREATE TABLE IF NOT EXISTS s_account.t_accounts
(
    id           BIGSERIAL PRIMARY KEY,
    account_name VARCHAR(20)  NOT NULL UNIQUE,
    password     VARCHAR(100) NOT NULL,
    email        VARCHAR(200) NOT NULL UNIQUE,
    status       VARCHAR(12)  NOT NULL DEFAULT 'ACTIVE',
    is_locked    BOOLEAN      NOT NULL DEFAULT FALSE,
    user_id      BIGSERIAL    NOT NULL,
    created_date TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by   VARCHAR(20)  NOT NULL DEFAULT 'system',
    updated_by   VARCHAR(20)  NOT NULL DEFAULT 'system'
);
-- Indexes for performance optimization with High Cardinality
CREATE INDEX idx_t_accounts_id ON s_account.t_accounts (id);
CREATE INDEX idx_t_accounts_account_name ON s_account.t_accounts (account_name);
CREATE INDEX idx_t_accounts_email ON s_account.t_accounts (email);

-- Example data for t_users table
INSERT INTO s_account.t_users (first_name, middle_name, last_name, phone_number, age)
VALUES ('John', 'Robert', 'Doe', '1234567890', 30),
       ('Jane', NULL, 'Smith', '9876543210', 28),
       ('Michael', 'James', 'Williams', '5551234567', 35);

-- Example data for t_accounts table
INSERT INTO s_account.t_accounts (account_name, password, email, user_id)
VALUES ('john_doe', '$10$JVgrpiZ/QcSwxLqfLYHyq.lqb4SoTw3J9gPQEy5gpg6UD/bI7xXKa', 'john.doe@example.com', 1),
       ('jane_smith', '$10$JVgrpiZ/QcSwxLqfLYHyq.lqb4SoTw3J9gPQEy5gpg6UD/bI7xXKa', 'jane.smith@example.com', 2),
       ('michael_williams', '$10$JVgrpiZ/QcSwxLqfLYHyq.lqb4SoTw3J9gPQEy5gpg6UD/bI7xXKa', 'michael.williams@example.com',
        3);
