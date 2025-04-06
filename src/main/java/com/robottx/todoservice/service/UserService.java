package com.robottx.todoservice.service;

public interface UserService {

    String getUserEmail(String userId);

    String getUserIdByEmail(String email);

}
