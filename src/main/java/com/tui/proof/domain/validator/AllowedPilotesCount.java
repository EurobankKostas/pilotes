package com.tui.proof.domain.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;
//domain validation logic for pilotes
@Documented
@Constraint(validatedBy = AllowedPilotesCountValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface AllowedPilotesCount {

    String message() default "Number of pilotes must be 5, 10, or 15.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
