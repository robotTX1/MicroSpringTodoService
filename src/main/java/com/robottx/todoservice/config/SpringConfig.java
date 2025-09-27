package com.robottx.todoservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.auth.AbstractAuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.auth.InstancePrincipalsAuthenticationDetailsProvider;
import com.oracle.bmc.secrets.SecretsClient;

import com.robottx.todoservice.config.jackson.CustomZonedDateTimeSerializer;
import com.robottx.todoservice.exception.InternalServerErrorException;
import com.robottx.todoservice.service.secret.SecretService;

import java.io.IOException;
import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableCaching
@EnableTransactionManagement
@EnableJpaAuditing(dateTimeProviderRef = "auditingDateTimeProvider")
public class SpringConfig {

    public static ObjectMapper buildObjectMapper(Jackson2ObjectMapperBuilder builder) {
        return builder
                .serializers(new CustomZonedDateTimeSerializer())
                .build();
    }

    @Bean
    public Clock clock() {
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
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        return buildObjectMapper(builder);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean // Makes ZonedDateTime compatible with auditing fields
    public DateTimeProvider auditingDateTimeProvider(Clock clock) {
        return () -> Optional.of(ZonedDateTime.now(clock));
    }

    @Bean
    public SecretsClient secretsClient(AbstractAuthenticationDetailsProvider authenticationDetailsProvider) {
        return SecretsClient.builder().build(authenticationDetailsProvider);
    }

    @Bean
    @ConditionalOnProperty(name = "config-directory", havingValue = "config")
    public AbstractAuthenticationDetailsProvider instancePrincipalDetailsProvider() {
        return InstancePrincipalsAuthenticationDetailsProvider.builder().build();
    }

    @Bean
    @ConditionalOnProperty(name = "config-directory", havingValue = "local-config")
    public AbstractAuthenticationDetailsProvider getConfigFileAuthDetailsProvider(
            @Value("${config-directory}") String configDirectory) {
        try {
            ConfigFileReader.ConfigFile configFile = ConfigFileReader.parse("%s/oci.config".formatted(configDirectory));
            return new ConfigFileAuthenticationDetailsProvider(configFile);
        } catch (IOException ex) {
            throw new InternalServerErrorException("Failed to create ConfigFileAuthenticationDetailsProvider", ex);
        }
    }

    @Bean
    public CacheManager cacheManager(CacheConfig cacheConfig) {
        List<CaffeineCache> caffeineCaches = cacheConfig.getConfig().entrySet().stream()
                .map(entry -> buildCache(entry.getKey(), entry.getValue()))
                .toList();
        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
        simpleCacheManager.setCaches(caffeineCaches);
        return simpleCacheManager;
    }

    private CaffeineCache buildCache(String cacheName, CacheConfig.CacheProperties cacheProperties) {
        return new CaffeineCache(cacheName, Caffeine.newBuilder()
                .expireAfterWrite(cacheProperties.getTimeToLiveMinutes(), TimeUnit.MINUTES)
                .maximumSize(cacheProperties.getMaxEntries())
                .build());
    }

}
