package com.robottx.todo.MicroSpringTodoService.service.secret;

import com.robottx.todo.MicroSpringTodoService.config.DatabaseConfig;
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
