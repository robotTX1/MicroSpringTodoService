package com.robottx.todoservice.service.secret;

public interface SecretService {

    String getSecret(String secretName);

    String getDatabaseUsername();

    String getDatabasePassword();

    String getApplicationRealm();

    String getApplicationClientId();

    String getApplicationClientSecret();

    String getAdminRealm();

    String getAdminClientId();

    String getAdminClientSecret();

}
