package org.tmdb.tests.movie;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.tmdb.controllers.MovieController;
import org.tmdb.models.movie.Movie;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.emptyString;

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
        /*
        Response response = movieController.getMovieDetails(movieIdInt);
        assertThat(response.getStatusCode(),is(equalTo(200)));
        Movie movie = movieController.extractMovieFromResponse(response);
        int oldVoteCount = movie.getVote_count();
        double oldVoteAverage = movie.getVote_average();
         */
        Response response = movieController.rateMovie(movieIdInt, Double.parseDouble(rating));
        assertThat(response.getStatusCode(),is(equalTo(201)));
        JsonPath jsonPath = new JsonPath(response.getBody().asString());
        Boolean success = jsonPath.get("success");
        assertThat(success, is(equalTo(true)));
        /*
        response = movieController.getMovieDetails(movieIdInt);
        assertThat(response.getStatusCode(),is(equalTo(200)));
        movie = movieController.extractMovieFromResponse(response);
        assertThat(movie.getVote_count(),is(greaterThan(oldVoteCount)));
        assertThat(movie.getVote_average(),is(not(equalTo(oldVoteAverage))));
         */
    }
}
