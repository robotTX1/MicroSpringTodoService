package com.robottx.todoservice.service;

import com.robottx.todoservice.config.ServiceConfig;
import com.robottx.todoservice.constant.CacheConstants;
import com.robottx.todoservice.exception.InternalServerErrorException;
import com.robottx.todoservice.exception.UserNotFoundException;
import com.robottx.todoservice.model.KeycloakUserResponse;
import com.robottx.todoservice.service.secret.SecretService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String USER_NOT_FOUND_BY_EMAIL = "User with email %s not found";

    private final RestTemplate restTemplate;
    private final SecretService secretService;
    private final ServiceConfig serviceConfig;
    private final AuthHeaderService authHeaderService;

    @Override
    @Cacheable(CacheConstants.DEFAULT_CACHE)
    public String getUserEmail(String userId) {
        log.debug("Fetching email for user id {}", userId);
        return findUserEmailById(userId);
    }

    @Override
    @Cacheable(CacheConstants.DEFAULT_CACHE)
    public String getUserIdByEmail(String email) {
        log.debug("Fetching user id for email {}", email);
        return findUserIdByEmail(email);
    }

    private String findUserIdByEmail(String email) {
        HttpEntity<Void> httpEntity = new HttpEntity<>(authHeaderService.createAuthHeaders());
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
        HttpEntity<Void> httpEntity = new HttpEntity<>(authHeaderService.createAuthHeaders());
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
