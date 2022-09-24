package org.tmdb.tests.auth;

import io.restassured.response.Response;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.tmdb.controllers.AuthV3Controller;
import org.tmdb.utils.AuthUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class AuthV3Tests {

    private AuthUtils authUtils;

    @BeforeTest
    public void initializeAuthUtils()
    {
        authUtils = new AuthUtils();
    }
    @Test
    public void CreateRequestTokenTest()
    {
        AuthV3Controller authV3Controller = new AuthV3Controller();
        Response response = authV3Controller.createRequestToken();
        assertThat(response.getStatusCode(),is(equalTo(200)));
        String requestToken = authUtils.extractRequestToken(response);
        assertThat(requestToken,is(not(emptyString())));
    }

    @Test
    public void ValidateRequestTokenTest()
    {
        AuthV3Controller authV3Controller = new AuthV3Controller();
        Response response = authV3Controller.createRequestToken();
        assertThat(response.getStatusCode(),is(equalTo(200)));
        String requestToken = authUtils.extractRequestToken(response);
        response = authV3Controller.validateRequestToken(requestToken);
        assertThat(response.getStatusCode(),is(equalTo(200)));
    }
}
