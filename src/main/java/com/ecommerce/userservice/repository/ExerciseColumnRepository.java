package com.ecommerce.userservice.repository;

import com.ecommerce.userservice.entity.ExerciseColumn;
import com.ecommerce.userservice.enums.RecordStatus;
import com.ecommerce.userservice.projection.ExerciseColumnView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExerciseColumnRepository extends JpaRepository<ExerciseColumn, Integer> {
    List<ExerciseColumnView> findAllByStatusAndCodeIn(RecordStatus status, List<String> columnCodes);
}
