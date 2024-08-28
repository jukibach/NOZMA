package com.ecommerce.userservice.mapper;

import com.ecommerce.userservice.dto.response.ExerciseRowResponse;
import com.ecommerce.userservice.entity.Exercise;
import com.ecommerce.userservice.enums.ExerciseColumnEnum;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CustomExerciseMapper {
    public List<ExerciseRowResponse> convertToExerciseRowResponse(List<Exercise> exercises,
                                                                  Map<Long, List<String>> exerciseEquipments,
                                                                  Map<Long, List<String>> exerciseMovementPatterns,
                                                                  Map<Long, List<String>> exerciseMuscleGroups) {
        List<ExerciseRowResponse> exerciseRowResponses = new ArrayList<>();
        for (Exercise exercise : exercises) {
            ExerciseRowResponse exerciseRowResponse = new ExerciseRowResponse();
            exerciseRowResponse.setId(exerciseRowResponse.getId());
            exerciseRowResponse.setCreatedDate(exerciseRowResponse.getCreatedDate());
            Map<Integer, Object> cellValuesByColumnId = setExerciseColumnValue(exerciseEquipments,
                    exerciseMovementPatterns, exerciseMuscleGroups,exercise);
            exerciseRowResponse.setCellValuesByColumnId(cellValuesByColumnId);
            exerciseRowResponses.add(exerciseRowResponse);
        }
        return exerciseRowResponses;
    }
    
    private static Map<Integer, Object> setExerciseColumnValue(Map<Long, List<String>> exerciseEquipments,
                                                               Map<Long, List<String>> exerciseMovementPatterns,
                                                               Map<Long, List<String>> exerciseMuscleGroups,
                                                               Exercise exercise) {
        Map<Integer, Object> cellValuesByColumnId = new HashMap<>();
        var equipments = exerciseEquipments.get(exercise.getId());
        cellValuesByColumnId.put(ExerciseColumnEnum.EQUIPMENT.ordinal() + 1, equipments);
        List<String> exerciseType = new ArrayList<>();
        if (exercise.isPlyo())
            exerciseType.add("Plyo");
        if (exercise.isCardio())
            exerciseType.add("Cardio");
        if (exercise.isWeight())
            exerciseType.add("Weight");
        cellValuesByColumnId.put(ExerciseColumnEnum.EXERCISE_TYPE.ordinal() + 1, exerciseType);
        var muscleGroup = exerciseMuscleGroups.get(exercise.getId());
        var movementPatterns = exerciseMovementPatterns.get(exercise.getId());
        cellValuesByColumnId.put(ExerciseColumnEnum.MOVEMENT_PATTERNS.ordinal() + 1, movementPatterns);
        cellValuesByColumnId.put(ExerciseColumnEnum.EXERCISE.ordinal() + 1, exercise.getName());
        cellValuesByColumnId.put(ExerciseColumnEnum.MAJOR_MUSCLE.ordinal() + 1, exercise.getMajorMuscle());
        cellValuesByColumnId.put(ExerciseColumnEnum.MUSCLE_GROUP.ordinal() + 1, muscleGroup);
        cellValuesByColumnId.put(ExerciseColumnEnum.MECHANIC.ordinal() + 1, exercise.getMechanics());
        cellValuesByColumnId.put(ExerciseColumnEnum.LATERALITY.ordinal() + 1, exercise.getLaterality());
        cellValuesByColumnId.put(ExerciseColumnEnum.BODY_REGION.ordinal() + 1, exercise.getBodyRegion());
        cellValuesByColumnId.put(ExerciseColumnEnum.LATERALITY.ordinal() + 1, exercise.getLaterality());
        cellValuesByColumnId.put(ExerciseColumnEnum.DESCRIPTION.ordinal() + 1, exercise.getDescription());
        return cellValuesByColumnId;
    }
}
