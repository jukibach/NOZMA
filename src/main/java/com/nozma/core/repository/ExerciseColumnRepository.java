package com.nozma.core.repository;

import com.nozma.core.entity.exercises.ExerciseColumn;
import com.nozma.core.enums.RecordStatus;
import com.nozma.core.dto.projections.ExerciseColumnView;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExerciseColumnRepository extends JpaRepository<ExerciseColumn, Integer> {
    String EXERCISE_COLUMN_VIEW_BY_STATUS = "exerciseColumnViewByStatus";
    
    @Cacheable(cacheNames = EXERCISE_COLUMN_VIEW_BY_STATUS)
    List<ExerciseColumn> findAllByStatus(RecordStatus status);
    
}
