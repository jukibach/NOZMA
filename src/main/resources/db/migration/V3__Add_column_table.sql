CREATE TABLE s_workout.m_exercise_columns
(
    id           SERIAL PRIMARY KEY,
    code         VARCHAR(25) NOT NULL UNIQUE,
    name         VARCHAR(25) NOT NULL UNIQUE,
    description  TEXT,
    type         VARCHAR(15) NOT NULL,
    status       VARCHAR(12) NOT NULL DEFAULT 'ACTIVE',
    created_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by   VARCHAR(20) NOT NULL DEFAULT 'SYSTEM',
    updated_by   VARCHAR(20) NOT NULL DEFAULT 'SYSTEM'
);
-- 1 = text, 2 = multi select, 3 = multiline text
INSERT INTO s_workout.m_exercise_columns (code, name, description, type)
VALUES ('name', 'Exercise', 'Description of the exercise', 'text'),
       ('equipments', 'Equipments', 'Equipment needed for the exercise', 'multiSelect'),
       ('exerciseTypes', 'Exercise Type', 'Type of exercise (e.g., strength, cardio)', 'multiSelect'),
       ('majorMuscle', 'Major Muscle', 'Primary muscle targeted by the exercise', 'text'),
       ('muscleGroup', 'Muscle Group', 'Group of muscles involved in the exercise', 'multiSelect'),
       ('mechanics', 'Mechanic', NULL, 'text'),
       ('bodyRegion', 'Body Region', NULL, 'text'),
       ('laterality', 'Laterality', NULL, 'text'),
       ('movementPatterns', 'Movement Patterns', 'Movement patterns associated with the exercise', 'multiSelect'),
       ('description', 'Description', NULL, 'multilineText');

CREATE TABLE s_workout.m_languages
(
    id            SERIAL PRIMARY KEY,
    language_code VARCHAR(7)  NOT NULL UNIQUE,
    display_name  VARCHAR(15) NOT NULL UNIQUE,
    status        VARCHAR(12) NOT NULL DEFAULT 'ACTIVE',
    created_date  TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date  TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by    VARCHAR(20) NOT NULL DEFAULT 'SYSTEM',
    updated_by    VARCHAR(20) NOT NULL DEFAULT 'SYSTEM'
);
INSERT INTO s_workout.m_languages (id, language_code, display_name)
VALUES (1, 'en', 'English'),
       (2, 'vn', 'Tiếng Việt');

CREATE TABLE s_workout.m_exercise_column_translations
(
    id                 SERIAL PRIMARY KEY,
    exercise_column_id INTEGER     NOT NULL,
    language_id        INTEGER     NOT NULL,
    localized_name     VARCHAR(25) NOT NULL UNIQUE,
    description        TEXT,
    status             VARCHAR(12) NOT NULL DEFAULT 'ACTIVE',
    created_date       TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date       TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by         VARCHAR(20) NOT NULL DEFAULT 'SYSTEM',
    updated_by         VARCHAR(20) NOT NULL DEFAULT 'SYSTEM'
);

INSERT INTO s_workout.m_exercise_column_translations (language_id, exercise_column_id, description, localized_name)
VALUES (2, 1, 'Description of the exercise', 'Bài tập'),
       (2, 2, 'Equipment needed for the exercise', 'Thiết bị'),
       (2, 3, 'Type of exercise (e.g., strength, cardio)', 'Loại'),
       (2, 4, 'Primary muscle targeted by the exercise', 'Nhóm cơ chính'),
       (2, 5, 'Group of muscles involved in the exercise', 'Nhóm cơ liên quan'),
       (2, 6, NULL, 'Cơ chế'),
       (2, 7, NULL, 'Phần thân'),
       (2, 8, NULL, 'Phía thuận'),
       (2, 9, 'Movement patterns associated with the exercise', 'Kiểu chuyển động'),
       (2, 10, 'Description', 'Mô tả');

CREATE TABLE s_workout.t_display_exercise_settings
(
    id                BIGSERIAL PRIMARY KEY,
    account_id        BIGINT      NOT NULL,
    code              VARCHAR(25) NOT NULL,
    name              BOOLEAN     NOT NULL DEFAULT TRUE,
    body_region       BOOLEAN     NOT NULL DEFAULT TRUE,
    laterality        BOOLEAN     NOT NULL DEFAULT TRUE,
    major_muscle      BOOLEAN     NOT NULL DEFAULT TRUE,
    mechanics         BOOLEAN     NOT NULL DEFAULT TRUE,
    equipments        BOOLEAN     NOT NULL DEFAULT TRUE,
    exercise_types    BOOLEAN     NOT NULL DEFAULT TRUE,
    muscle_group      BOOLEAN     NOT NULL DEFAULT TRUE,
    movement_patterns BOOLEAN     NOT NULL DEFAULT TRUE,
    description       BOOLEAN     NOT NULL DEFAULT TRUE,
    status            VARCHAR(12) NOT NULL DEFAULT 'ACTIVE',
    created_date      TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date      TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by        VARCHAR(20) NOT NULL DEFAULT 'SYSTEM',
    updated_by        VARCHAR(20) NOT NULL DEFAULT 'SYSTEM'
);

INSERT INTO s_workout.t_display_exercise_settings (account_id, code)
VALUES (1, 'exercises'),
       (2, 'exercises')
