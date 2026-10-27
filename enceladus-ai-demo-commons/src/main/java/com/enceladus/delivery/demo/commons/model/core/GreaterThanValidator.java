package com.enceladus.delivery.demo.commons.model.core;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class GreaterThanValidator implements ConstraintValidator<GreaterThan, Integer> {
    private int minValue;

    @Override
    public void initialize(GreaterThan constraintAnnotation) {
        this.minValue = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value > minValue;
    }
}
