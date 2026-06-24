package com.epam.rd.autocode.assessment.appliancestore.validation.annotation;

import com.epam.rd.autocode.assessment.appliancestore.validation.PasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
public @interface StrongPassword {
    String message() default "Password is too weak. Please use at least 8 characters,"
            + " including uppercase, lowercase, number and special character.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
