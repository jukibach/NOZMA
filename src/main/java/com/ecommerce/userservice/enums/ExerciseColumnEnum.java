package com.ecommerce.userservice.enums;

import lombok.Getter;

@Getter
public enum ExerciseColumnEnum {
    EXERCISE("name", "text"),
    
    EQUIPMENT("equipments", "multiSelect"),
    
    EXERCISE_TYPE("exerciseTypes", "multiSelect"),
    
    MAJOR_MUSCLE("majorMuscle", "text"),
    
    MUSCLE_GROUP("muscleGroup", "text"),
    
    MECHANIC("mechanics", "text"),
    
    BODY_REGION("bodyRegion", "text"),
    
    LATERALITY("laterality", "text"),
    
    MOVEMENT_PATTERNS("movementPatterns", "multiSelect"),
    
    DESCRIPTION("description", "multilineText");
    
    private final String code;
    private final String type;
    
    ExerciseColumnEnum(String code, String type) {
        this.code = code;
        this.type = type;
        
    }
}
