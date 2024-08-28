package com.ecommerce.userservice.repository;

import com.ecommerce.userservice.entity.ExerciseMovementPattern;
import com.ecommerce.userservice.enums.RecordStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExerciseMovementPatternRepository extends JpaRepository<ExerciseMovementPattern, Integer> {
    List<ExerciseMovementPattern> findAllByExerciseIdInAndCreatedByInAndStatus(List<Long> exerciseIds, List<String> system,
                                                        RecordStatus recordStatus);
}

