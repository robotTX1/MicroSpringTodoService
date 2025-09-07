package com.robottx.todoservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "cache-config")
@PropertySource(value = "file:${config-directory}/cache.properties", ignoreResourceNotFound = true)
public class CacheConfig {

    private Map<String, CacheProperties> config;

    @Data
    static class CacheProperties {

        private int maxEntries;
        private int timeToLiveMinutes;

    }

}
