package com.robottx.todoservice.config;

import com.robottx.todoservice.controller.EndpointConstants;
import com.robottx.todoservice.converter.JwtAuthConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${springdoc.api-docs.path}/**")
    private String springDocsPath;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthConverter converter, ServiceConfig serviceConfig) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(EndpointConstants.LOGIN_ENDPOINT).permitAll()
                        .requestMatchers(EndpointConstants.REFRESH_ENDPOINT).permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers(springDocsPath).permitAll()
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(converter)
                                .jwkSetUri(serviceConfig.getAuthorizationServerJwksUri())))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

}
