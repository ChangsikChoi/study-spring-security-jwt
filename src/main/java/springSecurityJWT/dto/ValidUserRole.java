package springSecurityJWT.dto;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UserRoleValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUserRole {
    String message() default "유효하지 않은 값 입니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
