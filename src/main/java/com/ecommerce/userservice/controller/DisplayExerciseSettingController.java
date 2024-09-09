package com.ecommerce.userservice.controller;

import com.ecommerce.userservice.constant.ApiURL;
import com.ecommerce.userservice.constant.Privileges;
import com.ecommerce.userservice.dto.request.UpdateDisplaySettingPayload;
import com.ecommerce.userservice.dto.response.ApiResponse;
import com.ecommerce.userservice.service.DisplayExerciseSettingService;
import com.ecommerce.userservice.util.ResponseEntityUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(ApiURL.ROOT_PATH)
public class DisplayExerciseSettingController {
    private final DisplayExerciseSettingService displayExerciseSettingService;
    
    @PreAuthorize("hasAuthority('" + Privileges.UPDATE_DISPLAY_EXERCISE_SETTING + "')")
    @PatchMapping(value = ApiURL.UPDATE_DISPLAY_EXERCISE_SETTING_BY_ID)
    public ResponseEntity<ApiResponse> updateDisplaySetting(@PathVariable("id") long accountId,
                                                            @RequestBody UpdateDisplaySettingPayload payload) {
        displayExerciseSettingService.updateDisplaySetting(accountId,
                payload);
        return ResponseEntityUtil.createSuccessfulPatchResponse();
    }
}
