package com.robottx.todoservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
@ConfigurationProperties(prefix = "service-config")
@PropertySource(value = "file:${config-directory}/service.properties")
public class ServiceConfig {

    // Database
    private String databaseUrl;
    private String databaseUsernameSecretName;
    private String databasePasswordSecretName;

    // OAuth
    private String authorizationServerUrl;
    private String authorizationServerJwksUri;
    private String authorizationServerTokenEndpoint;
    private String applicationRealmSecretName;
    private String applicationClientIdSecretName;
    private String applicationClientSecretSecretName;
    private String adminRealmSecretName;
    private String adminClientIdSecretName;
    private String adminClientSecretSecretName;

}
