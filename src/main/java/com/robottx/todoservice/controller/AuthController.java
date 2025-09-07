package com.robottx.todoservice.controller;

import com.robottx.todoservice.model.LoginRequest;
import com.robottx.todoservice.model.LoginResponse;
import com.robottx.todoservice.model.LogoutRequest;
import com.robottx.todoservice.model.RefreshRequest;
import com.robottx.todoservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.robottx.todoservice.constant.EndpointConstants.LOGIN_ENDPOINT;
import static com.robottx.todoservice.constant.EndpointConstants.LOGOUT_ALL_ENDPOINT;
import static com.robottx.todoservice.constant.EndpointConstants.LOGOUT_ENDPOINT;
import static com.robottx.todoservice.constant.EndpointConstants.REFRESH_ENDPOINT;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(LOGIN_ENDPOINT)
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping(REFRESH_ENDPOINT)
    public ResponseEntity<LoginResponse> refresh(@RequestBody @Valid RefreshRequest refreshRequest) {
        return ResponseEntity.ok(authService.refresh(refreshRequest));
    }

    @PostMapping(LOGOUT_ENDPOINT)
    public ResponseEntity<Void> logout(@RequestBody @Valid LogoutRequest logoutRequest) {
        authService.logout(logoutRequest);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(LOGOUT_ALL_ENDPOINT)
    public ResponseEntity<Void> logoutAll() {
        authService.logoutAll();
        return ResponseEntity.noContent().build();
    }

}
