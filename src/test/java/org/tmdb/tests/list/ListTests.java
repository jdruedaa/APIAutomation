package org.tmdb.tests.list;

import io.restassured.response.Response;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.tmdb.controllers.ListController;
import org.tmdb.models.list.TMDBList;
import org.tmdb.utils.ListUtils;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ListTests {
    private final ArrayList<Integer> testListIds = new ArrayList<>();
    private ListUtils listUtils;

    @BeforeMethod
    public void initializeListUtils()
    {
        listUtils = new ListUtils();
    }

    @Test
    @Parameters({"name", "description", "language"})
    public void CreateListTest(String name, String description, String language)
    {
        ListController listController = new ListController();
        Response response = listController.createList(name, description, language);
        assertThat(response.getStatusCode(),is(equalTo(201)));
        int listId = listUtils.extractListId(response);
        assertThat(listId,is(notNullValue()));
        testListIds.add(listId);
    }

    @Test
    public void GetListDetailsTest()
    {
        ListController listController = new ListController();
        Response response = listController.createList("Testing List 2", "none", "eng");
        assertThat(response.getStatusCode(),is(equalTo(201)));
        int listId = listUtils.extractListId(response);
        testListIds.add(listId);
        response = listController.getListDetails(listId);
        assertThat(response.getStatusCode(),is(equalTo(200)));
        TMDBList list = listController.extractListFromResponse(response);
        assertThat(list.getId(),is(notNullValue()));
    }

    @Test
    @Parameters("movieId")
    public void AddMovieToListTest(int movieId)
    {
        ListController listController = new ListController();
        Response response = listController.createList("Testing List 2", "none", "eng");
        assertThat(response.getStatusCode(),is(equalTo(201)));
        int listId = listUtils.extractListId(response);
        testListIds.add(listId);
        response = listController.getListDetails(listId);
        assertThat(response.getStatusCode(),is(equalTo(200)));
        TMDBList list = listController.extractListFromResponse(response);
        int oldItemCount = list.getItem_count();
        response = listController.addMovie(listId, movieId);
        assertThat(response.getStatusCode(),is(equalTo(201)));
        response = listController.getListDetails(listId);
        assertThat(response.getStatusCode(),is(equalTo(200)));
        list = listController.extractListFromResponse(response);
        assertThat(list.getItems(),is(not(empty())));
        assertThat(list.getItem_count(),is(greaterThan(oldItemCount)));
    }

    @Test
    public void ClearListTest()
    {
        ListController listController = new ListController();
        Response response = listController.createList("Testing List 2", "none", "eng");
        assertThat(response.getStatusCode(),is(equalTo(201)));
        int listId = listUtils.extractListId(response);
        testListIds.add(listId);
        response = listController.getListDetails(listId);
        assertThat(response.getStatusCode(),is(equalTo(200)));
        TMDBList list = listController.extractListFromResponse(response);
        int oldItemCount = list.getItem_count();
        response = listController.addMovie(listId, 550);
        assertThat(response.getStatusCode(),is(equalTo(201)));
        response = listController.getListDetails(listId);
        assertThat(response.getStatusCode(),is(equalTo(200)));
        list = listController.extractListFromResponse(response);
        assertThat(list.getItems(),is(not(empty())));
        assertThat(list.getItem_count(),is(greaterThan(oldItemCount)));
        response = listController.clearList(listId);
        assertThat(response.getStatusCode(),is(equalTo(201)));
        response = listController.getListDetails(listId);
        assertThat(response.getStatusCode(),is(equalTo(200)));
        list = listController.extractListFromResponse(response);
        assertThat(list.getItem_count(),is(equalTo(0)));
        assertThat(list.getItems(),is(empty()));
    }

    @Test
    public void DeleteListTest()
    {
        ListController listController = new ListController();
        Response response = listController.createList("Testing List 5", "none", "eng");
        assertThat(response.getStatusCode(),is(equalTo(201)));
        int listId = listUtils.extractListId(response);
        listController.deleteList(listId);
        response = listController.getListDetails(listId);
        assertThat(response.getStatusCode(),is(equalTo(404)));
        listUtils.deleteTestLists(testListIds);
    }
}
