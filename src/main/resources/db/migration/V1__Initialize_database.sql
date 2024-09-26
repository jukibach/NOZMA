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

--- Roles table ---
CREATE TABLE s_account.m_roles
(
    id           SERIAL PRIMARY KEY,
    name         VARCHAR(25) NOT NULL UNIQUE,
    description  TEXT,
    status       VARCHAR(12) NOT NULL DEFAULT 'ACTIVE',
    created_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by   VARCHAR(20) NOT NULL DEFAULT 'SYSTEM',
    updated_by   VARCHAR(20) NOT NULL DEFAULT 'SYSTEM'
);

CREATE INDEX idx_m_roles_id ON s_account.m_roles (id);

-- Insert default roles
INSERT INTO s_account.m_roles (id, name)
VALUES (1, 'ROLE_ADMIN'),
       (2, 'ROLE_PAID_USER'),
       (3, 'ROLE_USER');

--- Privilege table ---
CREATE TABLE s_account.m_privileges
(
    id           SERIAL PRIMARY KEY,
    name         VARCHAR(40) NOT NULL,
    description  TEXT,
    status       VARCHAR(12) NOT NULL DEFAULT 'ACTIVE',
    created_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by   VARCHAR(20) NOT NULL DEFAULT 'SYSTEM',
    updated_by   VARCHAR(20) NOT NULL DEFAULT 'SYSTEM'
);

CREATE INDEX idx_m_privileges_id ON s_account.m_privileges (id);

-- Insert default privileges
INSERT INTO s_account.m_privileges (id, name)
VALUES (1, 'GET_USERS'),
       (2, 'GET_USER_DETAIL'),
       (3, 'UPDATE_USER'),
       (4, 'DELETE_USER');

--- Role-Privileges table ---
CREATE TABLE s_account.m_role_privileges
(
    id           BIGSERIAL PRIMARY KEY,
    role_id      INT         NOT NULL REFERENCES s_account.m_roles,
    privilege_id INT         NOT NULL REFERENCES s_account.m_privileges,
    description  TEXT,
    status       VARCHAR(12) NOT NULL DEFAULT 'ACTIVE',
    created_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by   VARCHAR(20) NOT NULL DEFAULT 'SYSTEM',
    updated_by   VARCHAR(20) NOT NULL DEFAULT 'SYSTEM'
);

CREATE INDEX idx_rp_role_id ON s_account.m_role_privileges (role_id);

-- Insert m_role_privileges data
INSERT INTO s_account.m_role_privileges (role_id, privilege_id)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (1, 4),

       (2, 2),
       (2, 3),
       (2, 4),

       (3, 2),
       (3, 3),
       (3, 4);

--- Account table ---
CREATE TABLE IF NOT EXISTS s_account.t_accounts
(
    id                    BIGSERIAL PRIMARY KEY,
    account_name          VARCHAR(20)  NOT NULL UNIQUE,
    password              VARCHAR(100) NOT NULL,
    email                 VARCHAR(200) NOT NULL UNIQUE,
    status                VARCHAR(12)  NOT NULL DEFAULT 'ACTIVE',
    is_locked             BOOLEAN      NOT NULL DEFAULT FALSE,
    user_id               BIGINT       NOT NULL REFERENCES s_account.t_users,
    role_id               INT          NOT NULL REFERENCES s_account.m_roles,
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

-- Example data for t_accounts table
INSERT INTO s_account.t_accounts (account_name, password, email, user_id, role_id, from_date, to_date)
VALUES ('john_doe', '$2a$10$nqUNwmefC36uzYqdsynuI.wm4/Gn/n.wFezbXMIm/aNaCKfeeAMdC',
        'john.doe@example.com', 1, 1,
        CURRENT_TIMESTAMP, '2034-08-26 06:25:07.164574 +00:00'),
       ('jane_smith', '$2a$10$nqUNwmefC36uzYqdsynuI.wm4/Gn/n.wFezbXMIm/aNaCKfeeAMdC',
        'jane.smith@example.com', 2, 2,
        CURRENT_TIMESTAMP, '2034-08-26 06:25:07.164574 +00:00'),
       ('michael_williams', '$2a$10$nqUNwmefC36uzYqdsynuI.wm4/Gn/n.wFezbXMIm/aNaCKfeeAMdC',
        'michael.williams@example.com', 3, 3,
        CURRENT_TIMESTAMP, '2034-08-26 06:25:07.164574 +00:00');

--- Account History table ---
CREATE TABLE s_account.t_password_histories
(
    id           BIGSERIAL PRIMARY KEY,
    account_id   BIGINT       NOT NULL REFERENCES s_account.t_accounts,
    password     VARCHAR(100) NOT NULL,
    from_date    TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    to_date      TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status       VARCHAR(12)  NOT NULL DEFAULT 'ACTIVE',
    created_date TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by   VARCHAR(20)  NOT NULL DEFAULT 'SYSTEM',
    updated_by   VARCHAR(20)  NOT NULL DEFAULT 'SYSTEM'
);
-- Indexes for performance optimization with High Cardinality
CREATE INDEX idx_t_password_history_id ON s_account.t_password_histories (id);
CREATE INDEX idx_t_password_history_password ON s_account.t_password_histories (password);
