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
