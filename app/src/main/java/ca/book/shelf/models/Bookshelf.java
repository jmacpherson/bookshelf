package ca.book.shelf.models;

import java.util.ArrayList;
import java.util.List;

public class Bookshelf {
    public ArrayList<Story> stories = new ArrayList<>();
    public String nextUrl;

    public List<String> getCatalog() {
        ArrayList<String> catalog = new ArrayList<>();
        for(Story story: stories) {
            catalog.add(story.id);
        }

        return catalog;
    }
}
