package com.robottx.todoservice.service.secret;

import com.robottx.todoservice.config.DatabaseConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocalSecretService implements SecretService {

    private final DatabaseConfig databaseConfig;

    @Override
    public String getDatabaseUrl() {
        return databaseConfig.getUrl();
    }

    @Override
    public String getDatabaseUsername() {
        return databaseConfig.getUsername();
    }

    @Override
    public String getDatabasePassword() {
        return databaseConfig.getPassword();
    }

}
