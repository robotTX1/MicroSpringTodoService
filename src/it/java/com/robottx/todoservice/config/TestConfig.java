package com.robottx.todoservice.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.testcontainers.containers.MySQLContainer;

import javax.sql.DataSource;

@TestConfiguration
@EnableWebSecurity(debug = true)
public class TestConfig {

    @Bean
    @Primary
    public MySQLContainer<?> mysqlContainer() {
        return new MySQLContainer<>("mysql:8.4.6");
    }

    @Bean
    @Primary
    public DataSource getDataSource(MySQLContainer<?> mysqlContainer) {
        return DataSourceBuilder.create()
                .url(mysqlContainer.getJdbcUrl().replace("jdbc:", "jdbc:p6spy:"))
                .username(mysqlContainer.getUsername())
                .password(mysqlContainer.getPassword())
                .driverClassName("com.p6spy.engine.spy.P6SpyDriver")
                .build();
    }

}
