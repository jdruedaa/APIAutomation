package org.tmdb.models.list;

import org.tmdb.models.movie.Movie;

import java.util.List;

public class TMDBList {
    private final String id;
    private final int item_count;
    private final List<Movie> items;

    public TMDBList(String id, int item_count, List<Movie> items) {
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

    public List<Movie> getItems() {
        return items;
    }

}
