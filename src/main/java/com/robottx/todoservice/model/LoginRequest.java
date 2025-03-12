package com.robottx.todoservice.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginRequest {

    @NotEmpty
    private String code;

    @NotEmpty
    private String codeVerifier;

    @NotEmpty
    private String redirectUri;

}
