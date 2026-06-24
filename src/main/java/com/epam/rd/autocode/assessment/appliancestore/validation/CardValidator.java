package com.epam.rd.autocode.assessment.appliancestore.validation;

import com.epam.rd.autocode.assessment.appliancestore.validation.annotation.Card;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class CardValidator implements ConstraintValidator<Card, String> {
    private static final String PATTERN_OF_CARD = "^\\d{4}-\\d{4}-\\d{4}-\\d{4}$";

    @Override
    public boolean isValid(String card, ConstraintValidatorContext constraintValidatorContext) {
        return card != null && Pattern.compile(PATTERN_OF_CARD).matcher(card).matches();
    }
}
