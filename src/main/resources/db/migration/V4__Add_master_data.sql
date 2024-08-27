CREATE SCHEMA s_workout;

CREATE TABLE s_workout.m_major_muscles
(
    id           SERIAL PRIMARY KEY,
    name         VARCHAR(25) NOT NULL UNIQUE,
    description  TEXT,
    status       VARCHAR(12) NOT NULL DEFAULT 'ACTIVE',
    created_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by   VARCHAR(20) NOT NULL DEFAULT 'SYSTEM',
    updated_by   VARCHAR(20) NOT NULL DEFAULT 'SYSTEM'
);

INSERT INTO s_workout.m_major_muscles(name)
VALUES ('Core'),
       ('Arms'),
       ('Back'),
       ('Chest'),
       ('Shoulders'),
       ('Legs'),
       ('Other'),
       ('Full Body');

CREATE TABLE s_workout.m_categories
(
    id           SERIAL PRIMARY KEY,
    name         VARCHAR(25) NOT NULL,
    description  TEXT,
    status       VARCHAR(12) NOT NULL DEFAULT 'ACTIVE',
    created_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by   VARCHAR(20) NOT NULL DEFAULT 'SYSTEM',
    updated_by   VARCHAR(20) NOT NULL DEFAULT 'SYSTEM'
);

INSERT INTO s_workout.m_categories(name)
VALUES ('Cardio'),
       ('Weight'),
       ('Plyo'),
       ('Machine');

CREATE TABLE s_workout.m_equipments
(
    id           SERIAL PRIMARY KEY,
    name         VARCHAR(25) NOT NULL UNIQUE,
    description  TEXT,
    status       VARCHAR(12) NOT NULL DEFAULT 'ACTIVE',
    created_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by   VARCHAR(20) NOT NULL DEFAULT 'SYSTEM',
    updated_by   VARCHAR(20) NOT NULL DEFAULT 'SYSTEM'
);

INSERT INTO s_workout.m_equipments(name)
VALUES ('Band'),
       ('Dumbbells'),
       ('Bench'),
       ('Barbell'),
       ('Bar'),
       ('Cable'),
       ('Body Weight'),
       ('Platform'),
       ('Machine'),
       ('Kettle Bells'),
       ('Landmine'),
       ('Squat Rack'),
       ('Rope'),
       ('Medicine Ball');

CREATE TABLE s_workout.m_muscle_group
(
    id           SERIAL PRIMARY KEY,
    name         VARCHAR(25) NOT NULL UNIQUE,
    description  TEXT,
    status       VARCHAR(12) NOT NULL DEFAULT 'ACTIVE',
    created_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by   VARCHAR(20) NOT NULL DEFAULT 'SYSTEM',
    updated_by   VARCHAR(20) NOT NULL DEFAULT 'SYSTEM'
);

INSERT INTO s_workout.m_muscle_group(name)
VALUES ('Bicep'),
       ('Outer Thigh'),
       ('Glutes'),
       ('Hamstrings'),
       ('Quads'),
       ('Calves'),
       ('Upper Chest'),
       ('Inner Thigh'),
       ('Medium Chest'),
       ('Triceps'),
       ('Lats'),
       ('Oblique'),
       ('Lateral Delts'),
       ('Anterior Delts'),
       ('Rear Delts'),
       ('Traps'),
       ('Upper Back'),
       ('Lower Back'),
       ('Neck'),
       ('Forearms'),
       ('Hip Flexors'),
       ('Full Body');

CREATE TABLE s_workout.t_exercises
(
    id           SERIAL,
    name         VARCHAR(35) NOT NULL,
    major_muscle VARCHAR(25) NOT NULL,
    mechanics    VARCHAR(15) NOT NULL,
    body_region  VARCHAR(25) NOT NULL,
    laterality   VARCHAR(25) NOT NULL,
    is_weight    BOOLEAN     NOT NULL DEFAULT FALSE,
    is_cardio    BOOLEAN     NOT NULL DEFAULT FALSE,
    is_plyo      BOOLEAN     NOT NULL DEFAULT FALSE,
    description  TEXT,
    status       VARCHAR(12) NOT NULL DEFAULT 'ACTIVE',
    created_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by   VARCHAR(20) NOT NULL DEFAULT 'SYSTEM',
    updated_by   VARCHAR(20) NOT NULL DEFAULT 'SYSTEM',

    PRIMARY KEY (id, major_muscle),
    CONSTRAINT UK_tracking_id UNIQUE (major_muscle, name)

) PARTITION BY LIST (major_muscle);

-- Core
CREATE TABLE s_workout.t_exercises_core PARTITION OF s_workout.t_exercises
    FOR VALUES IN ('Core');

-- Arm
CREATE TABLE s_workout.t_exercises_arm PARTITION OF s_workout.t_exercises
    FOR VALUES IN ('Arms');

-- Back
CREATE TABLE s_workout.t_exercises_back PARTITION OF s_workout.t_exercises
    FOR VALUES IN ('Back');

-- Chest
CREATE TABLE s_workout.t_exercises_chest PARTITION OF s_workout.t_exercises
    FOR VALUES IN ('Chest');

-- Shoulders
CREATE TABLE s_workout.t_exercises_shoulders PARTITION OF s_workout.t_exercises
    FOR VALUES IN ('Shoulders');

-- Legs
CREATE TABLE s_workout.t_exercises_legs PARTITION OF s_workout.t_exercises
    FOR VALUES IN ('Legs');

-- Other
CREATE TABLE s_workout.t_exercises_other PARTITION OF s_workout.t_exercises
    FOR VALUES IN ('Other');

-- Full Body
CREATE TABLE s_workout.t_exercises_full_body PARTITION OF s_workout.t_exercises
    FOR VALUES IN ('Full Body');

-- Index for Core partition
CREATE INDEX idx_t_exercises_core_name ON s_workout.t_exercises_core (name);

-- Index for Arm partition
CREATE INDEX idx_t_exercises_arm_name ON s_workout.t_exercises_arm (name);

-- Index for Back partition
CREATE INDEX idx_t_exercises_back_name ON s_workout.t_exercises_back (name);

-- Index for Chest partition
CREATE INDEX idx_t_exercises_chest_name ON s_workout.t_exercises_chest (name);

-- Index for Shoulders partition
CREATE INDEX idx_t_exercises_shoulders_name ON s_workout.t_exercises_shoulders (name);

-- Index for Legs partition
CREATE INDEX idx_t_exercises_legs_name ON s_workout.t_exercises_legs (name);

-- Index for Other partition
CREATE INDEX idx_t_exercises_other_name ON s_workout.t_exercises_other (name);

-- Index for Full Body partition
CREATE INDEX idx_t_exercises_full_body_name ON s_workout.t_exercises_full_body (name);

INSERT INTO s_workout.t_exercises(name, mechanics, body_region, major_muscle, laterality, is_weight, is_cardio, is_plyo)
VALUES ('Arnold Press', 'Compound', 'Upper Body', 'Shoulders', 'Bilateral', TRUE, FALSE, FALSE),
       ('Bicep Curl', 'Isolation', 'Upper Body', 'Arms', 'Bilateral', TRUE, FALSE, FALSE),
       ('Bicycle Crunch', 'Isolation', 'Midsection', 'Core', 'Contralateral', FALSE, TRUE, FALSE),
       ('Bounds', 'Compound', 'Lower Body', 'Legs', 'Bilateral', FALSE, TRUE, TRUE),
       ('Box Jumps', 'Compound', 'Lower Body', 'Legs', 'Bilateral', FALSE, TRUE, TRUE),
       ('Box Toe Touch', 'Isolation', 'Midsection', 'Core', 'Bilateral', FALSE, TRUE, FALSE),
       ('Broad Jump', 'Compound', 'Lower Body', 'Legs', 'Bilateral', FALSE, TRUE, TRUE),
       ('Bulgarian Split Squat', 'Compound', 'Lower Body', 'Legs', 'Unilateral', TRUE, FALSE, FALSE),
       ('Burpee', 'Compound', 'Full Body', 'Full Body', 'Bilateral', FALSE, TRUE, TRUE),
       ('Burpee Broad Jump', 'Compound', 'Full Body', 'Full Body', 'Bilateral', FALSE, TRUE, TRUE),
       ('Butt Kickers', 'Isolation', 'Lower Body', 'Legs', 'Bilateral', FALSE, TRUE, FALSE),
       ('Calf Raise', 'Isolation', 'Lower Body', 'Legs', 'Bilateral', TRUE, FALSE, FALSE),
       ('Chest Press', 'Compound', 'Upper Body', 'Chest', 'Bilateral', TRUE, FALSE, FALSE),
       ('Close to Wide Grip Burnout', 'Isolation', 'Upper Body', 'Arms', 'Bilateral', TRUE, FALSE, FALSE),
       ('Compass Jump', 'Compound', 'Lower Body', 'Legs', 'Bilateral', FALSE, TRUE, TRUE),
       ('Crab Crawl', 'Compound', 'Full Body', 'Full Body', 'Bilateral', FALSE, TRUE, FALSE),
       ('Curtsey Lunges', 'Compound', 'Lower Body', 'Legs', 'Unilateral', TRUE, FALSE, FALSE),
       ('Deficit Squat', 'Compound', 'Lower Body', 'Legs', 'Bilateral', TRUE, FALSE, FALSE),
       ('Donkey Kick', 'Isolation', 'Lower Body', 'Legs', 'Unilateral', TRUE, FALSE, FALSE),
       ('Fire Hydrant', 'Isolation', 'Lower Body', 'Legs', 'Unilateral', TRUE, TRUE, FALSE),
       ('Flutter Kick', 'Isolation', 'Midsection', 'Core', 'Bilateral', FALSE, TRUE, FALSE),
       ('Frogger', 'Compound', 'Full Body', 'Full Body', 'Bilateral', FALSE, TRUE, FALSE),
       ('Glute Bridge', 'Isolation', 'Lower Body', 'Legs', 'Bilateral', TRUE, FALSE, FALSE),
       ('Glute Bridge March', 'Isolation', 'Lower Body', 'Legs', 'Unilateral', TRUE, FALSE, FALSE),
       ('Goblet Squat', 'Compound', 'Lower Body', 'Legs', 'Bilateral', TRUE, FALSE, FALSE),
       ('Halo', 'Isolation', 'Midsection', 'Core', 'Bilateral', TRUE, FALSE, FALSE),
       ('Heart Pump', 'Compound', 'Full Body', 'Full Body', 'Bilateral', FALSE, TRUE, FALSE),
       ('High Knees', 'Compound', 'Lower Body', 'Legs', 'Bilateral', FALSE, TRUE, FALSE),
       ('Jump Lunges', 'Compound', 'Lower Body', 'Legs', 'Bilateral', FALSE, TRUE, TRUE),
       ('Jump Rope', 'Compound', 'Full Body', 'Full Body', 'Bilateral', FALSE, TRUE, FALSE),
       ('Jumping Jack Push Press', 'Compound', 'Full Body', 'Full Body', 'Bilateral', FALSE, TRUE, TRUE),
       ('Jumping Jacks', 'Compound', 'Full Body', 'Full Body', 'Bilateral', FALSE, TRUE, FALSE),
       ('Kettlebell Swing', 'Compound', 'Full Body', 'Full Body', 'Bilateral', TRUE, TRUE, FALSE),
       ('Knee Drive', 'Compound', 'Lower Body', 'Legs', 'Bilateral', FALSE, TRUE, FALSE),
       ('Lateral Band Walk', 'Isolation', 'Lower Body', 'Legs', 'Bilateral', TRUE, FALSE, FALSE),
       ('Leg Pull Apart', 'Isolation', 'Lower Body', 'Legs', 'Bilateral', TRUE, FALSE, FALSE),
       ('Leg Raise', 'Isolation', 'Midsection', 'Core', 'Bilateral', FALSE, TRUE, FALSE),
       ('Literally Just Jumping', 'Compound', 'Lower Body', 'Legs', 'Bilateral', FALSE, TRUE, TRUE),
       ('Lying Leg Raises', 'Isolation', 'Midsection', 'Core', 'Bilateral', TRUE, TRUE, FALSE),
       ('Military Plank', 'Isolation', 'Midsection', 'Core', 'Bilateral', FALSE, TRUE, FALSE),
       ('Monkey Jump', 'Compound', 'Lower Body', 'Legs', 'Bilateral', FALSE, TRUE, TRUE),
       ('Mountain Climbers', 'Compound', 'Full Body', 'Full Body', 'Bilateral', FALSE, TRUE, FALSE),
       ('Plank', 'Isolation', 'Midsection', 'Core', 'Bilateral', FALSE, TRUE, FALSE),
       ('Plank Jack', 'Compound', 'Full Body', 'Core', 'Bilateral', FALSE, TRUE, FALSE),
       ('Plank Row', 'Compound', 'Upper Body', 'Back', 'Bilateral', TRUE, FALSE, FALSE),
       ('Pushup', 'Compound', 'Upper Body', 'Chest', 'Bilateral', TRUE, TRUE, FALSE),
       ('Pushup Walk', 'Compound', 'Full Body', 'Chest', 'Bilateral', TRUE, TRUE, FALSE),
       ('Reverse Crunches', 'Isolation', 'Midsection', 'Core', 'Bilateral', FALSE, TRUE, FALSE),
       ('Row', 'Compound', 'Upper Body', 'Back', 'Bilateral', TRUE, FALSE, FALSE),
       ('Russian Twist', 'Isolation', 'Midsection', 'Core', 'Bilateral', TRUE, FALSE, FALSE),
       ('Seal Jacks', 'Compound', 'Full Body', 'Full Body', 'Bilateral', FALSE, TRUE, TRUE),
       ('Shoulder Press', 'Compound', 'Upper Body', 'Shoulders', 'Bilateral', TRUE, FALSE, FALSE),
       ('Side Arm / Lateral Raise', 'Isolation', 'Upper Body', 'Shoulders', 'Bilateral', TRUE, FALSE, FALSE),
       ('Side Lunge', 'Compound', 'Lower Body', 'Legs', 'Unilateral', TRUE, FALSE, FALSE),
       ('Side Plank', 'Isolation', 'Midsection', 'Core', 'Unilateral', FALSE, TRUE, FALSE),
       ('Side Plank Dips', 'Isolation', 'Midsection', 'Core', 'Unilateral', FALSE, TRUE, FALSE),
       ('Side Plank with Leg Lift', 'Isolation', 'Midsection', 'Core', 'Unilateral', FALSE, TRUE, FALSE),
       ('Single Arm Clean and Press', 'Compound', 'Full Body', 'Full Body', 'Unilateral', TRUE, FALSE, FALSE),
       ('Single Leg Hip Bridge', 'Isolation', 'Lower Body', 'Legs', 'Unilateral', TRUE, FALSE, FALSE),
       ('Single Leg Squat', 'Compound', 'Lower Body', 'Legs', 'Unilateral', TRUE, FALSE, FALSE),
       ('Situp and Throw', 'Isolation', 'Midsection', 'Core', 'Bilateral', TRUE, FALSE, TRUE),
       ('Skaters', 'Compound', 'Lower Body', 'Legs', 'Bilateral', FALSE, TRUE, FALSE),
       ('Skipping', 'Compound', 'Full Body', 'Full Body', 'Bilateral', FALSE, TRUE, FALSE),
       ('Skull Crusher', 'Isolation', 'Upper Body', 'Arms', 'Bilateral', TRUE, FALSE, FALSE),
       ('Spiderman Pushup', 'Compound', 'Midsection', 'Core', 'Bilateral', TRUE, TRUE, FALSE),
       ('Squat', 'Compound', 'Lower Body', 'Legs', 'Bilateral', TRUE, FALSE, FALSE),
       ('Squat Jump', 'Compound', 'Lower Body', 'Legs', 'Bilateral', FALSE, TRUE, TRUE),
       ('Squat Jumps 180', 'Compound', 'Lower Body', 'Legs', 'Bilateral', FALSE, TRUE, TRUE),
       ('Squat to Lateral Leg Lift', 'Compound', 'Lower Body', 'Legs', 'Unilateral', TRUE, FALSE, FALSE),
       ('Standing Glute Kickbacks', 'Isolation', 'Lower Body', 'Legs', 'Unilateral', TRUE, FALSE, FALSE),
       ('Standing Leg Lift', 'Isolation', 'Lower Body', 'Legs', 'Unilateral', TRUE, FALSE, FALSE),
       ('Standing Oblique Crunch', 'Isolation', 'Midsection', 'Core', 'Bilateral', FALSE, TRUE, FALSE),
       ('Star Jump', 'Compound', 'Full Body', 'Full Body', 'Bilateral', FALSE, TRUE, TRUE),
       ('Step Up Lunges', 'Compound', 'Lower Body', 'Legs', 'Unilateral', TRUE, FALSE, FALSE),
       ('Step-Back Lunge', 'Compound', 'Lower Body', 'Legs', 'Unilateral', TRUE, FALSE, FALSE),
       ('Straightup Situp', 'Isolation', 'Midsection', 'Core', 'Bilateral', FALSE, TRUE, FALSE),
       ('Sumo Squat', 'Compound', 'Lower Body', 'Legs', 'Bilateral', TRUE, FALSE, FALSE),
       ('Superman', 'Isolation', 'Midsection', 'Core', 'Bilateral', TRUE, FALSE, TRUE),
       ('Touchdown', 'Compound', 'Lower Body', 'Legs', 'Bilateral', FALSE, TRUE, TRUE),
       ('Tricep Dip', 'Isolation', 'Upper Body', 'Arms', 'Bilateral', TRUE, FALSE, FALSE),
       ('Tricep Kick-Back', 'Isolation', 'Upper Body', 'Arms', 'Bilateral', TRUE, FALSE, FALSE),
       ('Tricep Overhead Press', 'Isolation', 'Upper Body', 'Arms', 'Bilateral', TRUE, FALSE, FALSE),
       ('Tuck Jump', 'Compound', 'Lower Body', 'Legs', 'Bilateral', FALSE, TRUE, TRUE),
       ('Twisted Mountain Climbers', 'Compound', 'Full Body', 'Full Body', 'Bilateral', FALSE, TRUE, FALSE),
       ('Wall Ball', 'Compound', 'Full Body', 'Full Body', 'Bilateral', TRUE, TRUE, FALSE),
       ('Weighted Jumping Jacks', 'Compound', 'Full Body', 'Full Body', 'Bilateral', TRUE, TRUE, TRUE),
       ('Weighted Punches', 'Compound', 'Full Body', 'Full Body', 'Bilateral', TRUE, TRUE, FALSE),
       ('Deadbug', 'Isolation', 'Midsection', 'Core', 'Bilateral', FALSE, TRUE, FALSE);



CREATE TABLE s_workout.t_exercises_muscles_group
(
    id                SERIAL PRIMARY KEY,
    exercise_id       INT         NOT NULL,
    exercise_name     VARCHAR(35) NOT NULL,
    muscle_group_id   INT         NOT NULL,
    muscle_group_name VARCHAR(25) NOT NULL,
    status            VARCHAR(12) NOT NULL DEFAULT 'ACTIVE',
    created_date      TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date      TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by        VARCHAR(20) NOT NULL DEFAULT 'SYSTEM',
    updated_by        VARCHAR(20) NOT NULL DEFAULT 'SYSTEM'
);

-- Arnold Press
INSERT INTO s_workout.t_exercises_muscles_group (exercise_id, exercise_name, muscle_group_id, muscle_group_name)
VALUES (1, 'Arnold Press', 13, 'Lateral Delts'),
       (1, 'Arnold Press', 14, 'Anterior Delts'),
       (1, 'Arnold Press', 7, 'Upper Chest'),

-- Bicep Curl
       (2, 'Bicep Curl', 1, 'Bicep'),
       (2, 'Bicep Curl', 20, 'Forearms'),

-- Bicycle Crunch
       (3, 'Bicycle Crunch', 12, 'Oblique'),
       (3, 'Bicycle Crunch', 21, 'Hip Flexors'),

-- Bounds
       (4, 'Bounds', 2, 'Outer Thigh'),
       (4, 'Bounds', 3, 'Glutes'),
       (4, 'Bounds', 4, 'Hamstrings'),

-- Box Jumps
       (5, 'Box Jumps', 5, 'Quads'),
       (5, 'Box Jumps', 6, 'Calves'),
       (5, 'Box Jumps', 3, 'Glutes'),
       (5, 'Box Jumps', 4, 'Hamstrings'),

-- Box Toe Touch
       (6, 'Box Toe Touch', 3, 'Glutes'),
       (6, 'Box Toe Touch', 4, 'Hamstrings'),

-- Broad Jump
       (7, 'Broad Jump', 5, 'Quads'),
       (7, 'Broad Jump', 6, 'Calves'),
       (7, 'Broad Jump', 3, 'Glutes'),
       (7, 'Broad Jump', 4, 'Hamstrings'),

-- Bulgarian Split Squat
       (8, 'Bulgarian Split Squat', 5, 'Quads'),
       (8, 'Bulgarian Split Squat', 3, 'Glutes'),
       (8, 'Bulgarian Split Squat', 4, 'Hamstrings'),
       (8, 'Bulgarian Split Squat', 8, 'Inner Thigh'),

-- Burpee
       (9, 'Burpee', 5, 'Quads'),
       (9, 'Burpee', 6, 'Calves'),
       (9, 'Burpee', 3, 'Glutes'),
       (9, 'Burpee', 4, 'Hamstrings'),
       (9, 'Burpee', 12, 'Oblique'),
       (9, 'Burpee', 10, 'Triceps'),
       (9, 'Burpee', 20, 'Forearms'),
       (9, 'Burpee', 21, 'Hip Flexors'),

-- Burpee Broad Jump
       (10, 'Burpee Broad Jump', 5, 'Quads'),
       (10, 'Burpee Broad Jump', 6, 'Calves'),
       (10, 'Burpee Broad Jump', 3, 'Glutes'),
       (10, 'Burpee Broad Jump', 4, 'Hamstrings'),
       (10, 'Burpee Broad Jump', 12, 'Oblique'),
       (10, 'Burpee Broad Jump', 10, 'Triceps'),
       (10, 'Burpee Broad Jump', 20, 'Forearms'),
       (10, 'Burpee Broad Jump', 21, 'Hip Flexors'),

-- Butt Kickers
       (11, 'Butt Kickers', 4, 'Hamstrings'),
       (11, 'Butt Kickers', 6, 'Calves'),

-- Calf Raise
       (12, 'Calf Raise', 6, 'Calves'),

-- Chest Press
       (13, 'Chest Press', 9, 'Medium Chest'),
       (13, 'Chest Press', 10, 'Triceps'),
       (13, 'Chest Press', 7, 'Upper Chest'),

-- Close to Wide Grip Burnout
       (14, 'Close to Wide Grip Burnout', 10, 'Triceps'),
       (14, 'Close to Wide Grip Burnout', 7, 'Upper Chest'),

-- Compass Jump
       (15, 'Compass Jump', 2, 'Outer Thigh'),
       (15, 'Compass Jump', 3, 'Glutes'),
       (15, 'Compass Jump', 21, 'Hip Flexors'),

-- Crab Crawl
       (16, 'Crab Crawl', 3, 'Glutes'),
       (16, 'Crab Crawl', 10, 'Triceps'),
       (16, 'Crab Crawl', 12, 'Oblique'),

-- Curtsey Lunges
       (17, 'Curtsey Lunges', 2, 'Outer Thigh'),
       (17, 'Curtsey Lunges', 3, 'Glutes'),
       (17, 'Curtsey Lunges', 5, 'Quads'),

-- Deficit Squat
       (18, 'Deficit Squat', 5, 'Quads'),
       (18, 'Deficit Squat', 3, 'Glutes'),
       (18, 'Deficit Squat', 4, 'Hamstrings'),

-- Donkey Kick
       (19, 'Donkey Kick', 3, 'Glutes'),

-- Fire Hydrant
       (20, 'Fire Hydrant', 3, 'Glutes'),

-- Flutter Kick
       (21, 'Flutter Kick', 21, 'Hip Flexors'),
       (21, 'Flutter Kick', 12, 'Oblique'),

-- Frogger
       (22, 'Frogger', 5, 'Quads'),
       (22, 'Frogger', 6, 'Calves'),
       (22, 'Frogger', 3, 'Glutes'),
       (22, 'Frogger', 4, 'Hamstrings'),

-- Glute Bridge
       (23, 'Glute Bridge', 3, 'Glutes'),

-- Glute Bridge March
       (24, 'Glute Bridge March', 3, 'Glutes'),

-- Goblet Squat
       (25, 'Goblet Squat', 5, 'Quads'),
       (25, 'Goblet Squat', 3, 'Glutes'),
       (25, 'Goblet Squat', 4, 'Hamstrings'),

-- Halo
       (26, 'Halo', 13, 'Lateral Delts'),
       (26, 'Halo', 14, 'Anterior Delts'),
       (26, 'Halo', 7, 'Upper Chest'),

-- Heart Pump
       (27, 'Heart Pump', 5, 'Quads'),
       (27, 'Heart Pump', 6, 'Calves'),
       (27, 'Heart Pump', 3, 'Glutes'),
       (27, 'Heart Pump', 21, 'Hip Flexors'),

-- High Knees
       (28, 'High Knees', 21, 'Hip Flexors'),
       (28, 'High Knees', 6, 'Calves'),

-- Jump Lunges
       (29, 'Jump Lunges', 5, 'Quads'),
       (29, 'Jump Lunges', 3, 'Glutes'),
       (29, 'Jump Lunges', 4, 'Hamstrings'),

-- Jump Rope
       (30, 'Jump Rope', 6, 'Calves'),
       (30, 'Jump Rope', 21, 'Hip Flexors'),

-- Jumping Jack Push Press
       (31, 'Jumping Jack Push Press', 13, 'Shoulders'),
       (31, 'Jumping Jack Push Press', 14, 'Triceps'),
       (31, 'Jumping Jack Push Press', 7, 'Upper Chest'),
       (31, 'Jumping Jack Push Press', 19, 'Core'),

-- Jumping Jacks
       (32, 'Jumping Jacks', 6, 'Calves'),
       (32, 'Jumping Jacks', 21, 'Hip Flexors'),

-- Kettlebell Swing
       (33, 'Kettlebell Swing', 3, 'Glutes'),
       (33, 'Kettlebell Swing', 4, 'Hamstrings'),
       (33, 'Kettlebell Swing', 5, 'Quads'),

-- Knee Drive
       (34, 'Knee Drive', 21, 'Hip Flexors'),
       (34, 'Knee Drive', 5, 'Quads'),

-- Lateral Band Walk
       (35, 'Lateral Band Walk', 3, 'Glutes'),
       (35, 'Lateral Band Walk', 2, 'Outer Thigh'),

-- Leg Pull Apart
       (36, 'Leg Pull Apart', 2, 'Outer Thigh'),
       (36, 'Leg Pull Apart', 3, 'Glutes'),

-- Leg Raise
       (37, 'Leg Raise', 19, 'Core'),
       (37, 'Leg Raise', 21, 'Hip Flexors'),

-- Literally Just Jumping
       (38, 'Literally Just Jumping', 6, 'Calves'),
       (38, 'Literally Just Jumping', 21, 'Hip Flexors'),

-- Lying Leg Raises
       (39, 'Lying Leg Raises', 21, 'Hip Flexors'),
       (39, 'Lying Leg Raises', 19, 'Core'),

-- Military Plank
       (40, 'Military Plank', 19, 'Core'),
       (40, 'Military Plank', 16, 'Traps'),

-- Monkey Jump
       (41, 'Monkey Jump', 3, 'Glutes'),
       (41, 'Monkey Jump', 5, 'Quads'),
       (41, 'Monkey Jump', 4, 'Hamstrings'),

-- Mountain Climbers
       (42, 'Mountain Climbers', 19, 'Core'),
       (42, 'Mountain Climbers', 21, 'Hip Flexors'),

-- Plank
       (43, 'Plank', 19, 'Core'),
       (43, 'Plank', 20, 'Forearms'),

-- Plank Jack
       (44, 'Plank Jack', 19, 'Core'),
       (44, 'Plank Jack', 20, 'Forearms'),

-- Plank Row
       (45, 'Plank Row', 19, 'Core'),
       (45, 'Plank Row', 20, 'Forearms'),

-- Pushup
       (46, 'Pushup', 14, 'Triceps'),
       (46, 'Pushup', 19, 'Core'),
       (46, 'Pushup', 7, 'Upper Chest'),

--- Pushup Walk
       (47, 'Pushup Walk', 10, 'Triceps'),
       (47, 'Pushup Walk', 19, 'Core'),
       (47, 'Pushup Walk', 7, 'Upper Chest'),

-- Reverse Crunches
       (48, 'Reverse Crunches', 19, 'Core'),
       (48, 'Reverse Crunches', 21, 'Hip Flexors'),

-- Row
       (49, 'Row', 11, 'Lats'),
       (49, 'Row', 19, 'Core'),

-- Russian Twist
       (50, 'Russian Twist', 19, 'Core'),
       (50, 'Russian Twist', 12, 'Oblique'),

-- Seal Jacks
       (51, 'Seal Jacks', 6, 'Calves'),
       (51, 'Seal Jacks', 21, 'Hip Flexors'),

-- Shoulder Press
       (52, 'Shoulder Press', 10, 'Triceps'),
       (52, 'Shoulder Press', 7, 'Upper Chest'),
       (52, 'Shoulder Press', 13, 'Lateral Delts'),

-- Side Arm / Lateral Raise
       (53, 'Side Arm / Lateral Raise', 13, 'Lateral Delts'),
       (53, 'Side Arm / Lateral Raise', 15, 'Rear Delts'),

-- Side Lunge
       (54, 'Side Lunge', 2, 'Outer Thigh'),
       (54, 'Side Lunge', 3, 'Glutes'),
       (54, 'Side Lunge', 5, 'Quads'),

-- Side Plank
       (55, 'Side Plank', 19, 'Core'),
       (55, 'Side Plank', 12, 'Oblique'),

-- Side Plank Dips
       (56, 'Side Plank Dips', 19, 'Core'),
       (56, 'Side Plank Dips', 12, 'Oblique'),

-- Side Plank with Leg Lift
       (57, 'Side Plank with Leg Lift', 19, 'Core'),
       (57, 'Side Plank with Leg Lift', 12, 'Oblique'),

-- Single Arm Clean and Press
       (58, 'Single Arm Clean and Press', 10, 'Triceps'),
       (58, 'Single Arm Clean and Press', 7, 'Upper Chest'),
       (58, 'Single Arm Clean and Press', 13, 'Lateral Delts'),

-- Single Leg Hip Bridge
       (59, 'Single Leg Hip Bridge', 3, 'Glutes'),

-- Single Leg Squat
       (60, 'Single Leg Squat', 5, 'Quads'),
       (60, 'Single Leg Squat', 3, 'Glutes'),
       (60, 'Single Leg Squat', 4, 'Hamstrings'),

-- Situp and Throw
       (61, 'Situp and Throw', 19, 'Core'),
       (61, 'Situp and Throw', 21, 'Hip Flexors'),

-- Skaters
       (62, 'Skaters', 2, 'Outer Thigh'),
       (62, 'Skaters', 3, 'Glutes'),
       (62, 'Skaters', 5, 'Quads'),

-- Skipping
       (63, 'Skipping', 6, 'Calves'),
       (63, 'Skipping', 21, 'Hip Flexors'),

-- Skull Crusher
       (64, 'Skull Crusher', 10, 'Triceps'),
       (64, 'Skull Crusher', 19, 'Core'),

-- Spiderman Pushup
       (65, 'Spiderman Pushup', 10, 'Triceps'),
       (65, 'Spiderman Pushup', 19, 'Core'),
       (65, 'Spiderman Pushup', 7, 'Upper Chest'),

-- Squat
       (66, 'Squat', 5, 'Quads'),
       (66, 'Squat', 3, 'Glutes'),
       (66, 'Squat', 4, 'Hamstrings'),

-- Squat Jump
       (67, 'Squat Jump', 5, 'Quads'),
       (67, 'Squat Jump', 3, 'Glutes'),
       (67, 'Squat Jump', 4, 'Hamstrings'),
       (67, 'Squat Jump', 6, 'Calves'),

-- Squat Jumps 180
       (68, 'Squat Jumps 180', 5, 'Quads'),
       (68, 'Squat Jumps 180', 3, 'Glutes'),
       (68, 'Squat Jumps 180', 4, 'Hamstrings'),
       (68, 'Squat Jumps 180', 6, 'Calves'),

-- Squat to Lateral Leg Lift
       (69, 'Squat to Lateral Leg Lift', 5, 'Quads'),
       (69, 'Squat to Lateral Leg Lift', 3, 'Glutes'),
       (69, 'Squat to Lateral Leg Lift', 4, 'Hamstrings'),
       (69, 'Squat to Lateral Leg Lift', 2, 'Outer Thigh'),

-- Standing Glute Kickbacks
       (70, 'Standing Glute Kickbacks', 3, 'Glutes'),

-- Standing Leg Lift
       (71, 'Standing Leg Lift', 2, 'Outer Thigh'),
       (71, 'Standing Leg Lift', 3, 'Glutes'),

-- Standing Oblique Crunch
       (72, 'Standing Oblique Crunch', 12, 'Oblique'),
       (72, 'Standing Oblique Crunch', 1, 'Core'),

-- Star Jump
       (73, 'Star Jump', 6, 'Calves'),
       (73, 'Star Jump', 21, 'Hip Flexors'),

-- Step Up Lunges
       (74, 'Step Up Lunges', 5, 'Quads'),
       (74, 'Step Up Lunges', 3, 'Glutes'),
       (74, 'Step Up Lunges', 4, 'Hamstrings'),

-- Step-Back Lunge
       (75, 'Step-Back Lunge', 5, 'Quads'),
       (75, 'Step-Back Lunge', 3, 'Glutes'),
       (75, 'Step-Back Lunge', 4, 'Hamstrings'),

-- Straightup Situp
       (76, 'Straightup Situp', 1, 'Core'),
       (76, 'Straightup Situp', 21, 'Hip Flexors'),

-- Sumo Squat
       (77, 'Sumo Squat', 2, 'Outer Thigh'),
       (77, 'Sumo Squat', 3, 'Glutes'),
       (77, 'Sumo Squat', 5, 'Quads'),

-- Superman
       (78, 'Superman', 18, 'Lower Back'),
       (78, 'Superman', 3, 'Glutes'),

-- Touchdown
       (79, 'Touchdown', 2, 'Outer Thigh'),
       (79, 'Touchdown', 3, 'Glutes'),
       (79, 'Touchdown', 5, 'Quads'),
       (79, 'Touchdown', 6, 'Calves'),
       (79, 'Touchdown', 21, 'Hip Flexors'),

-- Tricep Dip
       (80, 'Tricep Dip', 10, 'Triceps'),
       (80, 'Tricep Dip', 1, 'Core'),

-- Tricep Kick-Back
       (81, 'Tricep Kick-Back', 10, 'Triceps'),
       (81, 'Tricep Kick-Back', 1, 'Core'),

-- Tricep Overhead Press
       (82, 'Tricep Overhead Press', 10, 'Triceps'),
       (82, 'Tricep Overhead Press', 7, 'Upper Chest'),
       (82, 'Tricep Overhead Press', 13, 'Lateral Delts'),
       (82, 'Tricep Overhead Press', 15, 'Rear Delts'),

-- Tuck Jump
       (83, 'Tuck Jump', 5, 'Quads'),
       (83, 'Tuck Jump', 3, 'Glutes'),
       (83, 'Tuck Jump', 4, 'Hamstrings'),
       (83, 'Tuck Jump', 6, 'Calves'),

-- Twisted Mountain Climbers
       (84, 'Twisted Mountain Climbers', 1, 'Core'),
       (84, 'Twisted Mountain Climbers', 12, 'Oblique'),

-- Wall Ball
       (85, 'Wall Ball', 5, 'Quads'),
       (85, 'Wall Ball', 3, 'Glutes'),
       (85, 'Wall Ball', 4, 'Hamstrings'),
       (85, 'Wall Ball', 6, 'Calves'),
       (85, 'Wall Ball', 7, 'Upper Chest'),
       (85, 'Wall Ball', 10, 'Triceps'),
       (85, 'Wall Ball', 13, 'Shoulders'),
       (85, 'Wall Ball', 1, 'Core'),

-- Weighted Jumping Jacks
       (86, 'Weighted Jumping Jacks', 5, 'Quads'),
       (86, 'Weighted Jumping Jacks', 3, 'Glutes'),
       (86, 'Weighted Jumping Jacks', 4, 'Hamstrings'),
       (86, 'Weighted Jumping Jacks', 6, 'Calves'),
       (86, 'Weighted Jumping Jacks', 13, 'Shoulders'),
       (86, 'Weighted Jumping Jacks', 1, 'Core'),

-- Weighted Punches
       (87, 'Weighted Punches', 10, 'Triceps'),
       (87, 'Weighted Punches', 1, 'Core'),
       (87, 'Weighted Punches', 13, 'Shoulders'),
       (87, 'Weighted Punches', 7, 'Upper Chest'),

-- Deadbug
       (88, 'Deadbug', 1, 'Core'),
       (88, 'Deadbug', 21, 'Hip Flexors'),
       (88, 'Deadbug', 12, 'Oblique');

CREATE TABLE s_workout.t_exercises_equipments
(
    id             SERIAL PRIMARY KEY,
    exercise_id    INT         NOT NULL,
    exercise_name  VARCHAR(35) NOT NULL,
    equipment_id   INT         NOT NULL,
    equipment_name VARCHAR(30) NOT NULL,
    status         VARCHAR(12) NOT NULL DEFAULT 'ACTIVE',
    created_date   TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date   TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by     VARCHAR(20) NOT NULL DEFAULT 'SYSTEM',
    updated_by     VARCHAR(20) NOT NULL DEFAULT 'SYSTEM'
);

-- Example for Arnold Press
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Arnold Press'),
        'Arnold Press',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Dumbbells'),
        'Dumbbells'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Arnold Press'),
        'Arnold Press',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Bench'),
        'Bench');

-- Example for Bicep Curl
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Bicep Curl'),
        'Bicep Curl',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Dumbbells'),
        'Dumbbells'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Bicep Curl'),
        'Bicep Curl',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Barbell'),
        'Barbell'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Bicep Curl'),
        'Bicep Curl',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Cable'),
        'Cable'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Bicep Curl'),
        'Bicep Curl',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'),
        'Body Weight');

-- Bicycle Crunch
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Bicycle Crunch'),
        'Bicycle Crunch',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'),
        'Body Weight');

-- Bounds
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Bounds'),
        'Bounds',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'),
        'Body Weight');

-- Box Jumps
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Box Jumps'),
        'Box Jumps',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Platform'),
        'Platform');

-- Box Toe Touch
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Box Toe Touch'),
        'Box Toe Touch',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Platform'),
        'Platform'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Box Toe Touch'),
        'Box Toe Touch',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'),
        'Body Weight');

-- Broad Jump
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Broad Jump'),
        'Broad Jump',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'),
        'Body Weight');

-- Bulgarian Split Squat
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Bulgarian Split Squat'),
        'Bulgarian Split Squat',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Dumbbells'),
        'Dumbbells'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Bulgarian Split Squat'),
        'Bulgarian Split Squat',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Barbell'),
        'Barbell'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Bulgarian Split Squat'),
        'Bulgarian Split Squat',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Bench'),
        'Bench');

-- Burpee
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Burpee'),
        'Burpee',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'),
        'Body Weight');

-- Burpee Broad Jump
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Burpee Broad Jump'),
        'Burpee Broad Jump',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'),
        'Body Weight');

-- Butt Kickers
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Butt Kickers'),
        'Butt Kickers',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'),
        'Body Weight');

-- Calf Raise
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Calf Raise'),
        'Calf Raise',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Dumbbells'),
        'Dumbbells'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Calf Raise'),
        'Calf Raise',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Barbell'),
        'Barbell'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Calf Raise'),
        'Calf Raise',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Machine'),
        'Machine');

-- Chest Press
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Chest Press'),
        'Chest Press',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Dumbbells'),
        'Dumbbells'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Chest Press'),
        'Chest Press',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Barbell'),
        'Barbell'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Chest Press'),
        'Chest Press',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Bench'),
        'Bench'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Chest Press'),
        'Chest Press',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Machine'),
        'Machine');

-- Close to Wide Grip Burnout
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Close to Wide Grip Burnout'),
        'Close to Wide Grip Burnout',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Dumbbells'),
        'Dumbbells'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Close to Wide Grip Burnout'),
        'Close to Wide Grip Burnout',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Barbell'),
        'Barbell'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Close to Wide Grip Burnout'),
        'Close to Wide Grip Burnout',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Bench'),
        'Bench');

-- Compass Jump
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Compass Jump'),
        'Compass Jump',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'),
        'Body Weight');

-- Crab Crawl
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Crab Crawl'),
        'Crab Crawl',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'),
        'Body Weight');

-- Curtsey Lunges
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Curtsey Lunges'),
        'Curtsey Lunges',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Dumbbells'),
        'Dumbbells'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Curtsey Lunges'),
        'Curtsey Lunges',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Barbell'),
        'Barbell'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Curtsey Lunges'),
        'Curtsey Lunges',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'),
        'Body Weight');

-- Deficit Squat
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Deficit Squat'),
        'Deficit Squat',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Dumbbells'),
        'Dumbbells'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Deficit Squat'),
        'Deficit Squat',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Barbell'),
        'Barbell'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Deficit Squat'),
        'Deficit Squat',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Platform'),
        'Platform');

-- Donkey Kick
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Donkey Kick'),
        'Donkey Kick',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'),
        'Body Weight'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Donkey Kick'),
        'Donkey Kick',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Band'),
        'Band');

-- Fire Hydrant
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Fire Hydrant'),
        'Fire Hydrant',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'),
        'Body Weight'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Fire Hydrant'),
        'Fire Hydrant',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Band'),
        'Band');

-- Flutter Kick
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Flutter Kick'),
        'Flutter Kick',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'),
        'Body Weight');

-- Frogger
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Frogger'),
        'Frogger',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'),
        'Body Weight');

-- Glute Bridge
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Glute Bridge'),
        'Glute Bridge',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'),
        'Body Weight'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Glute Bridge'),
        'Glute Bridge',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Dumbbells'),
        'Dumbbells'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Glute Bridge'),
        'Glute Bridge',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Barbell'),
        'Barbell'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Glute Bridge'),
        'Glute Bridge',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Bench'),
        'Bench');

-- Glute Bridge March
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Glute Bridge March'),
        'Glute Bridge March',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'),
        'Body Weight'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Glute Bridge March'),
        'Glute Bridge March',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Dumbbells'),
        'Dumbbells');

-- Goblet Squat
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Goblet Squat'),
        'Goblet Squat',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Dumbbells'),
        'Dumbbells'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Goblet Squat'),
        'Goblet Squat',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Kettle Bells'),
        'Kettle Bells');

-- Halo
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Halo'),
        'Halo',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Dumbbells'),
        'Dumbbells'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Halo'),
        'Halo',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Kettle Bells'),
        'Kettle Bells');

-- Heart Pump
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Heart Pump'),
        'Heart Pump',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'),
        'Body Weight');

-- High Knees
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'High Knees'),
        'High Knees',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'),
        'Body Weight');

-- Jump Lunges
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Jump Lunges'),
        'Jump Lunges',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'),
        'Body Weight');

-- Jump Rope
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Jump Rope'),
        'Jump Rope',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'),
        'Body Weight'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Jump Rope'),
        'Jump Rope',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Rope'),
        'Rope');

-- Jumping Jack Push Press
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Jumping Jack Push Press'),
        'Jumping Jack Push Press',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Dumbbells'),
        'Dumbbells'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Jumping Jack Push Press'),
        'Jumping Jack Push Press',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'),
        'Body Weight');

-- Jumping Jacks
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Jumping Jacks'),
        'Jumping Jacks',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'),
        'Body Weight');

-- Kettlebell Swing
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Kettlebell Swing'),
        'Kettlebell Swing',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Kettle Bells'),
        'Kettle Bells');

-- Knee Drive
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Knee Drive'),
        'Knee Drive',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'),
        'Body Weight');

-- Lateral Band Walk
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Lateral Band Walk'),
        'Lateral Band Walk',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Band'),
        'Band');

-- Leg Pull Apart
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Leg Pull Apart'),
        'Leg Pull Apart',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Band'),
        'Band');

-- Leg Raise
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Leg Raise'),
        'Leg Raise',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'),
        'Body Weight');

-- Literally Just Jumping
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Literally Just Jumping'),
        'Literally Just Jumping',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'),
        'Body Weight');

-- Lying Leg Raises
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Lying Leg Raises'),
        'Lying Leg Raises',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'),
        'Body Weight');

-- Military Plank
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Military Plank'),
        'Military Plank',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'),
        'Body Weight');

-- Monkey Jump
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Monkey Jump'),
        'Monkey Jump',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'),
        'Body Weight');

-- Mountain Climbers
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Mountain Climbers'),
        'Mountain Climbers',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'),
        'Body Weight');

-- Plank
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Plank'), 'Plank',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'), 'Body Weight');

-- Plank Jack
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Plank Jack'), 'Plank Jack',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'), 'Body Weight');

-- Plank Row
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Plank Row'), 'Plank Row',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Dumbbells'), 'Dumbbells'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Plank Row'), 'Plank Row',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'), 'Body Weight');

-- Pushup
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Pushup'), 'Pushup',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'), 'Body Weight'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Pushup'), 'Pushup',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Bench'), 'Bench');

-- Pushup Walk
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Pushup Walk'), 'Pushup Walk',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'), 'Body Weight');

-- Reverse Crunches
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Reverse Crunches'), 'Reverse Crunches',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'), 'Body Weight');

-- Row
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Row'), 'Row',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Dumbbells'), 'Dumbbells'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Row'), 'Row',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Barbell'), 'Barbell'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Row'), 'Row',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Machine'), 'Machine');

-- Russian Twist
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Russian Twist'), 'Russian Twist',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Dumbbells'), 'Dumbbells'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Russian Twist'), 'Russian Twist',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Kettle Bells'), 'Kettle Bells'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Russian Twist'), 'Russian Twist',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'), 'Body Weight');

-- Seal Jacks
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Seal Jacks'), 'Seal Jacks',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'), 'Body Weight');

-- Shoulder Press
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Shoulder Press'), 'Shoulder Press',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Dumbbells'), 'Dumbbells'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Shoulder Press'), 'Shoulder Press',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Barbell'), 'Barbell'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Shoulder Press'), 'Shoulder Press',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Machine'), 'Machine');

-- Side Arm / Lateral Raise
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Side Arm / Lateral Raise'), 'Side Arm / Lateral Raise',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Dumbbells'), 'Dumbbells'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Side Arm / Lateral Raise'), 'Side Arm / Lateral Raise',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Cable'), 'Cable');

-- Side Lunge
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Side Lunge'), 'Side Lunge',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Dumbbells'), 'Dumbbells'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Side Lunge'), 'Side Lunge',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Barbell'), 'Barbell'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Side Lunge'), 'Side Lunge',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'), 'Body Weight');

-- Side Plank
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Side Plank'), 'Side Plank',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'), 'Body Weight');

-- Side Plank Dips
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Side Plank Dips'), 'Side Plank Dips',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'), 'Body Weight');

-- Side Plank with Leg Lift
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Side Plank with Leg Lift'), 'Side Plank with Leg Lift',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'), 'Body Weight');

-- Single Arm Clean and Press
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Single Arm Clean and Press'), 'Single Arm Clean and Press',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Dumbbells'), 'Dumbbells'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Single Arm Clean and Press'), 'Single Arm Clean and Press',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Kettle Bells'), 'Kettle Bells');

-- Single Leg Hip Bridge
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Single Leg Hip Bridge'), 'Single Leg Hip Bridge',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Dumbbells'), 'Dumbbells'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Single Leg Hip Bridge'), 'Single Leg Hip Bridge',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'), 'Body Weight');

-- Single Leg Squat
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Single Leg Squat'), 'Single Leg Squat',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Dumbbells'), 'Dumbbells'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Single Leg Squat'), 'Single Leg Squat',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Barbell'), 'Barbell'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Single Leg Squat'), 'Single Leg Squat',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'), 'Body Weight');

-- Situp and Throw
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Situp and Throw'), 'Situp and Throw',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Medicine Ball'), 'Medicine Ball'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Situp and Throw'), 'Situp and Throw',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'), 'Body Weight');

-- Skaters
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Skaters'), 'Skaters',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'), 'Body Weight');

-- Skipping
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Skipping'), 'Skipping',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Rope'), 'Rope');

-- Skull Crusher
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Skull Crusher'), 'Skull Crusher',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Dumbbells'), 'Dumbbells'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Skull Crusher'), 'Skull Crusher',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Barbell'), 'Barbell'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Skull Crusher'), 'Skull Crusher',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Cable'), 'Cable'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Skull Crusher'), 'Skull Crusher',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Bench'), 'Bench');

-- Spiderman Pushup
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Spiderman Pushup'), 'Spiderman Pushup',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'), 'Body Weight');

-- Squat
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Squat'), 'Squat',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Dumbbells'), 'Dumbbells'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Squat'), 'Squat',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Barbell'), 'Barbell'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Squat'), 'Squat',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'), 'Body Weight'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Squat'), 'Squat',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Squat Rack'), 'Squat Rack');

-- Squat Jump
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Squat Jump'), 'Squat Jump',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'), 'Body Weight');

-- Squat Jumps 180
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Squat Jumps 180'), 'Squat Jumps 180',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'), 'Body Weight');

-- Squat to Lateral Leg Lift
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Squat to Lateral Leg Lift'), 'Squat to Lateral Leg Lift',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Dumbbells'), 'Dumbbells'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Squat to Lateral Leg Lift'), 'Squat to Lateral Leg Lift',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'), 'Body Weight');

-- Standing Glute Kickbacks
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Standing Glute Kickbacks'), 'Standing Glute Kickbacks',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'), 'Body Weight'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Standing Glute Kickbacks'), 'Standing Glute Kickbacks',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Band'), 'Band');

-- Standing Leg Lift
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Standing Leg Lift'), 'Standing Leg Lift',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'), 'Body Weight'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Standing Leg Lift'), 'Standing Leg Lift',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Band'), 'Band');

-- Standing Oblique Crunch
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Standing Oblique Crunch'), 'Standing Oblique Crunch',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'), 'Body Weight');

-- Star Jump
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Star Jump'), 'Star Jump',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'), 'Body Weight');

-- Step Up Lunges
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Step Up Lunges'), 'Step Up Lunges',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Dumbbells'), 'Dumbbells'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Step Up Lunges'), 'Step Up Lunges',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Barbell'), 'Barbell'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Step Up Lunges'), 'Step Up Lunges',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Bench'), 'Bench');

-- Step-Back Lunge
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Step-Back Lunge'), 'Step-Back Lunge',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Dumbbells'), 'Dumbbells'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Step-Back Lunge'), 'Step-Back Lunge',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Barbell'), 'Barbell');

-- Straightup Situp
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Straightup Situp'), 'Straightup Situp',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'), 'Body Weight');

-- Sumo Squat
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Sumo Squat'), 'Sumo Squat',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Dumbbells'), 'Dumbbells'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Sumo Squat'), 'Sumo Squat',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Kettle Bells'), 'Kettle Bells'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Sumo Squat'), 'Sumo Squat',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Barbell'), 'Barbell');

-- Superman
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Superman'), 'Superman',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'), 'Body Weight');

-- Touchdown
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Touchdown'), 'Touchdown',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'), 'Body Weight');

-- Tricep Dip
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Tricep Dip'), 'Tricep Dip',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'), 'Body Weight'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Tricep Dip'), 'Tricep Dip',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Bench'), 'Bench'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Tricep Dip'), 'Tricep Dip',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Bar'), 'Bar');

-- Tricep Kick-Back
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Tricep Kick-Back'), 'Tricep Kick-Back',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Dumbbells'), 'Dumbbells'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Tricep Kick-Back'), 'Tricep Kick-Back',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Cable'), 'Cable');

-- Tricep Overhead Press
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Tricep Overhead Press'), 'Tricep Overhead Press',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Dumbbells'), 'Dumbbells'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Tricep Overhead Press'), 'Tricep Overhead Press',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Barbell'), 'Barbell'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Tricep Overhead Press'), 'Tricep Overhead Press',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Cable'), 'Cable');

-- Tuck Jump
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Tuck Jump'), 'Tuck Jump',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'), 'Body Weight');

-- Twisted Mountain Climbers
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Twisted Mountain Climbers'), 'Twisted Mountain Climbers',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'), 'Body Weight');

-- Wall Ball
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Wall Ball'), 'Wall Ball',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Medicine Ball'), 'Medicine Ball');

-- Weighted Jumping Jacks
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Weighted Jumping Jacks'), 'Weighted Jumping Jacks',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Dumbbells'), 'Dumbbells');

-- Weighted Punches
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Weighted Punches'), 'Weighted Punches',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Dumbbells'), 'Dumbbells'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Weighted Punches'), 'Weighted Punches',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'), 'Body Weight');

-- Deadbug
INSERT INTO s_workout.t_exercises_equipments (exercise_id, exercise_name, equipment_id, equipment_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Deadbug'), 'Deadbug',
        (SELECT id FROM s_workout.m_equipments WHERE name = 'Body Weight'), 'Body Weight');

CREATE TABLE s_workout.m_movement_patterns
(
    id           SERIAL PRIMARY KEY,
    name         VARCHAR(35) NOT NULL,
    description  TEXT,
    status       VARCHAR(12) NOT NULL DEFAULT 'ACTIVE',
    created_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by   VARCHAR(20) NOT NULL DEFAULT 'SYSTEM',
    updated_by   VARCHAR(20) NOT NULL DEFAULT 'SYSTEM'
);

INSERT INTO s_workout.m_movement_patterns(name)
VALUES ('Anti-Extension'),
       ('Hip Extension'),
       ('Anti-Rotational'),
       ('Rotational'),
       ('Spinal Flexion'),
       ('Horizontal Push'),
       ('Hip Flexion'),
       ('Lateral Flexion'),
       ('Anti-Lateral Flexion'),
       ('Isometric Hold'),
       ('Horizontal Pull'),
       ('Vertical Pull'),
       ('Hip External Rotation'),
       ('Hip Hinge'),
       ('Ankle Plantar Flexion'),
       ('Vertical Push'),
       ('Loaded Carry'),
       ('Shoulder Scapular Plane Elevation'),
       ('Elbow Flexion'),
       ('Elbow Extension'),
       ('Shoulder Flexion'),
       ('Wrist Extension'),
       ('Ankle Dorsiflexion'),
       ('Hip Abduction'),
       ('Hip Adduction'),
       ('Shoulder Abduction');

CREATE TABLE s_workout.t_exercises_movement_patterns
(
    id            SERIAL PRIMARY KEY,
    exercise_id   INT         NOT NULL,
    exercise_name VARCHAR(35) NOT NULL,
    pattern_id    INT         NOT NULL,
    pattern_name  VARCHAR(35) NOT NULL,
    status        VARCHAR(12) NOT NULL DEFAULT 'ACTIVE',
    created_date  TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date  TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by    VARCHAR(20) NOT NULL DEFAULT 'SYSTEM',
    updated_by    VARCHAR(20) NOT NULL DEFAULT 'SYSTEM'
);

-- Arnold Press
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id, exercise_name, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Arnold Press'),
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Shoulder Flexion'), 'Arnold Press',
        'Shoulder Flexion');

-- Bicep Curl
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id, exercise_name, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Bicep Curl'),
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Elbow Flexion'), 'Bicep Curl',
        'Elbow Flexion');

-- Bicycle Crunch
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id, exercise_name, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Bicycle Crunch'),
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Anti-Rotational'), 'Bicycle Crunch',
        'Anti-Rotational'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Bicycle Crunch'),
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Hinge'), 'Bicycle Crunch', 'Hip Hinge');

-- Bounds
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id, exercise_name, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Bounds'),
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Hinge'), 'Bounds', 'Hip Hinge');

-- Box Jumps
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id, exercise_name, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Box Jumps'),
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Hinge'), 'Box Jumps', 'Hip Hinge'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Box Jumps'),
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Ankle Dorsiflexion'), 'Box Jumps',
        'Ankle Dorsiflexion');

-- Box Toe Touch
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id, exercise_name, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Box Toe Touch'),
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Anti-Rotational'), 'Box Toe Touch',
        'Anti-Rotational');

-- Broad Jump
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id, exercise_name, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Broad Jump'),
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Hinge'), 'Broad Jump', 'Hip Hinge'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Broad Jump'),
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Abduction'), 'Broad Jump',
        'Hip Abduction');

-- Bulgarian Split Squat
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id, exercise_name, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Bulgarian Split Squat'),
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Hinge'), 'Bulgarian Split Squat',
        'Hip Hinge'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Bulgarian Split Squat'),
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Adduction'), 'Bulgarian Split Squat',
        'Hip Adduction');

-- Burpee
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id, exercise_name, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Burpee'),
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Hinge'), 'Burpee', 'Hip Hinge'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Burpee'),
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Shoulder Flexion'), 'Burpee',
        'Shoulder Flexion');

-- Burpee Broad Jump
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id, exercise_name, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Burpee Broad Jump'),
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Hinge'), 'Burpee Broad Jump',
        'Hip Hinge'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Burpee Broad Jump'),
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Shoulder Flexion'), 'Burpee Broad Jump',
        'Shoulder Flexion');

-- Butt Kickers
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id, exercise_name, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Butt Kickers'),
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Hinge'), 'Butt Kickers', 'Hip Hinge');

-- Calf Raise
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id, exercise_name, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Calf Raise'),
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Ankle Dorsiflexion'), 'Calf Raise',
        'Ankle Dorsiflexion');

-- Chest Press
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id, exercise_name, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Chest Press'),
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Shoulder Flexion'), 'Chest Press',
        'Shoulder Flexion'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Chest Press'),
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Horizontal Push'), 'Chest Press',
        'Horizontal Push');

-- Close to Wide Grip Burnout
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id, exercise_name, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Close to Wide Grip Burnout'),
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Elbow Flexion'),
        'Close to Wide Grip Burnout', 'Elbow Flexion'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Close to Wide Grip Burnout'),
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Shoulder Flexion'),
        'Close to Wide Grip Burnout', 'Shoulder Flexion');

-- Compass Jump
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id, exercise_name, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Compass Jump'),
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Hinge'), 'Compass Jump', 'Hip Hinge'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Compass Jump'),
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Abduction'), 'Compass Jump',
        'Hip Abduction');

-- Crab Crawl
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id, exercise_name, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Crab Crawl'),
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Anti-Rotational'), 'Crab Crawl',
        'Anti-Rotational'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Crab Crawl'),
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Shoulder Flexion'), 'Crab Crawl',
        'Shoulder Flexion');

-- Curtsey Lunges
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id, exercise_name, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Curtsey Lunges'),
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Adduction'), 'Curtsey Lunges',
        'Hip Adduction'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Curtsey Lunges'),
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Hinge'), 'Curtsey Lunges', 'Hip Hinge');

-- Deficit Squat
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id, exercise_name, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Deficit Squat'),
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Hinge'), 'Deficit Squat', 'Hip Hinge'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Deficit Squat'),
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Adduction'), 'Deficit Squat',
        'Hip Adduction');

-- Donkey Kick
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id, exercise_name, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Donkey Kick'),
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Hinge'), 'Donkey Kick', 'Hip Hinge');

-- Fire Hydrant
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id, exercise_name, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Fire Hydrant'),
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Abduction'), 'Fire Hydrant',
        'Hip Abduction');

-- Flutter Kick
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Flutter Kick'), 'Flutter Kick',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Anti-Rotational'), 'Anti-Rotational'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Flutter Kick'), 'Flutter Kick',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Hinge'), 'Hip Hinge');

-- Frogger
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Frogger'), 'Frogger',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Hinge'), 'Hip Hinge'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Frogger'), 'Frogger',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Shoulder Flexion'), 'Shoulder Flexion');

-- Glute Bridge
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Glute Bridge'), 'Glute Bridge',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Hinge'), 'Hip Hinge');

-- Glute Bridge March
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Glute Bridge March'), 'Glute Bridge March',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Hinge'), 'Hip Hinge');

-- Goblet Squat
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Goblet Squat'), 'Goblet Squat',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Hinge'), 'Hip Hinge'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Goblet Squat'), 'Goblet Squat',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Abduction'), 'Hip Abduction');

-- Halo
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Halo'), 'Halo',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Anti-Rotational'), 'Anti-Rotational');

-- Heart Pump
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Heart Pump'), 'Heart Pump',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Hinge'), 'Hip Hinge'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Heart Pump'), 'Heart Pump',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Shoulder Flexion'), 'Shoulder Flexion');

-- High Knees
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'High Knees'), 'High Knees',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Hinge'), 'Hip Hinge');

-- Jump Lunges
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Jump Lunges'), 'Jump Lunges',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Hinge'), 'Hip Hinge'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Jump Lunges'), 'Jump Lunges',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Adduction'), 'Hip Adduction');

-- Jump Rope
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Jump Rope'), 'Jump Rope',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Hinge'), 'Hip Hinge'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Jump Rope'), 'Jump Rope',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Shoulder Flexion'), 'Shoulder Flexion');

-- Jumping Jack Push Press
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Jumping Jack Push Press'),
        'Jumping Jack Push Press', (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Hinge'),
        'Hip Hinge'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Jumping Jack Push Press'),
        'Jumping Jack Push Press',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Shoulder Flexion'), 'Shoulder Flexion');

-- Jumping Jacks
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Jumping Jacks'), 'Jumping Jacks',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Hinge'), 'Hip Hinge'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Jumping Jacks'), 'Jumping Jacks',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Shoulder Flexion'), 'Shoulder Flexion');

-- Kettlebell Swing
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Kettlebell Swing'), 'Kettlebell Swing',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Hinge'), 'Hip Hinge'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Kettlebell Swing'), 'Kettlebell Swing',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Shoulder Flexion'), 'Shoulder Flexion');

-- Knee Drive
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Knee Drive'), 'Knee Drive',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Hinge'), 'Hip Hinge');

-- Lateral Band Walk
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Lateral Band Walk'), 'Lateral Band Walk',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Abduction'), 'Hip Abduction');

-- Leg Pull Apart
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Leg Pull Apart'), 'Leg Pull Apart',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Hinge'), 'Hip Hinge');

-- Leg Raise
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Leg Raise'), 'Leg Raise',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Anti-Rotational'), 'Anti-Rotational'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Leg Raise'), 'Leg Raise',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Hinge'), 'Hip Hinge');

-- Literally Just Jumping
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Literally Just Jumping'), 'Literally Just Jumping',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Hinge'), 'Hip Hinge');

-- Lying Leg Raises
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Lying Leg Raises'), 'Lying Leg Raises',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Anti-Rotational'), 'Anti-Rotational'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Lying Leg Raises'), 'Lying Leg Raises',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Hinge'), 'Hip Hinge');

-- Military Plank
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Military Plank'), 'Military Plank',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Anti-Rotational'), 'Anti-Rotational');

-- Monkey Jump
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Monkey Jump'), 'Monkey Jump',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Hinge'), 'Hip Hinge');

-- Mountain Climbers
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Mountain Climbers'), 'Mountain Climbers',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Hinge'), 'Hip Hinge'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Mountain Climbers'), 'Mountain Climbers',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Shoulder Flexion'), 'Shoulder Flexion');

-- Plank
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Plank'), 'Plank',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Anti-Extension'), 'Anti-Extension'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Plank'), 'Plank',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Anti-Rotational'), 'Anti-Rotational');

-- Plank Jack
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Plank Jack'), 'Plank Jack',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Anti-Rotational'), 'Anti-Rotational');

-- Plank Row
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Plank Row'), 'Plank Row',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Horizontal Pull'), 'Horizontal Pull');

-- Pushup
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Pushup'), 'Pushup',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Horizontal Push'), 'Horizontal Push');

-- Pushup Walk
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Pushup Walk'), 'Pushup Walk',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Horizontal Push'), 'Horizontal Push'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Pushup Walk'), 'Pushup Walk',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Anti-Rotational'), 'Anti-Rotational');

-- Reverse Crunches
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Reverse Crunches'), 'Reverse Crunches',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Anti-Extension'), 'Anti-Extension');

-- Row
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Row'), 'Row',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Horizontal Pull'), 'Horizontal Pull');

-- Russian Twist
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Russian Twist'), 'Russian Twist',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Anti-Rotational'), 'Anti-Rotational');

-- Seal Jacks
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Seal Jacks'), 'Seal Jacks',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Hinge'), 'Hip Hinge'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Seal Jacks'), 'Seal Jacks',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Shoulder Flexion'), 'Shoulder Flexion');

-- Shoulder Press
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Shoulder Press'), 'Shoulder Press',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Shoulder Flexion'), 'Shoulder Flexion');

-- Side Arm / Lateral Raise
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Side Arm / Lateral Raise'),
        'Side Arm / Lateral Raise',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Shoulder Abduction'), 'Shoulder Abduction');

-- Side Lunge
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Side Lunge'), 'Side Lunge',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Adduction'), 'Hip Adduction'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Side Lunge'), 'Side Lunge',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Hinge'), 'Hip Hinge');

-- Side Plank
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Side Plank'), 'Side Plank',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Anti-Lateral Flexion'),
        'Anti-Lateral Flexion');

-- Side Plank Dips
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Side Plank Dips'), 'Side Plank Dips',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Anti-Lateral Flexion'),
        'Anti-Lateral Flexion');

-- Side Plank with Leg Lift
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Side Plank with Leg Lift'),
        'Side Plank with Leg Lift',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Anti-Lateral Flexion'),
        'Anti-Lateral Flexion'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Side Plank with Leg Lift'),
        'Side Plank with Leg Lift', (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Abduction'),
        'Hip Abduction');

-- Single Arm Clean and Press
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Single Arm Clean and Press'),
        'Single Arm Clean and Press', (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Hinge'),
        'Hip Hinge'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Single Arm Clean and Press'),
        'Single Arm Clean and Press',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Shoulder Flexion'), 'Shoulder Flexion');

-- Single Leg Hip Bridge
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Single Leg Hip Bridge'), 'Single Leg Hip Bridge',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Hinge'), 'Hip Hinge');

-- Single Leg Squat
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Single Leg Squat'), 'Single Leg Squat',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Hinge'), 'Hip Hinge');

-- Situp and Throw
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Situp and Throw'), 'Situp and Throw',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Anti-Rotational'), 'Anti-Rotational');

-- Skaters
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Skaters'), 'Skaters',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Hinge'), 'Hip Hinge');

-- Skipping
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Skipping'), 'Skipping',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Hinge'), 'Hip Hinge'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Skipping'), 'Skipping',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Shoulder Flexion'), 'Shoulder Flexion');

-- Skull Crusher
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Skull Crusher'), 'Skull Crusher',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Elbow Flexion'), 'Elbow Flexion'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Skull Crusher'), 'Skull Crusher',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Shoulder Flexion'), 'Shoulder Flexion');

-- Spiderman Pushup
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Spiderman Pushup'), 'Spiderman Pushup',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Anti-Rotational'), 'Anti-Rotational'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Spiderman Pushup'), 'Spiderman Pushup',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Horizontal Push'), 'Horizontal Push');

-- Squat
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Squat'), 'Squat',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Hinge'), 'Hip Hinge'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Squat'), 'Squat',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Adduction'), 'Hip Adduction');

-- Squat Jump
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Squat Jump'), 'Squat Jump',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Hinge'), 'Hip Hinge');

-- Squat Jumps 180
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Squat Jumps 180'), 'Squat Jumps 180',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Hinge'), 'Hip Hinge'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Squat Jumps 180'), 'Squat Jumps 180',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Abduction'), 'Hip Abduction');

-- Squat to Lateral Leg Lift
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Squat to Lateral Leg Lift'),
        'Squat to Lateral Leg Lift', (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Hinge'),
        'Hip Hinge'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Squat to Lateral Leg Lift'),
        'Squat to Lateral Leg Lift',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Abduction'), 'Hip Abduction');

-- Standing Glute Kickbacks
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Standing Glute Kickbacks'),
        'Standing Glute Kickbacks', (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Hinge'),
        'Hip Hinge');

-- Standing Leg Lift
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Standing Leg Lift'), 'Standing Leg Lift',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Abduction'), 'Hip Abduction');

-- Standing Oblique Crunch
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Standing Oblique Crunch'),
        'Standing Oblique Crunch',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Anti-Lateral Flexion'),
        'Anti-Lateral Flexion');

-- Star Jump
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Star Jump'), 'Star Jump',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Hinge'), 'Hip Hinge'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Star Jump'), 'Star Jump',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Shoulder Flexion'), 'Shoulder Flexion');

-- Step Up Lunges
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Step Up Lunges'), 'Step Up Lunges',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Hinge'), 'Hip Hinge'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Step Up Lunges'), 'Step Up Lunges',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Adduction'), 'Hip Adduction');

-- Step-Back Lunge
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Step-Back Lunge'), 'Step-Back Lunge',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Hinge'), 'Hip Hinge'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Step-Back Lunge'), 'Step-Back Lunge',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Adduction'), 'Hip Adduction');

-- Straightup Situp
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Straightup Situp'), 'Straightup Situp',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Anti-Rotational'), 'Anti-Rotational');

-- Sumo Squat
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Sumo Squat'), 'Sumo Squat',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Hinge'), 'Hip Hinge'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Sumo Squat'), 'Sumo Squat',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Adduction'), 'Hip Adduction');

-- Superman
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Superman'), 'Superman',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Hinge'), 'Hip Hinge'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Superman'), 'Superman',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Anti-Extension'), 'Anti-Extension');

-- Touchdown
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Touchdown'), 'Touchdown',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Hinge'), 'Hip Hinge'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Touchdown'), 'Touchdown',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Adduction'), 'Hip Adduction');

-- Tricep Dip
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Tricep Dip'), 'Tricep Dip',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Elbow Flexion'), 'Elbow Flexion'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Tricep Dip'), 'Tricep Dip',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Shoulder Flexion'), 'Shoulder Flexion');

-- Tricep Kick-Back
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Tricep Kick-Back'), 'Tricep Kick-Back',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Elbow Flexion'), 'Elbow Flexion'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Tricep Kick-Back'), 'Tricep Kick-Back',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Shoulder Flexion'), 'Shoulder Flexion');

-- Tricep Overhead Press
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Tricep Overhead Press'), 'Tricep Overhead Press',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Elbow Flexion'), 'Elbow Flexion'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Tricep Overhead Press'), 'Tricep Overhead Press',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Shoulder Flexion'), 'Shoulder Flexion');

-- Tuck Jump
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Tuck Jump'), 'Tuck Jump',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Hinge'), 'Hip Hinge');

-- Twisted Mountain Climbers
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Twisted Mountain Climbers'),
        'Twisted Mountain Climbers', (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Hinge'),
        'Hip Hinge'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Twisted Mountain Climbers'),
        'Twisted Mountain Climbers',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Anti-Rotational'), 'Anti-Rotational');

-- Wall Ball
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Wall Ball'), 'Wall Ball',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Hinge'), 'Hip Hinge'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Wall Ball'), 'Wall Ball',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Shoulder Flexion'), 'Shoulder Flexion');

-- Weighted Jumping Jacks
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Weighted Jumping Jacks'), 'Weighted Jumping Jacks',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Hip Hinge'), 'Hip Hinge'),
       ((SELECT id FROM s_workout.t_exercises WHERE name = 'Weighted Jumping Jacks'), 'Weighted Jumping Jacks',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Shoulder Flexion'), 'Shoulder Flexion');

-- Weighted Punches
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Weighted Punches'), 'Weighted Punches',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Shoulder Flexion'), 'Shoulder Flexion');

-- Deadbug
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, exercise_name, pattern_id, pattern_name)
VALUES ((SELECT id FROM s_workout.t_exercises WHERE name = 'Deadbug'), 'Deadbug',
        (SELECT id FROM s_workout.m_movement_patterns WHERE name = 'Anti-Rotational'), 'Anti-Rotational');
