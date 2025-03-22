package com.robottx.todoservice.service.secret;

import com.robottx.todoservice.config.DatabaseConfig;
import com.robottx.todoservice.config.OAuthConfig;
import com.robottx.todoservice.config.ServiceConfig;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Primary
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "secret-provider", havingValue = "local-config")
public class LocalSecretService implements SecretService {

    private final ServiceConfig serviceConfig;
    private final DatabaseConfig databaseConfig;
    private final OAuthConfig oAuthConfig;

    private final Map<String, String> secrets = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        // Database
        secrets.put(serviceConfig.getDatabaseUsernameSecretName(), databaseConfig.getUsername());
        secrets.put(serviceConfig.getDatabasePasswordSecretName(), databaseConfig.getPassword());
        // OAuth
        secrets.put(serviceConfig.getApplicationRealmSecretName(), oAuthConfig.getApplicationRealm());
        secrets.put(serviceConfig.getApplicationClientIdSecretName(), oAuthConfig.getApplicationClientId());
        secrets.put(serviceConfig.getApplicationClientSecretSecretName(), oAuthConfig.getApplicationClientSecret());
        secrets.put(serviceConfig.getAdminRealmSecretName(), oAuthConfig.getAdminRealm());
        secrets.put(serviceConfig.getAdminClientIdSecretName(), oAuthConfig.getAdminClientId());
        secrets.put(serviceConfig.getAdminClientSecretSecretName(), oAuthConfig.getAdminClientSecret());
    }

    @Override
    public String getSecret(String secretName) {
        return secrets.get(secretName);
    }

    @Override
    public String getDatabaseUsername() {
        return getSecret(serviceConfig.getDatabaseUsernameSecretName());
    }

    @Override
    public String getDatabasePassword() {
        return getSecret(serviceConfig.getDatabasePasswordSecretName());
    }

    @Override
    public String getApplicationRealm() {
        return getSecret(serviceConfig.getApplicationRealmSecretName());
    }

    @Override
    public String getApplicationClientId() {
        return getSecret(serviceConfig.getApplicationClientIdSecretName());
    }

    @Override
    public String getApplicationClientSecret() {
        return getSecret(serviceConfig.getApplicationClientSecretSecretName());
    }

    @Override
    public String getAdminRealm() {
        return getSecret(serviceConfig.getAdminRealmSecretName());
    }

    @Override
    public String getAdminClientId() {
        return getSecret(serviceConfig.getAdminClientIdSecretName());
    }

    @Override
    public String getAdminClientSecret() {
        return getSecret(serviceConfig.getAdminClientSecretSecretName());
    }


}
