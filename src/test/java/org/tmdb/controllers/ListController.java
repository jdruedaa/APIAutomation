package org.tmdb.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmdb.models.list.TMDBList;
import org.tmdb.utils.AuthUtils;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class ListController implements BaseController {
    private String apiKey;

    private String authorizationHeader;

    private String sessionId;

    private Gson gson;

    private final Logger log = LoggerFactory.getLogger(ListController.class);

    private String basePath;

    public ListController()
    {
        setUp();
    }

    @Override
    public void setUp() {
        log.info("Performing setup for new instance...");
        requestAuthorizedSession();
        try {
            JsonObject paths = JsonParser
                    .parseReader(new FileReader("src/test/java/resources/pagePaths.json"))
                    .getAsJsonObject();
            basePath = paths.get("list").getAsString();
        } catch (FileNotFoundException e) {
            log.error("File {} not found.", "pagePaths.json");
        }
        apiKey = System.getenv("MovieDB_API_Key");
        authorizationHeader = System.getenv("MovieDB_Read_Access_Token");
        gson = new Gson();
        log.info("Setup finished.");
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

    public void requestAuthorizedSession()
    {
        log.info("Requesting authorized session...");
        AuthUtils authUtils = new AuthUtils();
        sessionId = authUtils.createAuthorizedSession();
        log.info("Received authorized session.");
    }

    public Response getListDetails(int listId)
    {
        log.info("Requesting list details with id {}...", listId);
        Response response = getRequest(basePath + "/" + listId);
        log.info("Received list details for id {}.", listId);
        return response;
    }

    public TMDBList extractListFromResponse(Response response)
    {
        log.info("Extracting list from response...");
        TMDBList tmdbList;
        tmdbList = gson.fromJson(response.getBody().asString(), TMDBList.class);
        log.info("List extracted from response.");
        return tmdbList;
    }

    public Response createList(String name, String description, String language)
    {
        log.info("Requesting to create list {}...", name);
        String body = "{" +
                "\"name\": \"" + name + "\",\n" +
                "\"description\": \"" + description + "\",\n" +
                "\"language\": \"" + language + "\"" +
                "}";
        Response response = postRequest(basePath + "", body);
        log.info("List {} created.", name);
        return response;
    }

    public Response addMovie(int listId, int movieId)
    {
        log.info("Adding movie with id {} to list with id {}...", movieId, listId);
        String body = "{" +
                "\"media_id\": \"" + movieId + "\"" +
                "}";
        Response response = postRequest(basePath + "/" + listId + "/add_item", body);
        log.info("Added movie with id {} to list with id {}.", movieId, listId);
        return response;
    }

    public Response clearList(int listId)
    {
        log.info("Clearing list with id {}...", listId);
        Response response = postRequestClear(basePath + "/" + listId + "/clear", "");
        log.info("Cleared list with id {}.", listId);
        return response;
    }

    public Response deleteList(int listId)
    {
        log.info("Deleting list with id {}...", listId);
        Response response = deleteRequest(basePath + "/" + listId,"");
        log.info("Deleted list with id {}.", listId);
        return response;
    }
}
