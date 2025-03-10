package com.robottx.todoservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
@ConfigurationProperties(prefix = "oauth-config")
@PropertySource(value = "file:${config-directory}/oauth.properties")
public class OAuthConfig {

    private String jwksUri;

}
