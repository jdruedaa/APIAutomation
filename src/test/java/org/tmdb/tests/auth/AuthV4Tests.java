package org.tmdb.tests.auth;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.tmdb.controllers.AuthV4Controller;
import org.tmdb.utils.AuthUtils;

public class AuthV4Tests {

    private AuthUtils authUtils;

    @BeforeTest
    public void initializeAuthUtils()
    {
        authUtils = new AuthUtils();
    }
    @Test
    public void CreateRequestTokenTest()
    {
        AuthV4Controller authV4Controller = new AuthV4Controller();
        Response response = authV4Controller.createRequestToken();
        Assert.assertEquals(response.getStatusCode(),200);
        String requestToken = authUtils.extractRequestToken(response);
        Assert.assertTrue(!requestToken.isEmpty());
        System.out.println("https://www.themoviedb.org/auth/access?request_token=" + requestToken);
    }

    @Test
    public void CreateAccessTokenTest()
    {
        AuthV4Controller authV4Controller = new AuthV4Controller();
        Response response = authV4Controller.createRequestToken();
        Assert.assertEquals(response.getStatusCode(),200);
        String requestToken = authUtils.extractRequestToken(response);
        //TODO Validate Request Token
        //authController.validateRequestToken(requestToken);
        response = authV4Controller.createAccessToken(requestToken);
        Assert.assertEquals(response.getStatusCode(),200);
        String accessToken = authUtils.extractAccessToken(response);
        Assert.assertTrue(!accessToken.isEmpty());
    }

    @AfterSuite
    @Test
    public void DeleteAccessTokenTest()
    {
        AuthV4Controller authV4Controller = new AuthV4Controller();
        Response response = authV4Controller.createRequestToken();
        Assert.assertEquals(response.getStatusCode(),200);
        String requestToken = authUtils.extractRequestToken(response);
        response = authV4Controller.createAccessToken(requestToken);
        Assert.assertEquals(response.getStatusCode(),200);
        String accessToken = authUtils.extractAccessToken(response);
        response = authV4Controller.deleteAccessToken(accessToken);
        Assert.assertEquals(response.getStatusCode(),200);
        JsonPath jsonPath = new JsonPath(response.getBody().asString());
        Boolean success = jsonPath.get("success");
        Assert.assertTrue(success);
    }
}
