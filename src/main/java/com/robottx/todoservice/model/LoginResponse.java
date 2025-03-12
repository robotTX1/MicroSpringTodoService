package com.robottx.todoservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private String accessToken;
    private Long expiresAt;
    private String refreshToken;
    private Long refreshExpiresAt;
    private String tokenType;
    private String idToken;

}
