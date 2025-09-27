package com.robottx.todoservice.service;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthHeaderServiceImpl implements AuthHeaderService {

    private final KeycloakService keycloakService;

    @Override
    public HttpHeaders createAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(keycloakService.getServiceToken().getAccessToken());
        return headers;
    }

}
