package com.robottx.todoservice.validation;

import com.robottx.todoservice.model.PatchTodoRequest;

import java.time.Clock;
import java.time.ZonedDateTime;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FutureWithDefaultValueValidator implements ConstraintValidator<FutureWithDefaultValue, ZonedDateTime> {

    private final Clock clock;

    @Override
    public boolean isValid(ZonedDateTime value, ConstraintValidatorContext context) {
        if (value == null || PatchTodoRequest.DEFAULT_DEADLINE.equals(value)) {
            return true;
        }
        return value.isAfter(ZonedDateTime.now(clock));
    }

}
