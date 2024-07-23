package com.ecommerce.userservice.annotations;

import com.auth0.jwt.interfaces.Payload;
import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CustomNotNullValidator.class)
@Target({ ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomNotNull {
    String message();
    String fieldCode(); // Use fieldCode instead of fieldName
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}