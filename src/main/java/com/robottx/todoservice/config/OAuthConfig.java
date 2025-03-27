package com.robottx.todoservice.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
@ConfigurationProperties(prefix = "oauth-config")
@ConditionalOnProperty(name = "secret-provider", havingValue = "local-config")
@PropertySource(value = "file:${config-directory}/oauth.properties", ignoreResourceNotFound = true)
public class OAuthConfig {

    // Application client settings
    private String applicationRealm;
    private String applicationClientId;
    private String applicationClientSecret;

    // Admin client settings
    private String adminRealm;
    private String adminClientId;
    private String adminClientSecret;

}
