--- Roles table ---
CREATE TABLE s_account.m_roles
(
    id           SERIAL PRIMARY KEY,
    name         VARCHAR(25) NOT NULL UNIQUE,
    description  VARCHAR(20),
    status       VARCHAR(12) NOT NULL DEFAULT 'ACTIVE',
    created_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by   VARCHAR(20) NOT NULL DEFAULT 'SYSTEM',
    updated_by   VARCHAR(20) NOT NULL DEFAULT 'SYSTEM'
);

CREATE INDEX idx_m_roles_id ON s_account.m_roles (id);

-- Insert default roles
INSERT INTO s_account.m_roles (name)
VALUES ('ROLE_ADMIN'),
       ('ROLE_PAID_USER'),
       ('ROLE_USER');

--- Privilege table ---
CREATE TABLE s_account.m_privileges
(
    id           SERIAL PRIMARY KEY,
    name         VARCHAR(40) NOT NULL,
    description  VARCHAR(20),
    status       VARCHAR(12) NOT NULL DEFAULT 'ACTIVE',
    created_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by   VARCHAR(20) NOT NULL DEFAULT 'SYSTEM',
    updated_by   VARCHAR(20) NOT NULL DEFAULT 'SYSTEM'
);

CREATE INDEX idx_m_privileges_id ON s_account.m_privileges (id);

-- Insert default privileges
INSERT INTO s_account.m_privileges (name)
VALUES ('GET_USERS'),
       ('GET_USER_DETAIL'),
       ('UPDATE_USER'),
       ('DELETE_USER');

--- Role-Privileges table ---
CREATE TABLE s_account.m_role_privileges
(
    id             BIGSERIAL PRIMARY KEY,
    role_id        INT         NOT NULL,
    privilege_id   INT         NOT NULL,
    role_name      VARCHAR(25) NOT NULL,
    privilege_name VARCHAR(40) NOT NULL,
    description    VARCHAR(20),
    status         VARCHAR(12) NOT NULL DEFAULT 'ACTIVE',
    created_date   TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date   TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by     VARCHAR(20) NOT NULL DEFAULT 'SYSTEM',
    updated_by     VARCHAR(20) NOT NULL DEFAULT 'SYSTEM'
);

CREATE INDEX idx_rp_role_id ON s_account.m_role_privileges (role_id);

-- Insert m_role_privileges data
INSERT INTO s_account.m_role_privileges (role_id, privilege_id, role_name, privilege_name)
VALUES (1, 1, 'ROLE_ADMIN', 'GET_USERS'),
       (1, 2, 'ROLE_ADMIN', 'GET_USER_DETAIL'),
       (1, 3, 'ROLE_ADMIN', 'UPDATE_USER'),
       (1, 4, 'ROLE_ADMIN', 'DELETE_USER'),

       (2, 2, 'ROLE_PAID_USER', 'GET_USER_DETAIL'),
       (2, 3, 'ROLE_PAID_USER', 'UPDATE_USER'),
       (2, 4, 'ROLE_PAID_USER', 'DELETE_USER'),

       (3, 2, 'ROLE_USER', 'GET_USER_DETAIL'),
       (3, 3, 'ROLE_USER', 'UPDATE_USER'),
       (3, 4, 'ROLE_USER', 'DELETE_USER');


-- Example data for t_accounts table
INSERT INTO s_account.t_accounts (account_name, password, email, user_id, role_id, role_name, from_date, to_date)
VALUES ('john_doe', '$2a$10$nqUNwmefC36uzYqdsynuI.wm4/Gn/n.wFezbXMIm/aNaCKfeeAMdC',
        'john.doe@example.com', 1, 1, 'ROLE_ADMIN',
        CURRENT_TIMESTAMP, '2034-08-26 06:25:07.164574 +00:00'),
       ('jane_smith', '$2a$10$nqUNwmefC36uzYqdsynuI.wm4/Gn/n.wFezbXMIm/aNaCKfeeAMdC',
        'jane.smith@example.com', 2, 2, 'ROLE_PAID_USER',
        CURRENT_TIMESTAMP, '2034-08-26 06:25:07.164574 +00:00'),
       ('michael_williams', '$2a$10$nqUNwmefC36uzYqdsynuI.wm4/Gn/n.wFezbXMIm/aNaCKfeeAMdC',
        'michael.williams@example.com', 3, 3, 'ROLE_USER',
        CURRENT_TIMESTAMP, '2034-08-26 06:25:07.164574 +00:00');
