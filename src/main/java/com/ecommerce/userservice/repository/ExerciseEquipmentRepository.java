package com.ecommerce.userservice.repository;

import com.ecommerce.userservice.entity.ExerciseEquipment;
import com.ecommerce.userservice.enums.RecordStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExerciseEquipmentRepository extends JpaRepository<ExerciseEquipment, Integer> {
    List<ExerciseEquipment> findAllByExerciseIdInAndCreatedByInAndStatus(List<Long> exerciseIds,
                                                                         List<String> initialCreatedByUsers,
                                                                         RecordStatus status);
}
