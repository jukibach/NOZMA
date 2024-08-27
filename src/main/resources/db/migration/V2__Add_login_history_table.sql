--- Login History table ---
CREATE TABLE s_account.t_login_histories
(
    id                  BIGSERIAL PRIMARY KEY,
    user_id             BIGINT      NOT NULL,
    account_id          BIGINT      NOT NULL,
    account_name        VARCHAR(20) NOT NULL,
    is_login_successful BOOLEAN     NOT NULL DEFAULT TRUE,
    failure_reason      VARCHAR(100),
    ip_address          VARCHAR(13) NOT NULL,
    device_type         VARCHAR(7)  NOT NULL,
    device_os           VARCHAR(10) NOT NULL,
    browser_name        VARCHAR(12) NOT NULL,
    browser_version     VARCHAR(8)  NOT NULL,
    status              VARCHAR(12) NOT NULL DEFAULT 'ACTIVE',
    login_timestamp     TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_date        TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date        TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by          VARCHAR(20) NOT NULL DEFAULT 'SYSTEM',
    updated_by          VARCHAR(20) NOT NULL DEFAULT 'SYSTEM'
);
-- Indexes for performance optimization with High Cardinality
CREATE INDEX idx_t_login_histories_id ON s_account.t_login_histories (id);
CREATE INDEX idx_t_login_histories_login_timestamp ON s_account.t_login_histories (login_timestamp);
CREATE INDEX idx_t_login_histories_ip_address ON s_account.t_login_histories (ip_address);

--- Account History table ---
CREATE TABLE s_account.t_password_histories
(
    id           BIGSERIAL PRIMARY KEY,
    account_id   BIGINT       NOT NULL,
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
