package com.tui.proof.domain.validator;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AllowedPilotesCountValidator implements ConstraintValidator<AllowedPilotesCount, Integer> {

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return value == 5 || value == 10 || value == 15;
    }
}
