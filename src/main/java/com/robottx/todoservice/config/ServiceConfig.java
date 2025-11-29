package com.robottx.todoservice.config;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
@ConfigurationProperties(prefix = "service-config")
@PropertySource(value = "file:${config-directory}/service.properties", ignoreResourceNotFound = true)
public class ServiceConfig {

    // Database
    private String driverClassName;
    private String databaseUrl;
    private String databaseUsernameSecretName;
    private String databasePasswordSecretName;

    // OAuth
    private String authorizationServerIssuerUri;
    private String authorizationServerJwksUri;
    private String authorizationServerTokenEndpoint;
    private String authorizationServerLogoutEndpoint;
    private String authorizationServerUsersEndpoint;
    private String applicationRealmSecretName;
    private String applicationClientIdSecretName;
    private String applicationClientSecretSecretName;
    private String adminRealmSecretName;
    private String adminClientIdSecretName;
    private String adminClientSecretSecretName;

}
