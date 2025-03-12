package com.robottx.todoservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
@Profile("local")
@ConfigurationProperties(prefix = "oauth-config")
@PropertySource(value = "file:${config-directory}/oauth.properties")
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
