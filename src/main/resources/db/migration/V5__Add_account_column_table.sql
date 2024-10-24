DROP TABLE IF EXISTS s_account.m_account_columns;
CREATE TABLE IF NOT EXISTS s_account.m_account_columns
(
    id           SERIAL PRIMARY KEY,
    code         VARCHAR(25) NOT NULL UNIQUE,
    name         VARCHAR(25) NOT NULL UNIQUE,
    type         VARCHAR(15) NOT NULL,
    status       VARCHAR(12) NOT NULL DEFAULT 'ACTIVE',
    created_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by   VARCHAR(20) NOT NULL DEFAULT 'SYSTEM',
    updated_by   VARCHAR(20) NOT NULL DEFAULT 'SYSTEM'
);

INSERT INTO s_account.m_account_columns (code, name, type)
VALUES ('accountName', 'Account Name', 'text'),
       ('email', 'Email', 'text'),
       ('fullName', 'Full name', 'text'),
       ('birthdate', 'Birth Date', 'date'),
       ('creationTime', 'Date Created', 'date'),
       ('lastModified', 'Date Modified', 'date'),
       ('status', 'Status', 'text'),
       ('action', 'Action', 'text');

