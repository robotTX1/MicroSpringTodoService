package com.robottx.todoservice.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.testcontainers.containers.MySQLContainer;

import javax.sql.DataSource;
import java.time.Clock;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@TestConfiguration
@EnableWebSecurity(debug = true)
public class TestConfig {

    private static final ZoneId TEST_ZONE_ID = ZoneId.of("Europe/Budapest");
    private static final ZonedDateTime FIXED_TIME = ZonedDateTime.of(2025, 8, 16, 15, 30, 10, 0, TEST_ZONE_ID);

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

    @Bean
    @Primary
    public Clock clock() {
        return Clock.fixed(FIXED_TIME.toInstant(), TEST_ZONE_ID);
    }

}
