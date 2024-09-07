package com.ecommerce.userservice.repository;

import com.ecommerce.userservice.entity.DisplayExerciseSetting;
import com.ecommerce.userservice.enums.RecordStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisplayExerciseSettingRepository extends JpaRepository<DisplayExerciseSetting, Integer> {
    DisplayExerciseSetting findByAccountIdAndCode(long accountId, String code);
    
    DisplayExerciseSetting findByStatusAndCode(RecordStatus status, String code);
}
