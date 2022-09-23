package org.tmdb.controllers;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class AuthController implements BaseController{

    private String apiKey;
    private String authorizationHeader;

    public AuthController()
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
                .when()
                .patch(path)
                .then()
                .extract()
                .response();
    }

    @Override
    public Response deleteRequest(String path) {
        return requestBase()
                .when()
                .delete(path)
                .then()
                .extract()
                .response();
    }

    public Response createRequestToken()
    {
        return postRequest("request_token","");
    }
}
