package com.robottx.todoservice.model;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class TodoShareDeleteRequest {

    @Email
    private String email;

}
