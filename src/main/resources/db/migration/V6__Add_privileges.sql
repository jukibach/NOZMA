-- Insert default privileges
INSERT INTO s_account.m_privileges (name)
VALUES ('UPDATE_DISPLAY_EXERCISE_SETTING');

INSERT INTO s_account.m_role_privileges (role_id, privilege_id, role_name, privilege_name)
VALUES (1, 5, 'ROLE_ADMIN', 'UPDATE_DISPLAY_EXERCISE_SETTING'),
       (2, 5, 'ROLE_PAID_USER', 'UPDATE_DISPLAY_EXERCISE_SETTING'),
       (3, 5, 'ROLE_USER', 'UPDATE_DISPLAY_EXERCISE_SETTING');