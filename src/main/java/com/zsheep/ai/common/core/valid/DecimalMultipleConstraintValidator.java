package com.zsheep.ai.common.core.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import java.util.Objects;

public class DecimalMultipleConstraintValidator implements ConstraintValidator<DecimalMultiple, Double> {
    
    private double value;
    
    @Override
    public void initialize(DecimalMultiple constraintAnnotation) {
        this.value = constraintAnnotation.value();
    }
    
    @Override
    public boolean isValid(Double d, ConstraintValidatorContext constraintValidatorContext) {
        if (d == null) {
            return true;
        }
        if (Objects.equals(value, 0)) {
            return true;
        }
        try {
            BigDecimal s = BigDecimal.valueOf(d);
            BigDecimal v = BigDecimal.valueOf(value);
            return s.remainder(v).compareTo(BigDecimal.ZERO) == 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
