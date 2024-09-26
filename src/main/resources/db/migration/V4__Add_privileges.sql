-- Insert default privileges
INSERT INTO s_account.m_privileges (id, name)
VALUES (5, 'UPDATE_DISPLAY_EXERCISE_SETTING');

INSERT INTO s_account.m_role_privileges (role_id, privilege_id)
VALUES (1, 5),
       (2, 5),
       (3, 5);