package com.ecommerce.userservice.mapper;

import com.ecommerce.userservice.dto.ExerciseRow;
import com.ecommerce.userservice.dto.response.ExerciseRowResponse;
import com.ecommerce.userservice.entity.ExerciseEquipment;
import com.ecommerce.userservice.entity.ExerciseMovementPattern;
import com.ecommerce.userservice.entity.ExerciseMuscleGroup;
import com.ecommerce.userservice.util.CommonUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CustomExerciseMapper {
    private final ExerciseMapper exerciseMapper;
    
    public List<ExerciseRowResponse> convertToExerciseRowResponse(List<ExerciseRow> exercises,
                                                                  List<ExerciseEquipment> exerciseEquipments,
                                                                  List<ExerciseMovementPattern> exerciseMovementPatterns,
                                                                  List<ExerciseMuscleGroup> exerciseMuscleGroups) {
        List<ExerciseRowResponse> exerciseRowResponses = new ArrayList<>();
        
        // Group data by exercise ID
        Map<Long, List<String>> groupByExerciseIdAndReturnListOfEquipmentName =
                CommonUtil.isNonNullOrNonEmpty(exerciseEquipments)
                        ? exerciseEquipments.stream().collect(Collectors.groupingBy(ExerciseEquipment::getExerciseId,
                        Collectors.mapping(ExerciseEquipment::getEquipmentName, Collectors.toList()))) : null;
        
        // Group data by exercise ID
        Map<Long, List<String>> groupByExerciseIdAndReturnListOfMovementPatternName =
                CommonUtil.isNonNullOrNonEmpty(exerciseMovementPatterns)
                        ? exerciseMovementPatterns.stream().collect(Collectors.groupingBy(
                        ExerciseMovementPattern::getExerciseId,
                        Collectors.mapping(ExerciseMovementPattern::getPatternName, Collectors.toList()))) : null;
        
        // Group data by exercise ID
        Map<Long, List<String>> groupByExerciseIdAndReturnListOfMuscleGroupName =
                CommonUtil.isNonNullOrNonEmpty(exerciseEquipments)
                        ? exerciseMuscleGroups.stream().collect(Collectors.groupingBy(
                        ExerciseMuscleGroup::getExerciseId, Collectors.mapping(ExerciseMuscleGroup::getMuscleGroupName,
                                Collectors.toList()))) : null;
        
        for (ExerciseRow exercise : exercises) {
            ExerciseRowResponse exerciseRowResponse = exerciseMapper.exerciseRowToResponse(exercise);
            
            if (CommonUtil.isNonNullOrNonEmpty(groupByExerciseIdAndReturnListOfEquipmentName))
                exerciseRowResponse.setEquipments(groupByExerciseIdAndReturnListOfEquipmentName.get(exercise.getId()));
            
            if (CommonUtil.isNonNullOrNonEmpty(groupByExerciseIdAndReturnListOfMovementPatternName))
                exerciseRowResponse.setMovementPatterns(groupByExerciseIdAndReturnListOfMovementPatternName.get(exercise.getId()));
            
            if (CommonUtil.isNonNullOrNonEmpty(groupByExerciseIdAndReturnListOfMuscleGroupName))
                exerciseRowResponse.setMuscleGroup(groupByExerciseIdAndReturnListOfMuscleGroupName.get(exercise.getId()));
            
            setExerciseTypes(exercise, exerciseRowResponse);
            
            exerciseRowResponses.add(exerciseRowResponse);
        }
        return exerciseRowResponses;
    }
    
    private static void setExerciseTypes(ExerciseRow exercise, ExerciseRowResponse exerciseRowResponse) {
        List<String> exerciseType = new ArrayList<>();
        if (CommonUtil.isNonNullOrNonEmpty(exercise.isCardio()))
            exerciseType.add("Cardio");
        if (CommonUtil.isNonNullOrNonEmpty(exercise.isWeight()))
            exerciseType.add("Weight");
        if (CommonUtil.isNonNullOrNonEmpty(exercise.isPlyo()))
            exerciseType.add("Plyo");
        exerciseRowResponse.setExerciseTypes(exerciseType);
    }
    
}
