package com.ecommerce.userservice.mapper;

import com.ecommerce.userservice.dto.response.ExerciseRowResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class CustomExerciseMapper {
    public List<ExerciseRowResponse> convertToExerciseRowResponse(List<ExerciseRowResponse> exercises,
                                                                  Map<Long, List<String>> exerciseEquipments,
                                                                  Map<Long, List<String>> exerciseMovementPatterns,
                                                                  Map<Long, List<String>> exerciseMuscleGroups) {
        for (ExerciseRowResponse exercise : exercises) {
            exercise.setEquipments(exerciseEquipments.get(exercise.getId()));
            exercise.setMovementPatterns(exerciseMovementPatterns.get(exercise.getId()));
            exercise.setMuscleGroup(exerciseMuscleGroups.get(exercise.getId()));
            exercise.setEquipments(exerciseEquipments.get(exercise.getId()));
        }
        return exercises;
    }
    
}
