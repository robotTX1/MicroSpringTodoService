package com.robottx.todoservice.service;

import com.robottx.todoservice.config.ServiceConfig;
import com.robottx.todoservice.exception.InternalServerErrorException;
import com.robottx.todoservice.exception.UserNotFoundException;
import com.robottx.todoservice.model.KeycloakUserResponse;
import com.robottx.todoservice.service.secret.SecretService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String USER_NOT_FOUND_BY_EMAIL = "User with email %s not found";

    private final ConcurrentMap<String, String> idCache = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, String> emailCache = new ConcurrentHashMap<>();

    private final RestTemplate restTemplate;
    private final SecretService secretService;
    private final ServiceConfig serviceConfig;
    private final KeycloakService keycloakService;

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
            if (StringUtils.isBlank(v)) {
                throw new InternalServerErrorException("Empty email cached");
            }
            return v;
        });
    }

    private String findUserIdByEmail(String email) {
        HttpEntity<Void> httpEntity = new HttpEntity<>(keycloakService.createAuthHeaders());
        var response = restTemplate.exchange(buildFindUserByEmailUrl(email), HttpMethod.GET, httpEntity, new ParameterizedTypeReference<List<KeycloakUserResponse>>() {});
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("User not found with email {}", email);
            throw new UserNotFoundException(USER_NOT_FOUND_BY_EMAIL.formatted(email));
        }
        return Optional.ofNullable(response.getBody())
                .filter(list -> !list.isEmpty())
                .map(list -> list.getFirst().getId())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_BY_EMAIL.formatted(email)));
    }

    private String findUserEmailById(String userId) {
        HttpEntity<Void> httpEntity = new HttpEntity<>(keycloakService.createAuthHeaders());
        var response = restTemplate.exchange(buildFindUserByIdUrl(userId), HttpMethod.GET, httpEntity, KeycloakUserResponse.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("User not found with id {}", userId);
            throw new InternalServerErrorException();
        }
        return Optional.ofNullable(response.getBody())
                .orElseThrow(() -> new InternalServerErrorException("No user email found in response from keycloak"))
                .getEmail();
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
