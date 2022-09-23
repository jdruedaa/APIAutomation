package org.tmdb.tests.auth;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.tmdb.controllers.AuthController;

public class AuthTests {
    @Test
    public void CreateRequestTokenTest()
    {
        AuthController authController = new AuthController();
        Response response = authController.createRequestToken();
        Assert.assertEquals(response.getStatusCode(),200);
        JsonPath jsonPath = new JsonPath(response.getBody().asString());
        String request_token = jsonPath.get("request_token");
        Assert.assertTrue(!request_token.isEmpty());
    }
}
