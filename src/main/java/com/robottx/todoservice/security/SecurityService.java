package com.robottx.todoservice.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    public JwtAuthenticationToken getAuthentication() {
        return (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
    }

    public String getId() {
        return getAuthentication().getName();
    }

    public String getEmail() {
        return ((Jwt) getAuthentication().getPrincipal()).getClaim("email");
    }

}
