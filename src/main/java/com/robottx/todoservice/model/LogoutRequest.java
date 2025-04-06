package com.robottx.todoservice.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LogoutRequest {

    @NotEmpty
    private String refreshToken;

}
