package com.robottx.todoservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
@ConfigurationProperties(prefix = "database-config")
@PropertySource(value = "file:${config-directory}/database.properties")
public class DatabaseConfig {

    private String url;
    private String username;
    private String password;
    private String usernameSecretOCID;
    private String passwordSecretOCID;

}
