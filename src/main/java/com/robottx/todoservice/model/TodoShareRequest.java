package com.robottx.todoservice.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class TodoShareRequest {

    @Email
    private String email;

    @Min(0)
    @Max(2)
    private Integer accessLevel;

}
