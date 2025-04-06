package com.robottx.todoservice.service;

import com.robottx.todoservice.model.LoginRequest;
import com.robottx.todoservice.model.LoginResponse;
import com.robottx.todoservice.model.LogoutRequest;
import com.robottx.todoservice.model.RefreshRequest;

public interface AuthService {

    LoginResponse login(LoginRequest loginRequest);

    LoginResponse refresh(RefreshRequest refreshRequest);

    void logout(LogoutRequest refreshRequest);

    void logoutAll();

}
