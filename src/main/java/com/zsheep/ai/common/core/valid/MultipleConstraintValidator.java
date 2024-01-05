package com.zsheep.ai.common.core.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 倍数约束验证器
 *
 * @author Elziy
 */
public class MultipleConstraintValidator implements ConstraintValidator<Multiple, Integer> {
    private int value;
    
    @Override
    public void initialize(Multiple constraintAnnotation) {
        this.value = constraintAnnotation.value();
    }
    
    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        if (integer == null) {
            return true;
        }
        if (value == 0) {
            return true;
        }
        return integer % value == 0;
    }
}
