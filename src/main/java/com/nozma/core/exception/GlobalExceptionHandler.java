package com.nozma.core.exception;

import com.nozma.core.dto.response.ApiResponse;
import com.nozma.core.enums.StatusAndMessage;
import com.nozma.core.util.CommonUtil;
import com.nozma.core.util.ResponseEntityUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@AllArgsConstructor
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private final MessageSource messageSource;
    
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse> handleBusinessException(BusinessException exception) {
        if (CommonUtil.isNonNullOrNonEmpty(exception.getResult()))
            return ResponseEntityUtil.createFailureResponseForInvalidFields(exception, messageSource);
        return ResponseEntityUtil.createFailureResponse(exception, messageSource);
    }
    
    @ExceptionHandler(CredentialsExpiredException.class)
    public ResponseEntity<ApiResponse> handleCredentialsExpiredException(CredentialsExpiredException exception) {
        log.error("Password expired");
        String message = messageSource.getMessage(StatusAndMessage.PASSWORD_EXPIRED.getMessage(), null,
                LocaleContextHolder.getLocale());
        return new ResponseEntity<>(ApiResponse.badRequest(message, null,
                StatusAndMessage.PASSWORD_EXPIRED.getMessage()), HttpStatus.UNAUTHORIZED);
    }
    
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ApiResponse> handleDisabledException(DisabledException exception) {
        log.error("Account was deleted");
        String message = messageSource.getMessage(StatusAndMessage.ACCOUNT_HAS_BEEN_DELETED.getMessage(), null,
                LocaleContextHolder.getLocale());
        return new ResponseEntity<>(ApiResponse.badRequest(message, null,
                StatusAndMessage.ACCOUNT_HAS_BEEN_DELETED.getMessage()), HttpStatus.UNAUTHORIZED);
    }
    
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse> handleBadCredentialsException(BadCredentialsException exception) {
        log.error("Password is incorrect");
        String message = messageSource.getMessage(StatusAndMessage.INCORRECT_PASSWORD.getMessage(), null,
                LocaleContextHolder.getLocale());
        return new ResponseEntity<>(ApiResponse.badRequest(message, null,
                StatusAndMessage.INCORRECT_PASSWORD.getMessage()), HttpStatus.UNAUTHORIZED);
    }
    
    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ApiResponse> handleLockedException(LockedException exception) {
        log.error("Account is locked!");
        String message = messageSource.getMessage(StatusAndMessage.ACCOUNT_LOCKED_AFTER_5_FAILED_ATTEMPTS.getMessage(),
                null, LocaleContextHolder.getLocale());
        return new ResponseEntity<>(ApiResponse.badRequest(message, null,
                StatusAndMessage.ACCOUNT_LOCKED_AFTER_5_FAILED_ATTEMPTS.getMessage()), HttpStatus.UNAUTHORIZED);
    }
}
