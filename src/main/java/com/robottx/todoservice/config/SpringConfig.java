package com.robottx.todoservice.config;

import com.robottx.todoservice.service.secret.SecretService;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.sql.DataSource;

@Configuration
@EnableJpaAuditing
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
