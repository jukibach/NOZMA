package com.nozma.core.repository;

import com.nozma.core.entity.exercises.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
}
