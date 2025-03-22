package com.robottx.todoservice.service;

import com.robottx.todoservice.config.ServiceConfig;
import com.robottx.todoservice.exception.InternalServerErrorException;
import com.robottx.todoservice.exception.NotFoundOrUnauthorizedException;
import com.robottx.todoservice.model.KeycloakResponse;
import com.robottx.todoservice.model.KeycloakUserResponse;
import com.robottx.todoservice.service.secret.SecretService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeycloakServiceImpl implements KeycloakService {

    private final ConcurrentMap<String, String> idCache = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, String> emailCache = new ConcurrentHashMap<>();

    private final RestTemplate restTemplate;
    private final SecretService secretService;
    private final ServiceConfig serviceConfig;

    @Override
    public String getUserEmail(String userId) {
        return emailCache.compute(userId, (k, v) -> {
            if (v == null) {
                return findUserEmailById(userId);
            }
            return v;
        });
    }

    @Override
    public String getUserIdByEmail(String email) {
        return idCache.compute(email, (k, v) -> {
            if (v == null) {
                return findUserIdByEmail(email);
            }
            return v;
        });
    }

    private String findUserIdByEmail(String email) {
        KeycloakResponse serviceToken = getServiceToken();
        HttpEntity<Void> httpEntity = new HttpEntity<>(createAuthHeaders(serviceToken.getAccessToken()));
        var response = restTemplate.exchange(buildFindUserByEmailUrl(email), HttpMethod.GET, httpEntity, new ParameterizedTypeReference<List<KeycloakUserResponse>>() {});
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("User not found with email {}", email);
            throw new NotFoundOrUnauthorizedException();
        }
        return Optional.ofNullable(response.getBody())
                .orElseThrow(() -> new InternalServerErrorException("No user id found in response from keycloak"))
                .getFirst()
                .getId();
    }

    private String findUserEmailById(String userId) {
        KeycloakResponse serviceToken = getServiceToken();
        HttpEntity<Void> httpEntity = new HttpEntity<>(createAuthHeaders(serviceToken.getAccessToken()));
        var response = restTemplate.exchange(buildFindUserByIdUrl(userId), HttpMethod.GET, httpEntity, KeycloakUserResponse.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("User not found with id {}", userId);
            throw new NotFoundOrUnauthorizedException();
        }
        return Optional.ofNullable(response.getBody())
                .orElseThrow(() -> new InternalServerErrorException("No user email found in response from keycloak"))
                .getEmail();
    }

    private KeycloakResponse getServiceToken() {
        var requestEntity = new HttpEntity<>(buildBody(), createHeaders());
        var response = restTemplate.exchange(buildServiceTokenUrl(), HttpMethod.POST, requestEntity, KeycloakResponse.class);
        return response.getBody();
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return headers;
    }

    private MultiValueMap<String, String> buildBody() {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "client_credentials");
        form.add("client_id", secretService.getAdminClientId());
        form.add("client_secret", secretService.getAdminClientSecret());
        return form;
    }

    private String buildServiceTokenUrl() {
        return UriComponentsBuilder.fromUriString(serviceConfig.getAuthorizationServerTokenEndpoint())
                .buildAndExpand(secretService.getAdminRealm())
                .toUriString();
    }

    private HttpHeaders createAuthHeaders(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        return headers;
    }

    private String buildFindUserByIdUrl(String userId) {
        return UriComponentsBuilder.fromUriString(serviceConfig.getAuthorizationServerUsersEndpoint())
                .pathSegment(userId)
                .buildAndExpand(secretService.getApplicationRealm())
                .toUriString();
    }

    private String buildFindUserByEmailUrl(String email) {
        return UriComponentsBuilder.fromUriString(serviceConfig.getAuthorizationServerUsersEndpoint())
                .queryParam("email", email)
                .buildAndExpand(secretService.getApplicationRealm())
                .toUriString();
    }

}
