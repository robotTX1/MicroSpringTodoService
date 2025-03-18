package com.robottx.todoservice.controller;

import com.robottx.todoservice.model.LoginRequest;
import com.robottx.todoservice.model.LoginResponse;
import com.robottx.todoservice.service.LoginService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.robottx.todoservice.controller.EndpointConstants.LOGIN_ENDPOINT;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping(LOGIN_ENDPOINT)
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        return ResponseEntity.ok(loginService.login(loginRequest));
    }

}
