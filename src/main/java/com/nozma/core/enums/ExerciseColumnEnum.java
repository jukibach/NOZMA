package com.nozma.core.enums;

import lombok.Getter;

@Getter
public enum ExerciseColumnEnum {
    EXERCISE("name", "text", "exercises.name"),
    
    EQUIPMENT("equipments", "multiSelect", null),
    
    EXERCISE_TYPE("exerciseTypes", "multiSelect", null),
    
    MAJOR_MUSCLE("majorMuscle", "text", "major_muscle.name"),
    
    MUSCLE_GROUP("muscleGroup", "text", null),
    
    MECHANIC("mechanics", "text", "mechanics.name"),
    
    BODY_REGION("bodyRegion", "text", "body_regions.name"),
    
    LATERALITY("laterality", "text", "laterality.name"),
    
    MOVEMENT_PATTERNS("movementPatterns", "multiSelect", null),
    
    DESCRIPTION("description", "multilineText", null);
    
    private final String code;
    private final String type;
    private final String field;
    
    ExerciseColumnEnum(String code, String type, String field) {
        this.code = code;
        this.type = type;
        this.field = field;
    }
}
