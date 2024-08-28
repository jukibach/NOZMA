CREATE TABLE s_workout.m_exercise_columns
(
    id           SERIAL PRIMARY KEY,
    name         VARCHAR(25) NOT NULL UNIQUE,
    description  TEXT,
    type         INT         NOT NULL,
    status       VARCHAR(12) NOT NULL DEFAULT 'ACTIVE',
    created_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by   VARCHAR(20) NOT NULL DEFAULT 'SYSTEM',
    updated_by   VARCHAR(20) NOT NULL DEFAULT 'SYSTEM'
);
-- 1 = text, 2 = multi select, 3 = multiline text
INSERT INTO s_workout.m_exercise_columns (name, description, type)
VALUES ('Exercise', 'Description of the exercise', 1),
       ('Equipment', 'Equipment needed for the exercise', 2),
       ('Exercise Type', 'Type of exercise (e.g., strength, cardio)', 2),
       ('Major Muscle', 'Primary muscle targeted by the exercise', 1),
       ('Muscle Group', 'Group of muscles involved in the exercise', 1),
       ('Mechanic', NULL, 1),
       ('Body Region', NULL, 1),
       ('Laterality', NULL, 1),
       ('Movement Patterns', 'Movement patterns associated with the exercise', 2),
       ('Description', NULL, 3);
