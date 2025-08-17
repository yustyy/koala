package com.exskylab.koala.core.customValidations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<ValidEnum, String> {

    private Class<? extends Enum<?>> enumClass;


    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        this.enumClass = constraintAnnotation.enumClass();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context){
        if (value == null) return false;

        for (Enum<?> enumConstant : enumClass.getEnumConstants()){
            if (enumConstant.name().equals(value)){
                return true;
            }
        }
        return false;
    }

}
