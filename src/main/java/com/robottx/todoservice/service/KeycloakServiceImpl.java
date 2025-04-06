package com.robottx.todoservice.service;

import com.robottx.todoservice.config.ServiceConfig;
import com.robottx.todoservice.model.KeycloakResponse;
import com.robottx.todoservice.service.secret.SecretService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class KeycloakServiceImpl implements KeycloakService {

    private final RestTemplate restTemplate;
    private final ServiceConfig serviceConfig;
    private final SecretService secretService;

    @Override
    public KeycloakResponse getServiceToken() {
        var requestEntity = new HttpEntity<>(buildBody(), createHeaders());
        var response = restTemplate.exchange(buildServiceTokenUrl(), HttpMethod.POST, requestEntity, KeycloakResponse.class);
        return response.getBody();
    }

    @Override
    public HttpHeaders createAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getServiceToken().getAccessToken());
        return headers;
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return headers;
    }

    private MultiValueMap<String, String> buildBody() {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "client_credentials");
        form.add("client_id", secretService.getAdminClientId());
        form.add("client_secret", secretService.getAdminClientSecret());
        return form;
    }

    private String buildServiceTokenUrl() {
        return UriComponentsBuilder.fromUriString(serviceConfig.getAuthorizationServerTokenEndpoint())
                .buildAndExpand(secretService.getAdminRealm())
                .toUriString();
    }

}
