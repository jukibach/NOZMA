package com.nozma.core.service;

import com.nozma.core.dto.request.UpdateDisplaySettingPayload;
import com.nozma.core.dto.response.UpdateDisplaySettingResponse;

public interface DisplayExerciseSettingService {
    UpdateDisplaySettingResponse updateDisplaySetting(long accountId,
                                                      UpdateDisplaySettingPayload updateDisplaySettingPayload);
}
