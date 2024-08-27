CREATE SCHEMA s_account;

--- User table ---
CREATE TABLE IF NOT EXISTS s_account.t_users
(
    id           BIGSERIAL PRIMARY KEY,
    first_name   VARCHAR(20) NOT NULL,
    middle_name  VARCHAR(20),
    last_name    VARCHAR(20) NOT NULL,
    phone_number VARCHAR(11) NOT NULL UNIQUE,
    status       VARCHAR(12) NOT NULL DEFAULT 'ACTIVE',
    birthdate    VARCHAR(10),
    created_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by   VARCHAR(20) NOT NULL DEFAULT 'SYSTEM',
    updated_by   VARCHAR(20) NOT NULL DEFAULT 'SYSTEM'

);
-- Indexes for performance optimization with High Cardinality
CREATE INDEX idx_t_users_id ON s_account.t_users (id);
CREATE INDEX idx_t_users_first_name ON s_account.t_users (first_name);
CREATE INDEX idx_t_users_last_name ON s_account.t_users (last_name);
CREATE INDEX idx_t_users_phone_number ON s_account.t_users (phone_number);

--- Account table ---
CREATE TABLE IF NOT EXISTS s_account.t_accounts
(
    id                    BIGSERIAL PRIMARY KEY,
    account_name          VARCHAR(20)  NOT NULL UNIQUE,
    password              VARCHAR(100) NOT NULL,
    email                 VARCHAR(200) NOT NULL UNIQUE,
    status                VARCHAR(12)  NOT NULL DEFAULT 'ACTIVE',
    is_locked             BOOLEAN      NOT NULL DEFAULT FALSE,
    user_id               BIGINT       NOT NULL,
    role_id               INT          NOT NULL,
    role_name             VARCHAR(25)  NOT NULL,
    last_locked           TIMESTAMPTZ,
    from_date             TIMESTAMPTZ,
    to_date               TIMESTAMPTZ,
    is_password_generated BOOLEAN      NOT NULL DEFAULT FALSE,
    created_date          TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date          TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by            VARCHAR(20)  NOT NULL DEFAULT 'SYSTEM',
    updated_by            VARCHAR(20)  NOT NULL DEFAULT 'SYSTEM'
);
-- Indexes for performance optimization with High Cardinality
CREATE INDEX idx_t_accounts_id ON s_account.t_accounts (id);
CREATE INDEX idx_t_accounts_account_name ON s_account.t_accounts (account_name);
CREATE INDEX idx_t_accounts_email ON s_account.t_accounts (email);

-- Example data for t_users table
INSERT INTO s_account.t_users (first_name, middle_name, last_name, phone_number, birthdate)
VALUES ('John', 'Robert', 'Doe', '1234567890', '01-01-2000'),
       ('Jane', NULL, 'Smith', '9876543210', '01-01-2000'),
       ('Michael', 'James', 'Williams', '5551234567', '01-01-2000');