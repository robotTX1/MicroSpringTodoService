package com.robottx.todo.MicroSpringTodoService.config;

import com.robottx.todo.MicroSpringTodoService.service.secret.SecretService;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class SpringConfig {

    @Bean
    public DataSource getDataSource(SecretService secretService) {
        return DataSourceBuilder.create()
                .url(secretService.getDatabaseUrl())
                .username(secretService.getDatabaseUsername())
                .password(secretService.getDatabasePassword())
                .driverClassName("oracle.jdbc.OracleDriver")
                .build();
    }

}
