package org.tmdb.controllers;

import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.tmdb.models.movie.Movie;

public class ListController implements BaseController{
    private String apiKey;

    private String authorizationHeader;

    private Gson gson;

    public ListController()
    {
        setUp();
    }

    @Override
    public void setUp() {
        RestAssured.baseURI = "https://api.themoviedb.org/3/movie/";
        apiKey = System.getenv("MovieDB_API_Key");
        authorizationHeader = System.getenv("MovieDB_Read_Access_Token");
        gson = new Gson();
    }

    @Override
    public RequestSpecification requestBase() {
        return RestAssured.given()
                .contentType("application/json;charset=utf-8")
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
                .log().all()
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

    public Response getListDetails(int movieId)
    {
        return getRequest("" + movieId);
    }

    public Movie extractListFromResponse(Response response)
    {
        Movie movie;
        movie = gson.fromJson(response.getBody().asString(), Movie.class);
        return movie;
    }

    public Response rateMovie(int movieId, double ratingValue)
    {
        String body = "{" +
                "\"value\": " + ratingValue +
                "}";
        Response response = postRequest(movieId + "/rating", body);
        return response;
    }
}
