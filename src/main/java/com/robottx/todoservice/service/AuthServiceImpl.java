package com.robottx.todoservice.service;

import com.robottx.todoservice.config.ServiceConfig;
import com.robottx.todoservice.exception.AuthErrorException;
import com.robottx.todoservice.model.KeycloakErrorResponse;
import com.robottx.todoservice.model.KeycloakResponse;
import com.robottx.todoservice.model.LoginRequest;
import com.robottx.todoservice.model.LoginResponse;
import com.robottx.todoservice.model.RefreshRequest;
import com.robottx.todoservice.service.secret.SecretService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Clock;
import java.time.ZonedDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final Clock clock;
    private final RestTemplate restTemplate;
    private final ServiceConfig serviceConfig;
    private final SecretService secretService;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        log.debug("Received login request");
        KeycloakResponse keycloakResponse = authenticateUser(loginRequest);
        log.debug("Successfully authenticated user");
        return createResponse(keycloakResponse);
    }

    @Override
    public LoginResponse refresh(RefreshRequest refreshRequest) {
        log.debug("Received refresh request");
        KeycloakResponse keycloakResponse = refreshUser(refreshRequest);
        log.debug("Successfully refreshed user");
        return createResponse(keycloakResponse);
    }

    private KeycloakResponse authenticateUser(LoginRequest loginRequest) {
        var requestEntity = new HttpEntity<>(buildLoginBody(loginRequest), createHeaders());
        return callTokenEndpoint(requestEntity);
    }

    private KeycloakResponse refreshUser(RefreshRequest refreshRequest) {
        var requestEntity = new HttpEntity<>(buildRefreshBody(refreshRequest), createHeaders());
        return callTokenEndpoint(requestEntity);
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return headers;
    }

    private MultiValueMap<String, String> buildLoginBody(LoginRequest loginRequest) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "authorization_code");
        form.add("client_id", secretService.getApplicationClientId());
        form.add("client_secret", secretService.getApplicationClientSecret());
        form.add("code", loginRequest.getCode());
        form.add("code_verifier", loginRequest.getCodeVerifier());
        form.add("redirect_uri", loginRequest.getRedirectUri());
        return form;
    }

    private MultiValueMap<String, String> buildRefreshBody(RefreshRequest refreshRequest) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "refresh_token");
        form.add("client_id", secretService.getApplicationClientId());
        form.add("client_secret", secretService.getApplicationClientSecret());
        form.add("refresh_token", refreshRequest.getRefreshToken());
        return form;
    }

    private KeycloakResponse callTokenEndpoint(HttpEntity<MultiValueMap<String, String>> requestEntity) {
        try {
            var response = restTemplate.exchange(buildUrl(), HttpMethod.POST, requestEntity, KeycloakResponse.class);
            return response.getBody();
        } catch (HttpClientErrorException exception) {
            KeycloakErrorResponse errorResponse = exception.getResponseBodyAs(KeycloakErrorResponse.class);
            String errorDescription = "Failed to to call authorization server";
            if (errorResponse != null) {
                errorDescription = errorResponse.getErrorDescription() != null ? errorResponse.getErrorDescription() : errorDescription;
            }
            throw new AuthErrorException(errorDescription);
        }

    }

    private String buildUrl() {
        return UriComponentsBuilder.fromUriString(serviceConfig.getAuthorizationServerTokenEndpoint())
                .buildAndExpand(secretService.getApplicationRealm())
                .toUriString();
    }

    private LoginResponse createResponse(KeycloakResponse keycloakResponse) {
        return LoginResponse.builder()
                .accessToken(keycloakResponse.getAccessToken())
                .refreshToken(keycloakResponse.getRefreshToken())
                .tokenType(keycloakResponse.getTokenType())
                .idToken(keycloakResponse.getIdToken())
                .expiresAt(ZonedDateTime.now(clock).plusSeconds(keycloakResponse.getExpiresIn()).toEpochSecond())
                .refreshExpiresAt(ZonedDateTime.now(clock).plusSeconds(keycloakResponse.getRefreshExpiresIn()).toEpochSecond())
                .build();
    }

}
