package com.nozma.core.annotations;

import com.nozma.core.exception.BusinessException;
import com.nozma.core.util.CommonUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class CustomNotNullValidator implements ConstraintValidator<CustomNotNull, String> {
    private String fieldCode;
    private String messageCode;
    private final MessageSource messageSource;
    public CustomNotNullValidator(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
    
    @Override
    public void initialize(CustomNotNull constraintAnnotation) {
        this.fieldCode = constraintAnnotation.fieldCode();
        this.messageCode = constraintAnnotation.message();
    }
    
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (CommonUtil.isNullOrEmpty(value)) {
            String fieldName = messageSource.getMessage(fieldCode, null, LocaleContextHolder.getLocale());
            throw new BusinessException(messageCode, fieldName);
        }
        return true;
    }
    
}