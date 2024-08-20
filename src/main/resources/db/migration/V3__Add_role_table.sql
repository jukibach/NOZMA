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
       ('ROLE_SELLER'),
       ('ROLE_USER'),
       ('ROLE_CUSTOMER_SUPPORT');

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
       ('GET_USER_DETAIL');

--- Role-Privileges table ---
CREATE TABLE s_account.m_role_privileges
(
    id           BIGSERIAL PRIMARY KEY,
    role_id      INT         NOT NULL,
    privilege_id INT         NOT NULL,
    description  VARCHAR(20),
    status       VARCHAR(12) NOT NULL DEFAULT 'ACTIVE',
    created_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by   VARCHAR(20) NOT NULL DEFAULT 'SYSTEM',
    updated_by   VARCHAR(20) NOT NULL DEFAULT 'SYSTEM'
);

CREATE INDEX idx_rp_role_id ON s_account.m_role_privileges (role_id);

-- Insert m_role_privileges data
INSERT INTO s_account.m_role_privileges (role_id, privilege_id)
VALUES (1, 1), -- ROLE_ADMIN has GET_USERS privilege
       (1, 2), -- ROLE_ADMIN has GET_USER_DETAIL privilege
       (2, 1), -- ROLE_SELLER has GET_USERS privilege
       (3, 2), -- ROLE_USER has GET_USER_DETAIL privilege
       (4, 1), -- ROLE_CUSTOMER_SUPPORT has GET_USERS privilege
       (4, 2);
-- ROLE_CUSTOMER_SUPPORT has GET_USER_DETAIL privilege

--- User-Roles table ---
CREATE TABLE s_account.t_account_roles
(
    id           SERIAL PRIMARY KEY,
    account_id   BIGINT      NOT NULL,
    role_id      INT         NOT NULL,
    status       VARCHAR(12) NOT NULL DEFAULT 'ACTIVE',
    created_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by   VARCHAR(20) NOT NULL DEFAULT 'SYSTEM',
    updated_by   VARCHAR(20) NOT NULL DEFAULT 'SYSTEM'
);

CREATE INDEX idx_st_account_id ON s_account.t_account_roles (account_id);

INSERT INTO s_account.t_account_roles (account_id, role_id)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (1, 4),
       (2, 2),
       (2, 3),
       (2, 4),
       (3, 3),
       (3, 4),
       (4, 3);

