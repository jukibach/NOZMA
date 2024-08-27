package com.ecommerce.userservice.controller;

import com.ecommerce.userservice.constant.ApiURL;
import com.ecommerce.userservice.dto.request.ChangePasswordPayload;
import com.ecommerce.userservice.dto.request.LoginRequest;
import com.ecommerce.userservice.dto.request.ReissueTokenPayload;
import com.ecommerce.userservice.dto.request.UserRegistrationRequest;
import com.ecommerce.userservice.dto.response.ApiResponse;
import com.ecommerce.userservice.service.AuthenticationService;
import com.ecommerce.userservice.util.ResponseEntityUtil;
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
        return ResponseEntityUtil.createSuccessResponse(authenticationService.login(request));
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
        return ResponseEntityUtil.createSuccessResponse(authenticationService.reissueToken(payload));
    }
    
    @PostMapping(value = ApiURL.CHANGE_PASSWORD)
    public ResponseEntity<ApiResponse> changePassword(@Valid @RequestBody ChangePasswordPayload payload) {
        authenticationService.changePassword(payload);
        return ResponseEntityUtil.createSuccessResponseWithoutData();
    }
    
    @PostMapping(value = ApiURL.REISSUE_PASSWORD)
    public ResponseEntity<ApiResponse> reissuePassword(@Valid @RequestParam String accountName) {
        authenticationService.reissuePassword(accountName);
        return ResponseEntityUtil.createSuccessResponseWithoutData();
    }
    
    @PostMapping(value = ApiURL.LOGOUT)
    public ResponseEntity<ApiResponse> logout(@Valid @RequestParam String profileToken) {
        authenticationService.logout(profileToken);
        return ResponseEntityUtil.createSuccessResponseWithoutData();
    }
}
