package springSecurityJWT.dto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class UserRoleValidator implements ConstraintValidator<ValidUserRole, UserRole> {
    @Override
    public void initialize(ValidUserRole constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(UserRole value, ConstraintValidatorContext constraintValidatorContext) {
        return value != null && Arrays.asList(UserRole.values()).contains(value);
    }
}
