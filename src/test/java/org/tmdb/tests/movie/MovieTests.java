package org.tmdb.tests.movie;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.tmdb.controllers.MovieController;
import org.tmdb.models.movie.Movie;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class MovieTests {

    @Test
    @Parameters("movieId")
    public void GetMovieDetailsTest(String movieId)
    {
        int movieIdInt = Integer.parseInt(movieId);
        MovieController movieController = new MovieController();
        Response response = movieController.getMovieDetails(movieIdInt);
        assertThat(response.getStatusCode(),is(equalTo(200)));
        Movie movie = movieController.extractMovieFromResponse(response);
        assertThat(movie.getId(),is(equalTo(movieIdInt)));
        assertThat(movie.getTitle(),is(not(emptyString())));
    }

    @Test
    @Parameters({"movieId", "rating"})
    public void RateMovieTest(String movieId, String rating)
    {
        int movieIdInt = Integer.parseInt(movieId);
        MovieController movieController = new MovieController();
        Response response = movieController.rateMovie(movieIdInt, Double.parseDouble(rating));
        assertThat(response.getStatusCode(),is(equalTo(201)));
        JsonPath jsonPath = new JsonPath(response.getBody().asString());
        Boolean success = jsonPath.get("success");
        assertThat(success, is(equalTo(true)));
    }
}
