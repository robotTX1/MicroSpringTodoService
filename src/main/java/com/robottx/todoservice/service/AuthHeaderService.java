package com.robottx.todoservice.service;

import org.springframework.http.HttpHeaders;

public interface AuthHeaderService {

    HttpHeaders createAuthHeaders();

}
