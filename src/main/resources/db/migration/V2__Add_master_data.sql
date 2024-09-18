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

INSERT INTO s_workout.m_major_muscles(id, name)
VALUES (1, 'Core'),
       (2, 'Arms'),
       (3, 'Back'),
       (4, 'Chest'),
       (5, 'Shoulders'),
       (6, 'Legs'),
       (7, 'Other'),
       (8, 'Full Body');

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

INSERT INTO s_workout.m_categories(id, name)
VALUES (1, 'Cardio'),
       (2, 'Weight'),
       (3, 'Plyo'),
       (4, 'Machine');

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

INSERT INTO s_workout.m_equipments(id, name)
VALUES (1, 'Band'),
       (2, 'Dumbbells'),
       (3, 'Bench'),
       (4, 'Barbell'),
       (5, 'Bar'),
       (6, 'Cable'),
       (7, 'Body Weight'),
       (8, 'Platform'),
       (9, 'Machine'),
       (10, 'Kettle Bells'),
       (11, 'Landmine'),
       (12, 'Squat Rack'),
       (13, 'Rope'),
       (14, 'Medicine Ball');

CREATE TABLE s_workout.m_laterality
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

INSERT INTO s_workout.m_laterality(id, name)
VALUES (1, 'Bilateral'),
       (2, 'Contralateral'),
       (3, 'Unilateral');

CREATE TABLE s_workout.m_body_regions
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

INSERT INTO s_workout.m_body_regions(id, name)
VALUES (1, 'Upper Body'),
       (2, 'Midsection'),
       (3, 'Lower Body'),
       (4, 'Full Body');

CREATE TABLE s_workout.m_mechanics
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

INSERT INTO s_workout.m_mechanics(id, name)
VALUES (1, 'Compound'),
       (2, 'Isolation');

CREATE TABLE s_workout.m_types
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

INSERT INTO s_workout.m_types(id, name)
VALUES (1, 'Cardio'),
       (2, 'Explosive'),
       (3, 'Weight');


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

INSERT INTO s_workout.m_muscle_group(id, name)
VALUES (1, 'Bicep'),
       (2, 'Outer Thigh'),
       (3, 'Glutes'),
       (4, 'Hamstrings'),
       (5, 'Quads'),
       (6, 'Calves'),
       (7, 'Upper Chest'),
       (8, 'Inner Thigh'),
       (9, 'Medium Chest'),
       (10, 'Triceps'),
       (11, 'Lats'),
       (12, 'Oblique'),
       (13, 'Lateral Delts'),
       (14, 'Anterior Delts'),
       (15, 'Rear Delts'),
       (16, 'Traps'),
       (17, 'Upper Back'),
       (18, 'Lower Back'),
       (19, 'Neck'),
       (20, 'Forearms'),
       (21, 'Hip Flexors'),
       (22, 'Full Body');

CREATE TABLE s_workout.t_exercises
(
    id              BIGSERIAL,
    name            VARCHAR(35) NOT NULL,
    major_muscle_id INTEGER     NOT NULL REFERENCES s_workout.m_major_muscles,
    mechanics_id    INTEGER     NOT NULL REFERENCES s_workout.m_mechanics,
    body_region_id  INTEGER     NOT NULL REFERENCES s_workout.m_body_regions,
    laterality_id   INTEGER     NOT NULL REFERENCES s_workout.m_laterality,
    description     TEXT,
    status          VARCHAR(12) NOT NULL DEFAULT 'ACTIVE',
    created_date    TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date    TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      VARCHAR(20) NOT NULL DEFAULT 'SYSTEM',
    updated_by      VARCHAR(20) NOT NULL DEFAULT 'SYSTEM',
    PRIMARY KEY (id)
);

-- Index for Core partition
CREATE INDEX idx_t_exercises_id ON s_workout.t_exercises (id);

CREATE INDEX idx_t_exercises_name ON s_workout.t_exercises (name);

INSERT INTO s_workout.t_exercises(id, name, mechanics_id, body_region_id, major_muscle_id, laterality_id)
VALUES (1, 'Arnold Press', 1, 1, 5, 1),
       (2, 'Bicep Curl', 2, 1, 2, 1),
       (3, 'Bicycle Crunch', 2, 2, 1, 2),
       (4, 'Bounds', 1, 3, 6, 1),
       (5, 'Box Jumps', 1, 3, 6, 1),
       (6, 'Box Toe Touch', 2, 2, 1, 1),
       (7, 'Broad Jump', 1, 3, 6, 1),
       (8, 'Bulgarian Split Squat', 1, 3, 6, 3),
       (9, 'Burpee', 1, 4, 8, 1),
       (10, 'Burpee Broad Jump', 1, 4, 8, 1),
       (11, 'Butt Kickers', 2, 3, 6, 1),
       (12, 'Calf Raise', 2, 3, 6, 1),
       (13, 'Chest Press', 1, 1, 4, 1),
       (14, 'Close to Wide Grip Burnout', 2, 1, 2, 1),
       (15, 'Compass Jump', 1, 3, 6, 1),
       (16, 'Crab Crawl', 1, 4, 8, 1),
       (17, 'Curtsey Lunges', 1, 3, 6, 3),
       (18, 'Deficit Squat', 1, 3, 6, 1),
       (19, 'Donkey Kick', 2, 3, 6, 3),
       (20, 'Fire Hydrant', 2, 3, 6, 3),
       (21, 'Flutter Kick', 2, 2, 1, 1),
       (22, 'Frogger', 1, 4, 8, 1),
       (23, 'Glute Bridge', 2, 3, 6, 1),
       (24, 'Glute Bridge March', 2, 3, 6, 3),
       (25, 'Goblet Squat', 1, 3, 6, 1),
       (26, 'Halo', 2, 2, 1, 1),
       (27, 'Heart Pump', 1, 4, 8, 1),
       (28, 'High Knees', 1, 3, 6, 1),
       (29, 'Jump Lunges', 1, 3, 6, 1),
       (30, 'Jump Rope', 1, 4, 8, 1),
       (31, 'Jumping Jack Push Press', 1, 4, 8, 1),
       (32, 'Jumping Jacks', 1, 4, 8, 1),
       (33, 'Kettlebell Swing', 1, 4, 8, 1),
       (34, 'Knee Drive', 1, 3, 6, 1),
       (35, 'Lateral Band Walk', 2, 3, 6, 1),
       (36, 'Leg Pull Apart', 2, 3, 6, 1),
       (37, 'Leg Raise', 2, 2, 1, 1),
       (38, 'Literally Just Jumping', 1, 3, 6, 1),
       (39, 'Lying Leg Raises', 2, 2, 1, 1),
       (40, 'Military Plank', 2, 2, 1, 1),
       (41, 'Monkey Jump', 1, 3, 6, 1),
       (42, 'Mountain Climbers', 1, 4, 8, 1),
       (43, 'Plank', 2, 2, 1, 1),
       (44, 'Plank Jack', 1, 4, 1, 1),
       (45, 'Plank Row', 1, 1, 3, 1),
       (46, 'Pushup', 1, 1, 4, 1),
       (47, 'Pushup Walk', 1, 4, 4, 1),
       (48, 'Reverse Crunches', 2, 2, 1, 1),
       (49, 'Row', 1, 1, 3, 1),
       (50, 'Russian Twist', 2, 2, 1, 1),
       (51, 'Seal Jacks', 1, 4, 8, 1),
       (52, 'Shoulder Press', 1, 1, 5, 1),
       (53, 'Side Arm / Lateral Raise', 2, 1, 5, 1),
       (54, 'Side Lunge', 1, 3, 6, 3),
       (55, 'Side Plank', 2, 2, 1, 3),
       (56, 'Side Plank Dips', 2, 2, 1, 3),
       (57, 'Side Plank with Leg Lift', 2, 2, 1, 3),
       (58, 'Single Arm Clean and Press', 1, 4, 8, 3),
       (59, 'Single Leg Hip Bridge', 2, 3, 6, 3),
       (60, 'Single Leg Squat', 1, 3, 6, 3),
       (61, 'Situp and Throw', 2, 2, 1, 1),
       (62, 'Skaters', 1, 3, 6, 1),
       (63, 'Skipping', 1, 4, 8, 1),
       (64, 'Skull Crusher', 2, 1, 2, 1),
       (65, 'Spiderman Pushup', 1, 2, 1, 1),
       (66, 'Squat', 1, 3, 6, 1),
       (67, 'Squat Jump', 1, 3, 6, 1),
       (68, 'Squat Jumps 180', 1, 3, 6, 1),
       (69, 'Squat to Lateral Leg Lift', 1, 3, 6, 3),
       (70, 'Standing Glute Kickbacks', 2, 3, 6, 3),
       (71, 'Standing Leg Lift', 2, 3, 6, 3),
       (72, 'Standing Oblique Crunch', 2, 2, 1, 1),
       (73, 'Star Jump', 1, 4, 8, 1),
       (74, 'Step Up Lunges', 1, 3, 6, 3),
       (75, 'Step-Back Lunge', 1, 3, 6, 3),
       (76, 'Straightup Situp', 2, 2, 1, 1),
       (77, 'Sumo Squat', 1, 3, 6, 1),
       (78, 'Superman', 2, 2, 1, 1),
       (79, 'Touchdown', 1, 3, 6, 1),
       (80, 'Tricep Dip', 2, 1, 2, 1),
       (81, 'Tricep Kick-Back', 2, 1, 2, 1),
       (82, 'Tricep Overhead Press', 2, 1, 2, 1),
       (83, 'Tuck Jump', 1, 3, 6, 1),
       (84, 'Twisted Mountain Climbers', 1, 4, 8, 1),
       (85, 'Wall Ball', 1, 4, 8, 1),
       (86, 'Weighted Jumping Jacks', 1, 4, 8, 1),
       (87, 'Weighted Punches', 1, 4, 8, 1),
       (88, 'Deadbug', 2, 2, 1, 1);

CREATE TABLE s_workout.t_exercises_types
(
    id           BIGSERIAL PRIMARY KEY,
    type_id      BIGINT      NOT NULL REFERENCES s_workout.m_types,
    exercise_id  INT         NOT NULL REFERENCES s_workout.t_exercises,
    status       VARCHAR(12) NOT NULL DEFAULT 'ACTIVE',
    created_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by   VARCHAR(20) NOT NULL DEFAULT 'SYSTEM',
    updated_by   VARCHAR(20) NOT NULL DEFAULT 'SYSTEM'
);

-- Cardio (1)
INSERT INTO s_workout.t_exercises_types (type_id, exercise_id)
VALUES (1, 3),  -- Bicycle Crunch
       (1, 4),  -- Bounds
       (1, 9),  -- Burpee
       (1, 11), -- Butt Kickers
       (1, 19), -- Donkey Kick
       (1, 20), -- Fire Hydrant
       (1, 21), -- Flutter Kick
       (1, 22), -- Frogger
       (1, 23), -- Glute Bridge
       (1, 24), -- Glute Bridge March
       (1, 27), -- Heart Pump
       (1, 28), -- High Knees
       (1, 30), -- Jump Rope
       (1, 32), -- Jumping Jacks
       (1, 34), -- Knee Drive
       (1, 37), -- Leg Raise
       (1, 38), -- Literally Just Jumping
       (1, 39), -- Lying Leg Raises
       (1, 41), -- Monkey Jump
       (1, 42), -- Mountain Climbers
       (1, 43), -- Plank
       (1, 44), -- Plank Jack
       (1, 48), -- Reverse Crunches
       (1, 49), -- Row
       (1, 50), -- Russian Twist
       (1, 51), -- Seal Jacks
       (1, 55), -- Side Plank
       (1, 59), -- Single Leg Hip Bridge
       (1, 62), -- Skaters
       (1, 63), -- Skipping
       (1, 66), -- Squat Jump
       (1, 67), -- Squat Jumps 180
       (1, 70), -- Standing Glute Kickbacks
       (1, 71), -- Standing Leg Lift
       (1, 72), -- Standing Oblique Crunch
       (1, 74), -- Step Up Lunges
       (1, 75), -- Step-Back Lunge
       (1, 78), -- Superman
       (1, 79), -- Touchdown
       (1, 83), -- Tuck Jump
       (1, 84);
-- Twisted Mountain Climbers

-- Explosive (2)
INSERT INTO s_workout.t_exercises_types (type_id, exercise_id)
VALUES (2, 5),  -- Box Jumps
       (2, 7),  -- Broad Jump
       (2, 10), -- Burpee Broad Jump
       (2, 15), -- Compass Jump
       (2, 14), -- Close to Wide Grip Burnout
       (2, 29), -- Jump Lunges
       (2, 31), -- Jumping Jack Push Press
       (2, 35), -- Lateral Band Walk
       (2, 41), -- Monkey Jump
       (2, 45), -- Plank Row
       (2, 52), -- Shoulder Press
       (2, 58), -- Single Arm Clean and Press
       (2, 61), -- Situp and Throw
       (2, 64), -- Skull Crusher
       (2, 65), -- Spiderman Pushup
       (2, 68), -- Squat to Lateral Leg Lift
       (2, 73), -- Star Jump
       (2, 85);
-- Wall Ball


-- Weight (3)
INSERT INTO s_workout.t_exercises_types (type_id, exercise_id)
VALUES (3, 1),  -- Arnold Press
       (3, 2),  -- Bicep Curl
       (3, 8),  -- Bulgarian Split Squat
       (3, 12), -- Calf Raise
       (3, 13), -- Chest Press
       (3, 16), -- Crab Crawl
       (3, 17), -- Curtsey Lunges
       (3, 18), -- Deficit Squat
       (3, 25), -- Goblet Squat
       (3, 26), -- Halo
       (3, 33), -- Kettlebell Swing
       (3, 36), -- Leg Pull Apart
       (3, 46), -- Pushup
       (3, 47), -- Pushup Walk
       (3, 50), -- Russian Twist
       (3, 53), -- Side Arm / Lateral Raise
       (3, 54), -- Side Lunge
       (3, 56), -- Side Plank Dips
       (3, 57), -- Side Plank with Leg Lift
       (3, 60), -- Single Leg Squat
       (3, 64), -- Skull Crusher
       (3, 66), -- Squat
       (3, 77), -- Sumo Squat
       (3, 80), -- Tricep Dip
       (3, 81), -- Tricep Kick-Back
       (3, 82), -- Tricep Overhead Press
       (3, 86), -- Weighted Jumping Jacks
       (3, 87), -- Weighted Punches
       (3, 88); -- Deadbug

CREATE TABLE s_workout.t_exercises_muscles_group
(
    id              BIGSERIAL PRIMARY KEY,
    exercise_id     BIGINT      NOT NULL REFERENCES s_workout.t_exercises,
    muscle_group_id INT         NOT NULL REFERENCES s_workout.m_muscle_group,
    status          VARCHAR(12) NOT NULL DEFAULT 'ACTIVE',
    created_date    TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date    TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      VARCHAR(20) NOT NULL DEFAULT 'SYSTEM',
    updated_by      VARCHAR(20) NOT NULL DEFAULT 'SYSTEM'
);

-- Arnold Press
INSERT INTO s_workout.t_exercises_muscles_group (exercise_id, muscle_group_id)
VALUES (1, 13),
       (1, 14),
       (1, 7),

-- Bicep Curl
       (2, 1),
       (2, 20),

-- Bicycle Crunch
       (3, 12),
       (3, 21),

-- Bounds
       (4, 2),
       (4, 3),
       (4, 4),

-- Box Jumps
       (5, 5),
       (5, 6),
       (5, 3),
       (5, 4),

-- Box Toe Touch
       (6, 3),
       (6, 4),

-- Broad Jump
       (7, 5),
       (7, 6),
       (7, 3),
       (7, 4),

-- Bulgarian Split Squat
       (8, 5),
       (8, 3),
       (8, 4),
       (8, 8),

-- Burpee
       (9, 5),
       (9, 6),
       (9, 3),
       (9, 4),
       (9, 12),
       (9, 10),
       (9, 20),
       (9, 21),

-- Burpee Broad Jump
       (10, 5),
       (10, 6),
       (10, 3),
       (10, 4),
       (10, 12),
       (10, 10),
       (10, 20),
       (10, 21),

-- Butt Kickers
       (11, 4),
       (11, 6),

-- Calf Raise
       (12, 6),

-- Chest Press
       (13, 9),
       (13, 10),
       (13, 7),

-- Close to Wide Grip Burnout
       (14, 10),
       (14, 7),

-- Compass Jump
       (15, 2),
       (15, 3),
       (15, 21),

-- Crab Crawl
       (16, 3),
       (16, 10),
       (16, 12),

-- Curtsey Lunges
       (17, 2),
       (17, 3),
       (17, 5),

-- Deficit Squat
       (18, 5),
       (18, 3),
       (18, 4),

-- Donkey Kick
       (19, 3),

-- Fire Hydrant
       (20, 3),

-- Flutter Kick
       (21, 21),
       (21, 12),

-- Frogger
       (22, 5),
       (22, 6),
       (22, 3),
       (22, 4),

-- Glute Bridge
       (23, 3),

-- Glute Bridge March
       (24, 3),

-- Goblet Squat
       (25, 5),
       (25, 3),
       (25, 4),

-- Halo
       (26, 13),
       (26, 14),
       (26, 7),

-- Heart Pump
       (27, 5),
       (27, 6),
       (27, 3),
       (27, 21),

-- High Knees
       (28, 21),
       (28, 6),

-- Jump Lunges
       (29, 5),
       (29, 3),
       (29, 4),

-- Jump Rope
       (30, 6),
       (30, 21),

-- Jumping Jack Push Press
       (31, 13),
       (31, 14),
       (31, 7),
       (31, 19),

-- Jumping Jacks
       (32, 6),
       (32, 21),

-- Kettlebell Swing
       (33, 3),
       (33, 4),
       (33, 5),

-- Knee Drive
       (34, 21),
       (34, 5),

-- Lateral Band Walk
       (35, 3),
       (35, 2),

-- Leg Pull Apart
       (36, 2),
       (36, 3),

-- Leg Raise
       (37, 19),
       (37, 21),

-- Literally Just Jumping
       (38, 6),
       (38, 21),

-- Lying Leg Raises
       (39, 21),
       (39, 19),

-- Military Plank
       (40, 19),
       (40, 16),

-- Monkey Jump
       (41, 3),
       (41, 5),
       (41, 4),

-- Mountain Climbers
       (42, 19),
       (42, 21),

-- Plank
       (43, 19),
       (43, 20),

-- Plank Jack
       (44, 19),
       (44, 20),

-- Plank Row
       (45, 19),
       (45, 20),

-- Pushup
       (46, 14),
       (46, 19),
       (46, 7),

-- Pushup Walk
       (47, 10),
       (47, 19),
       (47, 7),

-- Reverse Crunches
       (48, 19),
       (48, 21),

-- Row
       (49, 11),
       (49, 19),

-- Russian Twist
       (50, 19),
       (50, 12),

-- Seal Jacks
       (51, 6),
       (51, 21),

-- Shoulder Press
       (52, 10),
       (52, 7),
       (52, 13),

-- Side Arm / Lateral Raise
       (53, 13),
       (53, 15),

-- Side Lunge
       (54, 2),
       (54, 3),
       (54, 5),

-- Side Plank
       (55, 19),
       (55, 12),

-- Side Plank Dips
       (56, 19),
       (56, 12),

-- Side Plank with Leg Lift
       (57, 19),
       (57, 12),

-- Single Arm Clean and Press
       (58, 10),
       (58, 7),
       (58, 13),

-- Single Leg Hip Bridge
       (59, 3),

-- Single Leg Squat
       (60, 5),
       (60, 3),
       (60, 4),

-- Situp and Throw
       (61, 19),
       (61, 21),

-- Skaters
       (62, 2),
       (62, 3),
       (62, 5),

-- Skipping
       (63, 6),
       (63, 21),

-- Skull Crusher
       (64, 10),
       (64, 19),

-- Spiderman Pushup
       (65, 10),
       (65, 19),
       (65, 7),

-- Squat
       (66, 5),
       (66, 3),
       (66, 4),

-- Squat Jump
       (67, 5),
       (67, 3),
       (67, 4),
       (67, 6),

-- Squat Jumps 180
       (68, 5),
       (68, 3),
       (68, 4),
       (68, 6),

-- Squat to Lateral Leg Lift
       (69, 5),
       (69, 3),
       (69, 4),
       (69, 2),

-- Standing Glute Kickbacks
       (70, 3),
       -- Standing Leg Lift
       (71, 21),
       (71, 3),

-- Standing Side Kick
       (72, 21),
       (72, 3),
       (72, 5),

-- Step-Up
       (73, 5),
       (73, 3),
       (73, 4),

-- Step-Up Knee Drive
       (74, 21),
       (74, 5),

-- Stiff Leg Deadlift
       (75, 4),
       (75, 3),

-- Sumo Squat
       (76, 5),
       (76, 3),
       (76, 4),
       (76, 2),

-- Superman
       (77, 16),
       (77, 19),

-- Toe Tap
       (78, 6),
       (78, 5),

-- Tricep Dips
       (79, 10),
       (79, 19),

-- Tuck Jump
       (80, 21),
       (80, 5),
       (80, 3),

-- Wall Sit
       (81, 5),
       (81, 3),
       (81, 4),

-- Walking Lunge
       (82, 5),
       (82, 3),
       (82, 4),

-- Wide to Narrow Pushup
       (83, 14),
       (83, 19),
       (83, 7),

-- Windmill
       (84, 13),
       (84, 15),

-- Wall Ball
       (85, 5),
       (85, 3),
       (85, 4),
       (85, 6),
       (85, 7),
       (85, 10),
       (85, 13),
       (85, 1),

-- Weighted Jumping Jacks
       (86, 5),
       (86, 3),
       (86, 4),
       (86, 6),
       (86, 13),
       (86, 1),

-- Weighted Punches
       (87, 10),
       (87, 1),
       (87, 13),
       (87, 7),

-- Deadbug
       (88, 1),
       (88, 21),
       (88, 12);


CREATE TABLE s_workout.t_exercises_equipments
(
    id           BIGSERIAL PRIMARY KEY,
    exercise_id  BIGINT      NOT NULL REFERENCES s_workout.t_exercises,
    equipment_id INT         NOT NULL REFERENCES s_workout.m_equipments,
    status       VARCHAR(12) NOT NULL DEFAULT 'ACTIVE',
    created_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by   VARCHAR(20) NOT NULL DEFAULT 'SYSTEM',
    updated_by   VARCHAR(20) NOT NULL DEFAULT 'SYSTEM'
);

-- Arnold Press
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (1, 2),
       (1, 4),
       (1, 7);

-- Bicep Curl
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (2, 2),
       (2, 7);

-- Bicycle Crunch
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (3, 7);

-- Bounds
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (4, 7);

-- Box Jumps
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (5, 8);

-- Box Toe Touch
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (6, 8);

-- Broad Jump
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (7, 7);

-- Bulgarian Split Squat
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (8, 5),
       (8, 7),
       (8, 8);

-- Burpee
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (9, 7);

-- Burpee Broad Jump
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (10, 7);

-- Butt Kickers
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (11, 7);

-- Calf Raise
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (12, 7);

-- Chest Press
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (13, 9);

-- Close to Wide Grip Burnout
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (14, 9);

-- Compass Jump
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (15, 7);

-- Crab Crawl
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (16, 7);

-- Curtsey Lunges
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (17, 5),
       (17, 7);

-- Deficit Squat
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (18, 5);

-- Donkey Kick
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (19, 7);

-- Fire Hydrant
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (20, 7);

-- Flutter Kick
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (21, 7);

-- Frogger
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (22, 7);

-- Glute Bridge
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (23, 7);

-- Glute Bridge March
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (24, 7);

-- Goblet Squat
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (25, 5),
       (25, 7);

-- Halo
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (26, 10);

-- Heart Pump
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (27, 7);

-- High Knees
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (28, 7);

-- Jump Lunges
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (29, 7);

-- Jump Rope
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (30, 6);

-- Jumping Jack Push Press
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (31, 10);

-- Jumping Jacks
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (32, 7);

-- Kettlebell Swing
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (33, 10);

-- Knee Drive
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (34, 7);

-- Lateral Band Walk
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (35, 1);

-- Leg Pull Apart
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (36, 1);

-- Leg Raise
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (37, 7);

-- Literally Just Jumping
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (38, 6);

-- Lying Leg Raises
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (39, 7);

-- Military Plank
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (40, 7);
-- Monkey Jump
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (41, 7);

-- Mountain Climbers
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (42, 7);

-- Plank
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (43, 7);

-- Plank Jack
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (44, 7);

-- Plank Row
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (45, 2),
       (45, 7);

-- Pushup
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (46, 7);

-- Pushup Walk
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (47, 7);

-- Reverse Crunches
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (48, 7);

-- Row
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (49, 9);

-- Russian Twist
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (50, 7),
       (50, 14);

-- Seal Jacks
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (51, 7);

-- Shoulder Press
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (52, 2),
       (52, 4);

-- Side Arm / Lateral Raise
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (53, 2);

-- Side Lunge
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (54, 7);

-- Side Plank
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (55, 7);

-- Side Plank Dips
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (56, 7);

-- Side Plank with Leg Lift
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (57, 7);

-- Single Arm Clean and Press
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (58, 2),
       (58, 4);

-- Single Leg Hip Bridge
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (59, 7);

-- Single Leg Squat
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (60, 7);

-- Situp and Throw
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (61, 14);

-- Skaters
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (62, 7);

-- Skipping
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (63, 6);

-- Skull Crusher
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (64, 2),
       (64, 4);

-- Spiderman Pushup
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (65, 7);

-- Squat
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (66, 7);

-- Squat Jump
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (67, 7);

-- Squat Jumps 180
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (68, 7);

-- Squat to Lateral Leg Lift
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (69, 7);

-- Standing Glute Kickbacks
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (70, 7);

-- Standing Leg Lift
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (71, 7);

-- Standing Oblique Crunch
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (72, 7);

-- Star Jump
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (73, 7);

-- Step Up Lunges
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (74, 8);

-- Step-Back Lunge
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (75, 7);

-- Straightup Situp
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (76, 7);

-- Sumo Squat
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (77, 7);

-- Superman
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (78, 7);

-- Touchdown
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (79, 7);

-- Tricep Dip
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (80, 7),
       (80, 9);

-- Tricep Kick-Back
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (81, 2);

-- Tricep Overhead Press
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (82, 2),
       (82, 4);

-- Tuck Jump
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (83, 7);

-- Twisted Mountain Climbers
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (84, 7);

-- Wall Ball
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (85, 14);

-- Weighted Jumping Jacks
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (86, 2),
       (86, 7);

-- Weighted Punches
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (87, 2);

-- Deadbug
INSERT INTO s_workout.t_exercises_equipments (exercise_id, equipment_id)
VALUES (88, 7);

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

INSERT INTO s_workout.m_movement_patterns(id, name)
VALUES (1, 'Anti-Extension'),
       (2, 'Hip Extension'),
       (3, 'Anti-Rotational'),
       (4, 'Rotational'),
       (5, 'Spinal Flexion'),
       (6, 'Horizontal Push'),
       (7, 'Hip Flexion'),
       (8, 'Lateral Flexion'),
       (9, 'Anti-Lateral Flexion'),
       (10, 'Isometric Hold'),
       (11, 'Horizontal Pull'),
       (12, 'Vertical Pull'),
       (13, 'Hip External Rotation'),
       (14, 'Hip Hinge'),
       (15, 'Ankle Plantar Flexion'),
       (16, 'Vertical Push'),
       (17, 'Loaded Carry'),
       (18, 'Shoulder Scapular Plane Elevation'),
       (19, 'Elbow Flexion'),
       (20, 'Elbow Extension'),
       (21, 'Shoulder Flexion'),
       (22, 'Wrist Extension'),
       (23, 'Ankle Dorsiflexion'),
       (24, 'Hip Abduction'),
       (25, 'Hip Adduction'),
       (26, 'Shoulder Abduction');

CREATE TABLE s_workout.t_exercises_movement_patterns
(
    id           BIGSERIAL PRIMARY KEY,
    exercise_id  BIGSERIAL   NOT NULL REFERENCES s_workout.t_exercises,
    pattern_id   INT         NOT NULL REFERENCES s_workout.m_movement_patterns,
    status       VARCHAR(12) NOT NULL DEFAULT 'ACTIVE',
    created_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by   VARCHAR(20) NOT NULL DEFAULT 'SYSTEM',
    updated_by   VARCHAR(20) NOT NULL DEFAULT 'SYSTEM'
);

-- Arnold Press
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (1, 16), -- Vertical Push
       (1, 19);
-- Elbow Flexion

-- Bicep Curl
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (2, 19), -- Elbow Flexion
       (2, 20);
-- Elbow Extension

-- Bicycle Crunch
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (3, 5), -- Spinal Flexion
       (3, 8);
-- Lateral Flexion

-- Bounds
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (4, 7);
-- Hip Flexion

-- Box Jumps
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (5, 7), -- Hip Flexion
       (5, 15);
-- Ankle Plantar Flexion

-- Box Toe Touch
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (6, 7), -- Hip Flexion
       (6, 5);
-- Spinal Flexion

-- Broad Jump
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (7, 7), -- Hip Flexion
       (7, 15);
-- Ankle Plantar Flexion

-- Bulgarian Split Squat
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (8, 7), -- Hip Flexion
       (8, 15);
-- Ankle Plantar Flexion

-- Burpee
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (9, 7), -- Hip Flexion
       (9, 5), -- Spinal Flexion
       (9, 16);
-- Vertical Push

-- Burpee Broad Jump
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (10, 7),  -- Hip Flexion
       (10, 5),  -- Spinal Flexion
       (10, 16), -- Vertical Push
       (10, 15);
-- Ankle Plantar Flexion

-- Butt Kickers
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (11, 7), -- Hip Flexion
       (11, 15);
-- Ankle Plantar Flexion

-- Calf Raise
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (12, 15);
-- Ankle Plantar Flexion

-- Chest Press
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (13, 6), -- Horizontal Push
       (13, 19);
-- Elbow Flexion

-- Close to Wide Grip Burnout
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (14, 6), -- Horizontal Push
       (14, 19);
-- Elbow Flexion

-- Compass Jump
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (15, 7), -- Hip Flexion
       (15, 15);
-- Ankle Plantar Flexion

-- Crab Crawl
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (16, 7), -- Hip Flexion
       (16, 5);
-- Spinal Flexion

-- Curtsey Lunges
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (17, 7), -- Hip Flexion
       (17, 15);
-- Ankle Plantar Flexion

-- Deficit Squat
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (18, 7), -- Hip Flexion
       (18, 15);
-- Ankle Plantar Flexion

-- Donkey Kick
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (19, 7), -- Hip Flexion
       (19, 14);
-- Hip Hinge

-- Fire Hydrant
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (20, 7), -- Hip Flexion
       (20, 14);
-- Hip Hinge

-- Flutter Kick
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (21, 7), -- Hip Flexion
       (21, 5);
-- Spinal Flexion

-- Frogger
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (22, 7), -- Hip Flexion
       (22, 15);
-- Ankle Plantar Flexion

-- Glute Bridge
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (23, 7), -- Hip Flexion
       (23, 14);
-- Hip Hinge

-- Glute Bridge March
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (24, 7), -- Hip Flexion
       (24, 14);
-- Hip Hinge

-- Goblet Squat
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (25, 7), -- Hip Flexion
       (25, 15);
-- Ankle Plantar Flexion

-- Halo
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (26, 18);
-- Shoulder Scapular Plane Elevation

-- Heart Pump
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (27, 16);
-- Vertical Push

-- High Knees
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (28, 7), -- Hip Flexion
       (28, 15);
-- Ankle Plantar Flexion

-- Jump Lunges
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (29, 7), -- Hip Flexion
       (29, 15);
-- Ankle Plantar Flexion

-- Jump Rope
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (30, 15);
-- Ankle Plantar Flexion

-- Jumping Jack Push Press
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (31, 16), -- Vertical Push
       (31, 15);
-- Ankle Plantar Flexion

-- Jumping Jacks
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (32, 15);
-- Ankle Plantar Flexion

-- Kettlebell Swing
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (33, 7), -- Hip Flexion
       (33, 14);
-- Hip Hinge

-- Knee Drive
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (34, 7);
-- Hip Flexion

-- Lateral Band Walk
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (35, 7);
-- Hip Flexion

-- Leg Pull Apart
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (36, 7);
-- Hip Flexion

-- Leg Raise
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (37, 7);
-- Hip Flexion

-- Literally Just Jumping
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (38, 15);
-- Ankle Plantar Flexion

-- Lying Leg Raises
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (39, 7);
-- Hip Flexion

-- Military Plank
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (40, 10);
-- Isometric Hold

-- Monkey Jump
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (41, 7), -- Hip Flexion
       (41, 15);
-- Ankle Plantar Flexion

-- Mountain Climbers
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (42, 7), -- Hip Flexion
       (42, 15);
-- Ankle Plantar Flexion

-- Plank
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (43, 10);
-- Isometric Hold

-- Plank Jack
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (44, 10), -- Isometric Hold
       (44, 15);
-- Ankle Plantar Flexion

-- Plank Row
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (45, 10), -- Isometric Hold
       (45, 11);
-- Horizontal Pull

-- Pushup
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (46, 6), -- Horizontal Push
       (46, 19);
-- Elbow Flexion

-- Pushup Walk
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (47, 6), -- Horizontal Push
       (47, 19);
-- Elbow Flexion

-- Reverse Crunches
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (48, 5), -- Spinal Flexion
       (48, 7);
-- Hip Flexion

-- Row
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (49, 11), -- Horizontal Pull
       (49, 19);
-- Elbow Flexion

-- Russian Twist
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (50, 5), -- Spinal Flexion
       (50, 8);
-- Lateral Flexion

-- Seal Jacks
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (51, 15);
-- Ankle Plantar Flexion

-- Shoulder Press
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (52, 16), -- Vertical Push
       (52, 21);
-- Shoulder Flexion

-- Side Arm / Lateral Raise
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (53, 16), -- Vertical Push
       (53, 26);
-- Shoulder Abduction

-- Side Lunge
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (54, 7), -- Hip Flexion
       (54, 24);
-- Hip Abduction

-- Side Plank
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (55, 10);
-- Isometric Hold

-- Side Plank Dips
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (56, 10), -- Isometric Hold
       (56, 24);
-- Hip Abduction

-- Side Plank with Leg Lift
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (57, 10), -- Isometric Hold
       (57, 24), -- Hip Abduction
       (57, 7);
-- Hip Flexion

-- Single Arm Clean and Press
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (58, 16), -- Vertical Push
       (58, 11);
-- Horizontal Pull

-- Single Leg Hip Bridge
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (59, 14), -- Hip Hinge
       (59, 7);
-- Hip Flexion

-- Single Leg Squat
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (60, 7), -- Hip Flexion
       (60, 15);
-- Ankle Plantar Flexion

-- Situp and Throw
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (61, 5), -- Spinal Flexion
       (61, 16);
-- Vertical Push

-- Skaters
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (62, 7), -- Hip Flexion
       (62, 15);
-- Ankle Plantar Flexion

-- Skipping
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (63, 15);
-- Ankle Plantar Flexion

-- Skull Crusher
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (64, 20), -- Elbow Extension
       (64, 19);
-- Elbow Flexion

-- Spiderman Pushup
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (65, 6), -- Horizontal Push
       (65, 19);
-- Elbow Flexion

-- Squat
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (66, 7), -- Hip Flexion
       (66, 15);
-- Ankle Plantar Flexion

-- Squat Jump
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (67, 7), -- Hip Flexion
       (67, 15);
-- Ankle Plantar Flexion

-- Squat Jumps 180
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (68, 7), -- Hip Flexion
       (68, 15);
-- Ankle Plantar Flexion

-- Squat to Lateral Leg Lift
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (69, 7), -- Hip Flexion
       (69, 24);
-- Hip Abduction

-- Standing Glute Kickbacks
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (70, 7), -- Hip Flexion
       (70, 14);
-- Hip Hinge

-- Standing Leg Lift
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (71, 7), -- Hip Flexion
       (71, 24);
-- Hip Abduction

-- Standing Oblique Crunch
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (72, 5), -- Spinal Flexion
       (72, 8);
-- Lateral Flexion

-- Star Jump
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (73, 7), -- Hip Flexion
       (73, 15);
-- Ankle Plantar Flexion

-- Step Up Lunges
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (74, 7), -- Hip Flexion
       (74, 15);
-- Ankle Plantar Flexion

-- Step-Back Lunge
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (75, 7), -- Hip Flexion
       (75, 15);
-- Ankle Plantar Flexion

-- Straightup Situp
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (76, 5);
-- Spinal Flexion

-- Sumo Squat
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (77, 7), -- Hip Flexion
       (77, 15);
-- Ankle Plantar Flexion

-- Superman
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (78, 10);
-- Isometric Hold

-- Touchdown
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (79, 7), -- Hip Flexion
       (79, 15);
-- Ankle Plantar Flexion

-- Tricep Dip
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (80, 20), -- Elbow Extension
       (80, 6);
-- Horizontal Push

-- Tricep Kick-Back
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (81, 20), -- Elbow Extension
       (81, 19);
-- Elbow Flexion

-- Tricep Overhead Press
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (82, 16), -- Vertical Push
       (82, 20);
-- Elbow Extension

-- Tuck Jump
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (83, 7), -- Hip Flexion
       (83, 15);
-- Ankle Plantar Flexion

-- Twisted Mountain Climbers
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (84, 7), -- Hip Flexion
       (84, 15);
-- Ankle Plantar Flexion

-- Wall Ball
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (85, 16);
-- Vertical Push

-- Weighted Jumping Jacks
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (86, 15);
-- Ankle Plantar Flexion

-- Weighted Punches
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (87, 16);
-- Vertical Push

-- Deadbug
INSERT INTO s_workout.t_exercises_movement_patterns (exercise_id, pattern_id)
VALUES (88, 10); -- Isometric Hold
