package com.robottx.todoservice;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

import com.github.tomakehurst.wiremock.WireMockServer;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public abstract class MockServerTest extends IntegrationTest {

    private static final String JWKS_URL = "/realms/applications/protocol/openid-connect/certs";
    private static final String TOKEN_URL = "/realms/master/protocol/openid-connect/token";
    private static final String USER_URL = "/admin/realms/applications/users/";
    private static final String USER_EMAIL_URL = "/admin/realms/applications/users?email=";
    private static final String JWKS_RESPONSE = "secret/jwks.json";
    private static final String GET_SERVICE_TOKEN_RESPONSE = "keycloak/response/get_service_token.json";
    private static final String GET_USER1_DETAILS_RESPONSE = "keycloak/response/get_user_details_user1.json";
    private static final String GET_USER2_DETAILS_RESPONSE = "keycloak/response/get_user_details_user2.json";
    private static final String GET_USER1_DETAILS_BY_EMAIL_RESPONSE = "keycloak/response/get_user_details_by_email_user1.json";
    private static final String GET_USER2_DETAILS_BY_EMAIL_RESPONSE = "keycloak/response/get_user_details_by_email_user2.json";

    protected static WireMockServer mockServer;

    @BeforeAll
    static void beforeAll() {
        startMockServer();
    }

    @AfterAll
    static void afterAll() {
        stopMockServer();
    }

    protected static void startMockServer() {
        mockServer = new WireMockServer(options().port(1080));
        mockServer.start();
    }

    protected static void stopMockServer() {
        mockServer.stop();
    }

    protected static void createDefaultExpectations() {
        createExpectationForJwks();
        createExpectationForToken();
        createExpectationForGetUserDetails(TEST_USER1_ID, GET_USER1_DETAILS_RESPONSE);
        createExpectationForGetUserDetails(TEST_USER2_ID, GET_USER2_DETAILS_RESPONSE);
        createExpectationForFindByEmail("test.user1@example.com", GET_USER1_DETAILS_BY_EMAIL_RESPONSE);
        createExpectationForFindByEmail("test.user2@example.com", GET_USER2_DETAILS_BY_EMAIL_RESPONSE);
    }

    private static void createExpectationForJwks() {
        mockServer.stubFor(get(JWKS_URL)
                .willReturn(okJson(loadText(JWKS_RESPONSE))));
    }

    private static void createExpectationForToken() {
        mockServer.stubFor(post(TOKEN_URL)
                .willReturn(okJson(loadText(GET_SERVICE_TOKEN_RESPONSE))));
    }

    private static void createExpectationForGetUserDetails(String userId, String responsePath) {
        String url = USER_URL + userId;
        mockServer.stubFor(get(url)
                .willReturn(okJson(loadText(responsePath))));
    }

    private static void createExpectationForFindByEmail(String email, String responsePath) {
        String url = USER_EMAIL_URL + email;
        mockServer.stubFor(get(url)
                .willReturn(okJson(loadText(responsePath))));
    }

}
