package org.tmdb.models.list;

import org.tmdb.models.movie.Movie;

import java.util.ArrayList;

public class TMDBList {
    private final String id;
    private final int item_count;
    private final ArrayList<Movie> items;

    public TMDBList(String id, int item_count, ArrayList<Movie> items) {
        this.id = id;
        this.item_count = item_count;
        this.items = items;
    }

    public String getId() {
        return id;
    }

    public int getItem_count() {
        return item_count;
    }

    public ArrayList<Movie> getItems() {
        return items;
    }

}
