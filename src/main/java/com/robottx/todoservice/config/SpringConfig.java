package com.robottx.todoservice.config;

import com.robottx.todoservice.service.secret.SecretService;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.Optional;

@Configuration
@EnableTransactionManagement
@EnableJpaAuditing(dateTimeProviderRef = "auditingDateTimeProvider")
public class SpringConfig {

    @Bean
    public Clock getClock() {
        return Clock.systemDefaultZone();
    }

    @Bean
    public DataSource getDataSource(ServiceConfig serviceConfig, SecretService secretService) {
        return DataSourceBuilder.create()
                .url(serviceConfig.getDatabaseUrl())
                .username(secretService.getDatabaseUsername())
                .password(secretService.getDatabasePassword())
                .driverClassName(serviceConfig.getDriverClassName())
                .build();
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean // Makes ZonedDateTime compatible with auditing fields
    public DateTimeProvider auditingDateTimeProvider(Clock clock) {
        return () -> Optional.of(ZonedDateTime.now(clock));
    }

}
