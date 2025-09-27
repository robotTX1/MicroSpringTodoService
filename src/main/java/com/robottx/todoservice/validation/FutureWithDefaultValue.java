package com.robottx.todoservice.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FutureWithDefaultValueValidator.class)
public @interface FutureWithDefaultValue {

    String message() default "must be a future date";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


}
