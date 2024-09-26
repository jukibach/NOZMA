package com.nozma.core.repository;

import com.nozma.core.entity.exercises.DisplayExerciseSetting;
import com.nozma.core.enums.RecordStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisplayExerciseSettingRepository extends JpaRepository<DisplayExerciseSetting, Integer> {
    DisplayExerciseSetting findByAccountIdAndCode(long accountId, String code);
    
    DisplayExerciseSetting findByStatusAndCode(RecordStatus status, String code);
}
