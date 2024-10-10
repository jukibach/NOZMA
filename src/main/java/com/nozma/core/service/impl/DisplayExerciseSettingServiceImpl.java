package com.nozma.core.service.impl;

import com.nozma.core.dto.request.UpdateDisplaySettingPayload;
import com.nozma.core.dto.response.UpdateDisplaySettingResponse;
import com.nozma.core.entity.exercises.DisplayExerciseSetting;
import com.nozma.core.enums.StatusAndMessage;
import com.nozma.core.exception.BusinessException;
import com.nozma.core.mapper.DisplaySettingMapper;
import com.nozma.core.repository.DisplayExerciseSettingRepository;
import com.nozma.core.service.DisplayExerciseSettingService;
import com.nozma.core.util.CommonUtil;
import com.nozma.core.util.SecurityUtil;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
@AllArgsConstructor
public class DisplayExerciseSettingServiceImpl implements DisplayExerciseSettingService {
    private final DisplaySettingMapper displaySettingMapper;
    private final DisplayExerciseSettingRepository displayExerciseSettingRepository;
    
    @Override
    @Transactional
    public UpdateDisplaySettingResponse updateDisplaySetting(
            long accountId,
            UpdateDisplaySettingPayload updateDisplaySettingPayload
    ) {
        if (Objects.isNull(SecurityUtil.getCurrentAccountId())
                || !Objects.equals(accountId, SecurityUtil.getCurrentAccountId())
        ) {
            throw new BusinessException(StatusAndMessage.ACCOUNT_DOES_NOT_EXIST);
        }
        
        var displayExerciseSettings = displayExerciseSettingRepository
                .findOneByAccountIdAndCode(SecurityUtil.getCurrentAccountId(), "exercises")
                .orElse(
                        DisplayExerciseSetting.builder()
                        .accountId(SecurityUtil.getCurrentAccountId())
                        .code("exercises")
                        .name(true)
                        .bodyRegion(true)
                        .laterality(true)
                        .majorMuscle(true)
                        .mechanics(true)
                        .equipments(true)
                        .exerciseTypes(true)
                        .muscleGroup(true)
                        .movementPatterns(true)
                        .description(true)
                        .build()
                );
        
        displaySettingMapper.updateEntity(updateDisplaySettingPayload, displayExerciseSettings);
        displayExerciseSettingRepository.save(displayExerciseSettings);
        return displaySettingMapper.entityToResponse(displayExerciseSettings);
    }
}
