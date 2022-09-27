package org.tmdb.controllers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class AuthV3Controller implements BaseController {

    private String apiKey;

    private String authorizationHeader;

    private final Logger log = LoggerFactory.getLogger(AuthV3Controller.class);

    private String basePath;

    public AuthV3Controller()
    {
        setUp();
    }

    @Override
    @Step("Set Up AuthV3Controller")
    public void setUp() {
        log.info("Performing setup for new instance...");
        try {
            JsonObject paths = JsonParser
                    .parseReader(new FileReader("src/test/java/resources/pagePaths.json"))
                    .getAsJsonObject();
            basePath = paths.get("authV3").getAsString();
        } catch (FileNotFoundException e) {
            log.error("File {} not found.", "pagePaths.json");
        }
        apiKey = System.getenv("MovieDB_API_Key");
        authorizationHeader = System.getenv("MovieDB_Read_Access_Token");
        log.info("Setup finished.");
    }

    @Override
    public RequestSpecification requestBase() {
        return RestAssured.given()
                .contentType("application/json")
                .header("Authorization",authorizationHeader)
                .queryParam("api_key", apiKey);
    }

    @Override
    public Response getRequest(String path) {
        return requestBase()
                .when()
                .get(path)
                .then()
                .extract()
                .response();
    }

    @Override
    public Response postRequest(String path, String body) {
        return requestBase()
                .body(body)
                .when()
                .post(path)
                .then()
                .extract()
                .response();
    }

    @Override
    public Response patchRequest(String path, String body) {
        return requestBase()
                .body(body)
                .when()
                .patch(path)
                .then()
                .extract()
                .response();
    }

    @Override
    public Response deleteRequest(String path, String body) {
        return requestBase()
                .body(body)
                .when()
                .delete(path)
                .then()
                .extract()
                .response();
    }

    @Step("Create Request Token")
    public Response createRequestToken()
    {
        log.info("Requesting new request token...");
        Response response = getRequest(basePath + "token/new");
        log.info("New request token received.");
        return response;
    }

    @Step("Validate Request Token")
    public Response validateRequestToken(String requestToken)
    {
        log.info("Validating request token with login...");
        String username = System.getenv("MovieDB_username");
        String password = System.getenv("MovieDB_password");
        String body = "{" +
                "\"username\": \"" + username  + "\",\n" +
                "\"password\": \"" + password  + "\",\n" +
                "\"request_token\": \"" + requestToken  + "\"" +
                "}";
        Response response = postRequest(basePath + "token/validate_with_login", body);
        log.info("Request token validated with login.");
        return response;
    }

    @Step("Create Session")
    public Response createSession(String validatedRequestToken)
    {
        log.info("Creating new session...");
        String body = "{" +
                "\"request_token\": \"" + validatedRequestToken  + "\"" +
                "}";
        Response response = postRequest(basePath + "session/new", body);
        log.info("New session created.");
        return response;
    }
}
