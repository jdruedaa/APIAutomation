package org.tmdb.controllers;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class AuthV4Controller implements BaseController{

    private String apiKey;
    private String authorizationHeader;

    public AuthV4Controller()
    {
        setUp();
    }

    @Override
    public void setUp() {
        RestAssured.baseURI = "https://api.themoviedb.org/4/auth/";
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

    public Response createRequestToken()
    {
        String body = "";
        return postRequest("request_token",body);
    }

    public Response validateRequestToken(String requestToken)
    {
        RestAssured.baseURI = "https://api.themoviedb.org/3/authentication/";
        String username = System.getenv("MovieDB_username");
        String password = System.getenv("MovieDB_password");
        String body = "{" +
                "\"username\":\"" + username  + "\",\n" +
                "\"password\":\"" + password  + "\",\n" +
                "\"request_token\":\"" + requestToken  + "\",\n" +
                "}";
        Response response = postRequest("token/validate_with_login", body);
        setUp();
        return response;
    }

    public Response createAccessToken(String requestToken)
    {
        String body = "{\"request_token\":\"" + requestToken  + "\"}";
        return postRequest("access_token", body);
    }

    public Response deleteAccessToken(String accessToken)
    {
        String body = "{\"access_token\":\"" + accessToken  + "\"}";
        return deleteRequest("access_token", body);
    }
}
