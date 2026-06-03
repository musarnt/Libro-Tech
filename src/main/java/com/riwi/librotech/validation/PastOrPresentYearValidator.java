package com.riwi.librotech.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class PastOrPresentYearValidator implements ConstraintValidator< PastOrPresentYear, Integer> {

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) return true;
        return value <= LocalDate.now().getYear();
    }
}
