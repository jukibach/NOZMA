package com.nozma.core.controller;

import com.nozma.core.constant.ApiURL;
import com.nozma.core.dto.request.ChangePasswordPayload;
import com.nozma.core.dto.request.LoginRequest;
import com.nozma.core.dto.request.ReissueTokenPayload;
import com.nozma.core.dto.request.UserRegistrationRequest;
import com.nozma.core.dto.response.ApiResponse;
import com.nozma.core.service.AuthenticationService;
import com.nozma.core.util.ResponseEntityUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@RestController
@AllArgsConstructor
@RequestMapping(ApiURL.ROOT_PATH)
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    
    @PostMapping(value = ApiURL.LOGIN)
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        return ResponseEntityUtil.createSuccessfulOkResponse(authenticationService.login(request));
    }
    
    @PostMapping(value = ApiURL.REGISTER_USER) // Based on AWS
    public ResponseEntity<ApiResponse> registerNewUserAccount(@RequestBody UserRegistrationRequest request)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        return ResponseEntityUtil.createSuccessResponseWithCreatedStatus(
                authenticationService.registerNewAccount(request));
    }
    
    @PostMapping(value = ApiURL.REISSUE_TOKEN)
    public ResponseEntity<ApiResponse> reissueToken(@Valid @RequestBody ReissueTokenPayload payload)
            throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        return ResponseEntityUtil.createSuccessfulOkResponse(authenticationService.reissueToken(payload));
    }
    
    @PostMapping(value = ApiURL.CHANGE_PASSWORD)
    public ResponseEntity<ApiResponse> changePassword(@Valid @RequestBody ChangePasswordPayload payload) {
        authenticationService.changePassword(payload);
        return ResponseEntityUtil.createSuccessResponseWithoutData();
    }
    
    @PostMapping(value = ApiURL.LOGOUT)
    public ResponseEntity<ApiResponse> logout(@Valid @RequestBody String profileToken) {
        authenticationService.logout(profileToken);
        return ResponseEntityUtil.createSuccessResponseWithoutData();
    }
}
