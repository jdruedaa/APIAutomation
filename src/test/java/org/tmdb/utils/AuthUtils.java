package org.tmdb.utils;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class AuthUtils {
    public String extractRequestToken(Response response)
    {
        JsonPath jsonPath = new JsonPath(response.getBody().asString());
        String requestToken = jsonPath.get("request_token");
        return requestToken;
    }
    public String extractAccessToken(Response response)
    {
        JsonPath jsonPath = new JsonPath(response.getBody().asString());
        String accessToken = jsonPath.get("access_token");
        return accessToken;
    }
}
