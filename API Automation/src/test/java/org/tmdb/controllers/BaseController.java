package org.tmdb.controllers;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public interface BaseController {
    void setUp();
    RequestSpecification requestBase();
    Response getRequest(String path);
    Response postRequest(String path, String body);
    Response patchRequest(String path, String body);
    Response deleteRequest(String path);
}
