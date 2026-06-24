package com.epam.rd.autocode.assessment.appliancestore.validation;

import com.epam.rd.autocode.assessment.appliancestore.validation.annotation.StrongPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<StrongPassword, String> {
    private static final String PATTERN_OF_STRONG_PASSWORD =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#!$%^&-+=()])(?=\\S+$).{8,20}$";

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        return password != null
                && Pattern.compile(PATTERN_OF_STRONG_PASSWORD).matcher(password).matches();
    }
}
