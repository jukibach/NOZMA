package com.ecommerce.userservice.service;

import com.ecommerce.userservice.dto.request.UpdateDisplaySettingPayload;
import com.ecommerce.userservice.dto.response.UpdateDisplaySettingResponse;

public interface DisplayExerciseSettingService {
    UpdateDisplaySettingResponse updateDisplaySetting(long accountId,
                                                      UpdateDisplaySettingPayload updateDisplaySettingPayload);
}
