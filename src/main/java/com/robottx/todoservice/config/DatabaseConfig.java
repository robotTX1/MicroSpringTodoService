package com.robottx.todoservice.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
@ConfigurationProperties(prefix = "database-config")
@PropertySource(value = "file:${config-directory}/database.properties")
@ConditionalOnProperty(name = "secret-provider", havingValue = "local-config")
public class DatabaseConfig {

    private String username;
    private String password;

}
