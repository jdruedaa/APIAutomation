package org.tmdb.controllers;

import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.tmdb.models.list.TMDBList;
import org.tmdb.models.movie.Movie;
import org.tmdb.utils.AuthUtils;

public class ListController implements BaseController{
    private String apiKey;

    private String authorizationHeader;

    private String sessionId;

    private Gson gson;

    public ListController()
    {
        setUp();
    }

    @Override
    public void setUp() {
        sessionId = requestAuthorizedSession();
        RestAssured.baseURI = "https://api.themoviedb.org/3/list";
        apiKey = System.getenv("MovieDB_API_Key");
        authorizationHeader = System.getenv("MovieDB_Read_Access_Token");
        gson = new Gson();
    }

    @Override
    public RequestSpecification requestBase() {
        return RestAssured.given()
                .contentType("application/json;charset=utf-8")
                .header("Authorization",authorizationHeader)
                .queryParam("api_key", apiKey)
                .queryParam("session_id", sessionId);
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

    public Response postRequestClear(String path, String body) {
        return requestBase()
                .queryParam("confirm", true)
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

    public String requestAuthorizedSession()
    {
        AuthUtils authUtils = new AuthUtils();
        return authUtils.createAuthorizedSession();
    }

    public Response getListDetails(int listId)
    {
        return getRequest("" + listId);
    }

    public TMDBList extractListFromResponse(Response response)
    {
        TMDBList tmdbList;
        tmdbList = gson.fromJson(response.getBody().asString(), TMDBList.class);
        return tmdbList;
    }

    public Response createList(String name, String description, String language)
    {
        String body = "{" +
                "\"name\": \"" + name + "\",\n" +
                "\"description\": \"" + description + "\",\n" +
                "\"language\": \"" + language + "\"" +
                "}";
        return postRequest("", body);
    }

    public Response addMovie(int listId, int movieId)
    {
        String body = "{" +
                "\"media_id\": \"" + movieId + "\"" +
                "}";
        return postRequest("/" + listId + "/add_item", body);
    }

    public Response clearList(int listId)
    {
        return postRequestClear("/" + listId + "/clear", "");
    }

    public Response deleteList(int listId)
    {
        return deleteRequest("/" + listId,"");
    }
}
