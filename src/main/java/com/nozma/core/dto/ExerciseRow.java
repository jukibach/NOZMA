package com.nozma.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ExerciseRow {
    @Setter
    private long id;
    @Setter
    private String name;
    @Setter
    private String description;
    @Setter
    private String majorMuscle;
    @Setter
    private String mechanics;
    @Setter
    private String bodyRegion;
    @Setter
    private String laterality;
    @Setter
    private List<String> exerciseTypes;
    @Setter
    private List<String> equipments;
    @Setter
    private List<String> movementPatterns;
    @Setter
    private List<String> muscleGroup;
    
    private String exerciseTypesString;
    private String equipmentsString;
    private String movementPatternsString;
    private String muscleGroupString;
    
    public void setExerciseTypesString(String exerciseTypesString) {
        this.exerciseTypesString = exerciseTypesString;
        this.exerciseTypes = Arrays.stream(exerciseTypesString.split(",")).map(String::trim).toList();
    }
    
    public void setEquipmentsString(String equipmentsString) {
        this.equipmentsString = equipmentsString;
        this.equipments = Arrays.stream(equipmentsString.split(",")).map(String::trim).toList();
    }
    
    public void setMovementPatternsString(String movementPatternsString) {
        this.movementPatternsString = movementPatternsString;
        this.movementPatterns = Arrays.stream(movementPatternsString.split(",")).map(String::trim).toList();
    }
    
    public void setMuscleGroupString(String muscleGroupString) {
        this.muscleGroupString = muscleGroupString;
        this.muscleGroup = Arrays.stream(muscleGroupString.split(",")).map(String::trim).toList();
    }
}
