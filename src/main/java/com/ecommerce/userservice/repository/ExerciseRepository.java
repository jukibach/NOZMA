package com.ecommerce.userservice.repository;

import com.ecommerce.userservice.entity.Exercise;
import com.ecommerce.userservice.enums.RecordStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    List<Exercise> findAllByStatusAndCreatedByIn(RecordStatus status, List<String> initialCreatedByUsers);
}
