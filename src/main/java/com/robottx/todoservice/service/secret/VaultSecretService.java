package com.robottx.todoservice.service.secret;

import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.Region;
import com.oracle.bmc.auth.AbstractAuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.auth.okeworkloadidentity.OkeWorkloadIdentityAuthenticationDetailsProvider;
import com.oracle.bmc.secrets.SecretsClient;
import com.oracle.bmc.secrets.model.Base64SecretBundleContentDetails;
import com.oracle.bmc.secrets.requests.GetSecretBundleByNameRequest;
import com.oracle.bmc.secrets.responses.GetSecretBundleByNameResponse;
import com.robottx.todoservice.config.ServiceConfig;
import com.robottx.todoservice.config.VaultConfig;
import com.robottx.todoservice.exception.InternalServerErrorException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "secret-provider", havingValue = "vault")
public class VaultSecretService implements SecretService {

    private final VaultConfig vaultConfig;
    private final ServiceConfig serviceConfig;

    @Value("${config-directory}")
    private final String configDirectory;

    private final Map<String, String> secrets = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        try (SecretsClient client = SecretsClient.builder()
                .region(Region.fromRegionId(vaultConfig.getVaultRegion()))
                .build(getAuthenticationDetailsProvider())) {
            // Database
            saveSecret(client, serviceConfig.getDatabaseUsernameSecretName());
            saveSecret(client, serviceConfig.getDatabasePasswordSecretName());
            // OAuth
            saveSecret(client, serviceConfig.getApplicationRealmSecretName());
            saveSecret(client, serviceConfig.getApplicationClientIdSecretName());
            saveSecret(client, serviceConfig.getApplicationClientSecretSecretName());
            saveSecret(client, serviceConfig.getAdminRealmSecretName());
            saveSecret(client, serviceConfig.getAdminClientIdSecretName());
            saveSecret(client, serviceConfig.getAdminClientSecretSecretName());
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
                .vaultId(vaultConfig.getVaultOCID())
                .secretName(secretName)
                .build();
        GetSecretBundleByNameResponse secretBundle = client.getSecretBundleByName(secretBundleRequest);
        Base64SecretBundleContentDetails secretBundleContent = (Base64SecretBundleContentDetails) secretBundle.getSecretBundle().getSecretBundleContent();
        return new String(Base64.getDecoder().decode(secretBundleContent.getContent()));
    }

    private AbstractAuthenticationDetailsProvider getAuthenticationDetailsProvider() {
        if (configDirectory.equals("local-config")) {
            log.debug("Using local authentication provider");
            return getConfigFileAuthDetailsProvider();
        } else {
            log.debug("Using oke instance principals authentication provider");
            return getOkeWorkloadIdentityAuthenticationDetailsProvider();
        }
    }

    private AbstractAuthenticationDetailsProvider getOkeWorkloadIdentityAuthenticationDetailsProvider() {
        try {
            return OkeWorkloadIdentityAuthenticationDetailsProvider.builder().build();
        } catch (Exception ex) {
            throw new InternalServerErrorException("Failed to create InstancePrincipalsAuthenticationDetailsProvider", ex);
        }
    }

    private AbstractAuthenticationDetailsProvider getConfigFileAuthDetailsProvider() {
        try {
            ConfigFileReader.ConfigFile configFile = ConfigFileReader.parse("local-config/oci.config");
            return new ConfigFileAuthenticationDetailsProvider(configFile);
        } catch (IOException ex) {
            throw new InternalServerErrorException("Failed to create ConfigFileAuthenticationDetailsProvider", ex);
        }
    }

}
