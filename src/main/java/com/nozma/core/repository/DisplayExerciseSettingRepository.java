package com.nozma.core.repository;

import com.nozma.core.entity.exercises.DisplayExerciseSetting;
import com.nozma.core.enums.RecordStatus;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DisplayExerciseSettingRepository extends JpaRepository<DisplayExerciseSetting, Integer> {
    
    Optional<DisplayExerciseSetting> findOneByAccountIdAndCode(long accountId, String code);
}
