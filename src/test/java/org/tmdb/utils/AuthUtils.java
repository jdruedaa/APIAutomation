package org.tmdb.utils;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.tmdb.controllers.AuthV3Controller;

public class AuthUtils {
    public String extractRequestToken(Response response)
    {
        JsonPath jsonPath = new JsonPath(response.getBody().asString());
        return jsonPath.get("request_token");
    }

    public String extractSessionId(Response response)
    {
        JsonPath jsonPath = new JsonPath(response.getBody().asString());
        return jsonPath.get("session_id");
    }

    public String extractAccessToken(Response response)
    {
        JsonPath jsonPath = new JsonPath(response.getBody().asString());
        return jsonPath.get("access_token");
    }

    public String createAuthorizedSession()
    {
        AuthV3Controller authV3Controller = new AuthV3Controller();
        Response response = authV3Controller.createRequestToken();
        String requestToken = extractRequestToken(response);
        authV3Controller.validateRequestToken(requestToken);
        response = authV3Controller.createSession(requestToken);
        return extractSessionId(response);
    }
}
