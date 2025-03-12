package com.robottx.todoservice.service;

import com.robottx.todoservice.model.LoginRequest;
import com.robottx.todoservice.model.LoginResponse;

public interface LoginService {

    LoginResponse login(LoginRequest loginRequest);

}
