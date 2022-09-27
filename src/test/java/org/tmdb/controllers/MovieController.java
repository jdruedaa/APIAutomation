package org.tmdb.controllers;

import com.google.gson.Gson;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmdb.models.movie.Movie;

public class MovieController implements BaseController{
    private String apiKey;

    private String authorizationHeader;

    private Gson gson;

    private final Logger log = LoggerFactory.getLogger(MovieController.class);

    public MovieController()
    {
        setUp();
    }

    @Override
    @Step("Set Up MovieController")
    public void setUp() {
        log.info("Performing setup for new MovieController...");
        RestAssured.baseURI = "https://api.themoviedb.org/3/movie/";
        apiKey = System.getenv("MovieDB_API_Key");
        authorizationHeader = System.getenv("MovieDB_Read_Access_Token");
        gson = new Gson();
        log.info("Setup for new MovieController finished.");
    }

    @Override
    @Step("Creating Base Request")
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

    @Step("Get movie details")
    public Response getMovieDetails(int movieId)
    {
        log.info("Requesting movie details for movieId: " + movieId + " ...");
        Response response = getRequest("" + movieId);
        log.info("Response received for movie details for movieId: " + movieId + ".");
        return response;
    }

    @Step("Extract movie from response")
    public Movie extractMovieFromResponse(Response response)
    {
        Movie movie;
        log.info("Extracting movie POJO from response...");
        movie = gson.fromJson(response.getBody().asString(), Movie.class);
        log.info("Movie Pojo extracted from response.");
        return movie;
    }

    @Step("Rate movie")
    public Response rateMovie(int movieId, double ratingValue)
    {
        String body = "{" +
                "\"value\": " + ratingValue +
                "}";
        log.info("Rating movie with id " + movieId + " with rating " + ratingValue + "...");
        Response response = postRequest(movieId + "/rating", body);
        log.info("Movie with id " + movieId + "rated with rating " + ratingValue + ".");
        return response;
    }
}
