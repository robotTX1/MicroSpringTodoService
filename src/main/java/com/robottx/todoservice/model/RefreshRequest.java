package com.robottx.todoservice.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class RefreshRequest {

    @NotEmpty
    private String refreshToken;

}
