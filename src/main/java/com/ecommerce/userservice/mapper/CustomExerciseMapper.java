package com.ecommerce.userservice.mapper;

import com.ecommerce.userservice.dto.response.ExerciseRowResponse;
import com.ecommerce.userservice.entity.Exercise;
import com.ecommerce.userservice.entity.ExerciseColumn;
import com.ecommerce.userservice.entity.ExerciseEquipment;
import com.ecommerce.userservice.entity.ExerciseMovementPattern;
import com.ecommerce.userservice.entity.ExerciseMuscleGroup;
import com.ecommerce.userservice.enums.ExerciseColumnEnum;
import org.flywaydb.core.internal.util.Pair;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CustomExerciseMapper {
    public List<ExerciseRowResponse> convertToExerciseRowResponse(List<Exercise> exercises,
                                                                  List<ExerciseEquipment> exerciseEquipments,
                                                                  List<ExerciseMovementPattern> exerciseMovementPatterns,
                                                                  List<ExerciseMuscleGroup> exerciseMuscleGroups,
                                                                  List<ExerciseColumn> columns) {
        List<ExerciseRowResponse> exerciseRowResponses = new ArrayList<>();
        for (Exercise exercise : exercises) {
            ExerciseRowResponse exerciseRowResponse = new ExerciseRowResponse();
            exerciseRowResponse.setId(exerciseRowResponse.getId());
            exerciseRowResponse.setCreatedDate(exerciseRowResponse.getCreatedDate());
            
            Map<Integer, Object> cellValuesByColumnId = new HashMap<>();
            for (ExerciseColumn exerciseColumn : columns) {
                setExerciseColumnValue(exerciseEquipments, exerciseMovementPatterns, exerciseMuscleGroups, exercise,
                        exerciseColumn, cellValuesByColumnId);
            }
            exerciseRowResponse.setCellValuesByColumnId(cellValuesByColumnId);
            exerciseRowResponses.add(exerciseRowResponse);
        }
        return exerciseRowResponses;
    }
    
    private static void setExerciseColumnValue(List<ExerciseEquipment> exerciseEquipments,
                                               List<ExerciseMovementPattern> exerciseMovementPatterns,
                                               List<ExerciseMuscleGroup> exerciseMuscleGroups, Exercise exercise,
                                               ExerciseColumn exerciseColumn,
                                               Map<Integer, Object> cellValuesByColumnId) {
        switch (ExerciseColumnEnum.fromName(exerciseColumn.getName())) {
            case EXERCISE -> cellValuesByColumnId.put(exerciseColumn.getId(), exercise.getName());
            case EQUIPMENT -> {
                var equipments = exerciseEquipments.stream()
                        .filter(exerciseEquipment -> exerciseEquipment.getExerciseId() == exercise.getId())
                        .map(exerciseEquipment
                                -> Pair.of(exerciseEquipment.getEquipmentId(), exerciseEquipment.getEquipmentName()))
                        .toList();
                cellValuesByColumnId.put(exerciseColumn.getId(), equipments);
            }
            case EXERCISE_TYPE -> {
                List<String> exerciseType = new ArrayList<>();
                if (exercise.isPlyo())
                    exerciseType.add("Plyo");
                if (exercise.isCardio())
                    exerciseType.add("Cardio");
                if (exercise.isWeight())
                    exerciseType.add("Weight");
                cellValuesByColumnId.put(exerciseColumn.getId(), exerciseType);
            }
            case MAJOR_MUSCLE -> cellValuesByColumnId.put(exerciseColumn.getId(), exercise.getMajorMuscle());
            case MUSCLE_GROUP -> {
                var muscleGroup = exerciseMuscleGroups.stream()
                        .filter(exerciseEquipment -> exerciseEquipment.getExerciseId() == exercise.getId())
                        .map(exerciseEquipment
                                -> Pair.of(exerciseEquipment.getMuscleGroupId(),
                                exerciseEquipment.getMuscleGroupName()))
                        .toList();
                cellValuesByColumnId.put(exerciseColumn.getId(), muscleGroup);
            }
            case MECHANIC -> cellValuesByColumnId.put(exerciseColumn.getId(), exercise.getMechanics());
            case BODY_REGION -> cellValuesByColumnId.put(exerciseColumn.getId(), exercise.getBodyRegion());
            case LATERALITY -> cellValuesByColumnId.put(exerciseColumn.getId(), exercise.getLaterality());
            case MOVEMENT_PATTERNS -> {
                var movementPatterns = exerciseMovementPatterns.stream()
                        .filter(exerciseEquipment -> exerciseEquipment.getExerciseId() == exercise.getId())
                        .map(exerciseEquipment
                                -> Pair.of(exerciseEquipment.getPatternId(),
                                exerciseEquipment.getPatternName()))
                        .toList();
                cellValuesByColumnId.put(exerciseColumn.getId(), movementPatterns);
            }
            case DESCRIPTION -> cellValuesByColumnId.put(exerciseColumn.getId(), exercise.getDescription());
        }
    }
}
