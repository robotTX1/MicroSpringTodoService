package com.robottx.todoservice.service;

import com.robottx.todoservice.exception.NotFoundOrUnauthorizedException;
import com.robottx.todoservice.security.SecurityService;
import com.robottx.todoservice.service.secret.SecretService;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.keycloak.admin.client.Keycloak;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeycloakServiceImpl implements KeycloakService {

    private final Map<String, String> emailCache = new ConcurrentHashMap<>();

    private final Keycloak keycloak;
    private final SecretService secretService;
    private final SecurityService securityService;

    @Override
    public String getUserEmail(String userId) {
        return emailCache.compute(userId, (k, v) -> {
            if (v == null) {
                if (StringUtils.equals(userId, securityService.getId())) {
                    return securityService.getEmail();
                } else {
                    try {
                        return keycloak.realm(secretService.getApplicationRealm())
                                .users()
                                .get(userId)
                                .toRepresentation()
                                .getEmail();
                    } catch (NotFoundException exception) {
                        log.error("User not found with id {}", userId);
                        return "Unknown";
                    }
                }
            }
            return v;
        });
    }

    @Override
    public String getUserIdByEmail(String email) {
        try {
            return keycloak.realm(secretService.getApplicationRealm())
                    .users()
                    .searchByEmail(email, true)
                    .getFirst()
                    .getId();
        } catch (NoSuchElementException exception) {
            log.error("User not found with email {}", email);
            throw new NotFoundOrUnauthorizedException();
        }
    }

}
