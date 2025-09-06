package com.robottx.todoservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.robottx.todoservice.config.TestConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.junit.jupiter.api.Tag;
import org.junit.platform.commons.util.StringUtils;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
@Tag("IntegrationTest")
@ContextConfiguration(classes = TestConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public abstract class IntegrationTest {

    protected static final String TEST_USER1_ID = "testuser1";
    protected static final String TEST_USER2_ID = "testuser2";
    protected static final String TEST_USER1_EMAIL = "test.user1@example.com";
    protected static final String TEST_USER2_EMAIL = "test.user2@example.com";
    protected static final String USER1_TOKEN_PATH = "token/user1.jwt";
    protected static final String USER2_TOKEN_PATH = "token/user2.jwt";
    protected static final String NO_REQUEST_BODY = null;
    protected static final String NO_RESPONSE_BODY = null;

    private static final String JSON_ASSERT_MESSAGE = "Expected response: |%s|\nActual response: |%s|";
    private static final ObjectMapper PRETTY_PRINTER = new ObjectMapper();

    @Autowired
    protected TestRestTemplate restTemplate;

    protected static void assertResponse(ResponseEntity<String> response, String expectedJsonPath, HttpStatus expectedStatus) {
        assertNotNull(response);
        assertEquals(expectedStatus, response.getStatusCode(), "Response body: " + prettyPrintJson(response.getBody()));
        if (expectedJsonPath == null) {
            assertTrue(StringUtils.isBlank(response.getBody()), "Empty response body expected, got: " + prettyPrintJson(response.getBody()));
        } else {
            assertJson(response.getBody(), expectedJsonPath);
        }
    }

    protected static void assertJson(String actualJson, String expectedJsonPath) {
        String expectedJson = loadText(expectedJsonPath);
        try {
            String message = JSON_ASSERT_MESSAGE.formatted(prettyPrintJson(expectedJson), prettyPrintJson(actualJson));
            JSONAssert.assertEquals(message, expectedJson, actualJson, JSONCompareMode.STRICT);
        } catch (JSONException exception) {
            fail("Invalid response", exception);
        }
    }

    protected static String loadText(String filePath) {
        try {
            InputStream stream = Objects.requireNonNull(IntegrationTest.class.getResourceAsStream(filePath));
            return IOUtils.toString(stream, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            log.error("Failed to load text from {}", filePath, ex);
            throw new RuntimeException("Failed to load text from" + filePath, ex);
        }
    }

    protected static HttpHeaders createAuthorizedHeaders(String tokenPath) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(loadText(tokenPath));
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    protected void testEndpointWithUser1(String url, HttpMethod method, String bodyPath, String responsePath, HttpStatus expectedStatus) {
        testEndpoint(url, method, bodyPath, responsePath, expectedStatus, USER1_TOKEN_PATH);
    }

    protected void testEndpointWithUser2(String url, HttpMethod method, String bodyPath, String responsePath, HttpStatus expectedStatus) {
        testEndpoint(url, method, bodyPath, responsePath, expectedStatus, USER2_TOKEN_PATH);
    }

    protected void testEndpoint(String url, HttpMethod method, String bodyPath, String responsePath, HttpStatus expectedStatus, String userTokenPath) {
        HttpEntity<String> httpEntity;
        HttpHeaders headers = createAuthorizedHeaders(userTokenPath);
        if (bodyPath == null) {
            httpEntity = new HttpEntity<>(headers);
        } else {
            httpEntity = new HttpEntity<>(loadText(bodyPath), headers);
        }
        var response = restTemplate.exchange(url, method, httpEntity, String.class);
        assertResponse(response, responsePath, expectedStatus);
    }

    private static String prettyPrintJson(String json) {
        try {
            Object jsonObject = PRETTY_PRINTER.readValue(json, Object.class);
            return PRETTY_PRINTER.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
        } catch (Exception ex) {
            return json;
        }
    }

}
