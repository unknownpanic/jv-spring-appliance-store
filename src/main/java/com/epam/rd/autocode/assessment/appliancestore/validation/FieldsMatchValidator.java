package com.epam.rd.autocode.assessment.appliancestore.validation;

import com.epam.rd.autocode.assessment.appliancestore.validation.annotation.FieldsMatch;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

public class FieldsMatchValidator implements ConstraintValidator<FieldsMatch, Object> {
    private String field;
    private String matchField;

    @Override
    public void initialize(FieldsMatch constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.matchField = constraintAnnotation.matchField();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        Object fieldValue = new BeanWrapperImpl(object).getPropertyValue(field);
        Object fieldMatchValue = new BeanWrapperImpl(object).getPropertyValue(matchField);

        boolean isValid = fieldValue != null && fieldValue.equals(fieldMatchValue);

        if (!isValid) {
            context.disableDefaultConstraintViolation();

            context.buildConstraintViolationWithTemplate(
                    context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode(matchField)
                    .addConstraintViolation();
        }

        return isValid;
    }
}
