package com.robottx.todoservice.validation;

import com.oracle.bmc.util.internal.StringUtils;
import com.robottx.todoservice.model.BaseTodoRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TitleAndDescriptionValidator implements ConstraintValidator<ValidTodoTitleOrDescription, BaseTodoRequest> {

    @Override
    public boolean isValid(BaseTodoRequest value, ConstraintValidatorContext context) {
        return StringUtils.isNotBlank(value.getTitle()) || StringUtils.isNotBlank(value.getDescription());
    }

}
