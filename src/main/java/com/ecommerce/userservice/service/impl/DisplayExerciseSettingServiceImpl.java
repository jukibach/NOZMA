package com.ecommerce.userservice.service.impl;

import com.ecommerce.userservice.dto.request.UpdateDisplaySettingPayload;
import com.ecommerce.userservice.dto.response.UpdateDisplaySettingResponse;
import com.ecommerce.userservice.enums.StatusAndMessage;
import com.ecommerce.userservice.exception.BusinessException;
import com.ecommerce.userservice.mapper.DisplaySettingMapper;
import com.ecommerce.userservice.repository.DisplayExerciseSettingRepository;
import com.ecommerce.userservice.service.DisplayExerciseSettingService;
import com.ecommerce.userservice.util.CommonUtil;
import com.ecommerce.userservice.util.SecurityUtil;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class DisplayExerciseSettingServiceImpl implements DisplayExerciseSettingService {
    private final DisplaySettingMapper displaySettingMapper;
    private final DisplayExerciseSettingRepository displayExerciseSettingRepository;
    
    @Override
    @Transactional
    public UpdateDisplaySettingResponse updateDisplaySetting(long accountId,
                                                             UpdateDisplaySettingPayload updateDisplaySettingPayload) {
        if (CommonUtil.isNullOrEmpty(SecurityUtil.getCurrentAccountId())
                || accountId != SecurityUtil.getCurrentAccountId())
            throw new BusinessException(StatusAndMessage.ACCOUNT_DOES_NOT_EXIST);
        
        var displayExerciseSettings = displayExerciseSettingRepository.findByAccountIdAndCode(
                SecurityUtil.getCurrentAccountId(), "exercises");
        displaySettingMapper.updateEntity(updateDisplaySettingPayload, displayExerciseSettings);
        displayExerciseSettingRepository.save(displayExerciseSettings);
        return displaySettingMapper.entityToResponse(
                displayExerciseSettings);
    }
}
