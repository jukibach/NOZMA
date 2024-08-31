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
       ('mechanic', 'Mechanic', NULL, 'text'),
       ('bodyRegion', 'Body Region', NULL, 'text'),
       ('laterality', 'Laterality', NULL, 'text'),
       ('movementPatterns', 'Movement Patterns', 'Movement patterns associated with the exercise', 'multiSelect'),
       ('description', 'Description', NULL, 'multilineText');

CREATE TABLE s_workout.t_user_exercise_settings
(
    id           BIGSERIAL PRIMARY KEY,
    account_id   BIGINT      NOT NULL,
    code         VARCHAR(25) NOT NULL,
    settings     jsonb       NOT NULL,
    description  TEXT,
    status       VARCHAR(12) NOT NULL DEFAULT 'ACTIVE',
    created_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by   VARCHAR(20) NOT NULL DEFAULT 'SYSTEM',
    updated_by   VARCHAR(20) NOT NULL DEFAULT 'SYSTEM'
);

-- Insert for account_id 1
INSERT INTO s_workout.t_user_exercise_settings (account_id, code, settings)
VALUES (1, 'exercises', '
{
  "name": true,
  "equipments": true,
  "exerciseTypes": true,
  "majorMuscle": true,
  "muscleGroup": true,
  "mechanics": true,
  "bodyRegion": true,
  "laterality": true,
  "movementPatterns": true,
  "description": true
}'),
       (2, 'exercises', '
       {
         "name": true,
         "equipments": true,
         "exerciseTypes": true,
         "majorMuscle": true,
         "muscleGroup": true,
         "mechanics": true,
         "bodyRegion": true,
         "laterality": true,
         "movementPatterns": true,
         "description": true
       }
       ');
