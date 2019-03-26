package ca.pen.sieve.models;

import java.util.ArrayList;

public class BookShelf {
    ArrayList<Story> shelf = new ArrayList();

    public void putStory(Story story) {
        shelf.add(story);
    }
}
