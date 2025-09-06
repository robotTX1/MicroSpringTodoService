package com.robottx.todoservice.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TitleAndDescriptionValidator.class)
public @interface ValidTodoTitleOrDescription {

    String message() default "Title or description must not be blank";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
