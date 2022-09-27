package org.tmdb.controllers;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class AuthV3Controller implements BaseController{

    private String apiKey;

    private String authorizationHeader;

    public AuthV3Controller()
    {
        setUp();
    }

    @Override
    @Step("Set Up AuthV3Controller")
    public void setUp() {
        RestAssured.baseURI = "https://api.themoviedb.org/3/authentication/";
        apiKey = System.getenv("MovieDB_API_Key");
        authorizationHeader = System.getenv("MovieDB_Read_Access_Token");
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
        return getRequest("token/new");
    }

    @Step("Validate Request Token")
    public Response validateRequestToken(String requestToken)
    {
        String username = System.getenv("MovieDB_username");
        String password = System.getenv("MovieDB_password");
        String body = "{" +
                "\"username\": \"" + username  + "\",\n" +
                "\"password\": \"" + password  + "\",\n" +
                "\"request_token\": \"" + requestToken  + "\"" +
                "}";
        return postRequest("token/validate_with_login", body);
    }

    @Step("Create Session")
    public Response createSession(String validatedRequestToken)
    {
        String body = "{" +
                "\"request_token\": \"" + validatedRequestToken  + "\"" +
                "}";
        return postRequest("session/new", body);
    }
}
