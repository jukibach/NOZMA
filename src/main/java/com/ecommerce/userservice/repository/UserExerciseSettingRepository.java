package com.ecommerce.userservice.repository;

import com.ecommerce.userservice.entity.UserExerciseSetting;
import com.ecommerce.userservice.enums.RecordStatus;
import com.ecommerce.userservice.projection.UserExerciseSettingView;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserExerciseSettingRepository extends JpaRepository<UserExerciseSetting, Integer> {
    UserExerciseSettingView findByStatusAndAccountIdAndCode(RecordStatus status, long accountId, String code);
    
    UserExerciseSettingView findByStatusAndCode(RecordStatus status, String code);
}
