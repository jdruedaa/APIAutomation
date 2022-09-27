package org.tmdb.utils;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.tmdb.controllers.ListController;

import java.util.List;

public class ListUtils {
    public int extractListId(Response response)
    {
        JsonPath jsonPath = new JsonPath(response.getBody().asString());
        return jsonPath.get("list_id");
    }

    public void deleteTestLists(List<Integer> listIds)
    {
        ListController listController = new ListController();
        for(int listId:listIds)
        {
            listController.deleteList(listId);
        }
    }
}
