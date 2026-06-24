package com.epam.rd.autocode.assessment.appliancestore.validation.annotation;

import com.epam.rd.autocode.assessment.appliancestore.validation.CardValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CardValidator.class)
public @interface Card {
    String message() default "Invalid card.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
