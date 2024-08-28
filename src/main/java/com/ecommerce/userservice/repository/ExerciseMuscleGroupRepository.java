package com.ecommerce.userservice.repository;

import com.ecommerce.userservice.entity.ExerciseMuscleGroup;
import com.ecommerce.userservice.enums.RecordStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExerciseMuscleGroupRepository extends JpaRepository<ExerciseMuscleGroup, Integer> {
    List<ExerciseMuscleGroup> findAllByExerciseIdInAndCreatedByInAndStatus(List<Long> exerciseIds, List<String> system,
                                                        RecordStatus recordStatus);
}
