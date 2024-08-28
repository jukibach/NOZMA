package com.ecommerce.userservice.enums;

import lombok.Getter;

@Getter
public enum ExerciseColumnEnum {
    EXERCISE(1, "Exercise"),
    EQUIPMENT(2, "Equipment"),
    EXERCISE_TYPE(3, "Exercise Type"),
    MAJOR_MUSCLE(4, "Major Muscle"),
    MUSCLE_GROUP(5, "Muscle Group"),
    MECHANIC(6, "Mechanic"),
    BODY_REGION(7, "Body Region"),
    LATERALITY(8, "Laterality"),
    MOVEMENT_PATTERNS(9, "Movement Patterns"),
    DESCRIPTION(10, "Description");
    
    private final int columnId;
    private final String columnName;
    
    ExerciseColumnEnum(int columnId, String columnName) {
        this.columnId = columnId;
        this.columnName = columnName;
    }
    
    public static ExerciseColumnEnum fromName(String name) {
        for (ExerciseColumnEnum column : ExerciseColumnEnum.values()) {
            if (column.getColumnName().equalsIgnoreCase(name)) {
                return column;
            }
        }
        throw new IllegalArgumentException("No enum constant for name: " + name);
    }
}
