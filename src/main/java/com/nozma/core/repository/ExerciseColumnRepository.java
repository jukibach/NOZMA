package com.nozma.core.repository;

import com.nozma.core.entity.exercises.ExerciseColumn;
import com.nozma.core.enums.RecordStatus;
import com.nozma.core.dto.projections.ExerciseColumnView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExerciseColumnRepository extends JpaRepository<ExerciseColumn, Integer> {
    
    List<ExerciseColumnView> findAllByStatus(RecordStatus status);
    
    
}
