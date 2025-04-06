package com.robottx.todoservice.service;

import com.robottx.todoservice.model.KeycloakResponse;
import org.springframework.http.HttpHeaders;

public interface KeycloakService {

    KeycloakResponse getServiceToken();

    HttpHeaders createAuthHeaders();

}
