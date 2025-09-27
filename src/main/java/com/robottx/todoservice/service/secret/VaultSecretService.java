package com.robottx.todoservice.service.secret;

import com.oracle.bmc.secrets.SecretsClient;
import com.oracle.bmc.secrets.model.Base64SecretBundleContentDetails;
import com.oracle.bmc.secrets.requests.GetSecretBundleByNameRequest;
import com.oracle.bmc.secrets.responses.GetSecretBundleByNameResponse;

import com.robottx.todoservice.config.ServiceConfig;
import com.robottx.todoservice.config.VaultConfig;

import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.annotation.PostConstruct;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "secret-provider", havingValue = "vault")
public class VaultSecretService implements SecretService {

    private final SecretsClient secretsClient;
    private final VaultConfig vaultConfig;
    private final ServiceConfig serviceConfig;

    private final Map<String, String> secrets = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        try (secretsClient) {
            // Database
            saveSecret(secretsClient, serviceConfig.getDatabaseUsernameSecretName());
            saveSecret(secretsClient, serviceConfig.getDatabasePasswordSecretName());
            // OAuth
            saveSecret(secretsClient, serviceConfig.getApplicationRealmSecretName());
            saveSecret(secretsClient, serviceConfig.getApplicationClientIdSecretName());
            saveSecret(secretsClient, serviceConfig.getApplicationClientSecretSecretName());
            saveSecret(secretsClient, serviceConfig.getAdminRealmSecretName());
            saveSecret(secretsClient, serviceConfig.getAdminClientIdSecretName());
            saveSecret(secretsClient, serviceConfig.getAdminClientSecretSecretName());
        }
        log.debug("VaultSecretService initialized");
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

    private void saveSecret(SecretsClient client, String secretName) {
        secrets.put(secretName, getSecretFromVault(client, secretName));
        log.trace("Fetched secret {}", secretName);
    }

    private String getSecretFromVault(SecretsClient client, String secretName) {
        GetSecretBundleByNameRequest secretBundleRequest = GetSecretBundleByNameRequest.builder()
                .vaultId(vaultConfig.getVaultOcid())
                .secretName(secretName)
                .build();
        GetSecretBundleByNameResponse secretBundle = client.getSecretBundleByName(secretBundleRequest);
        Base64SecretBundleContentDetails secretBundleContent =
                (Base64SecretBundleContentDetails) secretBundle.getSecretBundle().getSecretBundleContent();
        return new String(Base64.getDecoder().decode(secretBundleContent.getContent()));
    }

}
