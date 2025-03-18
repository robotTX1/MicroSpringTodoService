package com.robottx.todoservice.service;

public interface KeycloakService {

    String getUserEmail(String userId);

    String getUserIdByEmail(String email);

}
