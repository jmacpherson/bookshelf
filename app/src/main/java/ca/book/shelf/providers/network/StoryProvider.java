package ca.book.shelf.providers.network;

import java.io.IOException;

import ca.book.shelf.models.Bookshelf;

public interface StoryProvider {
    Bookshelf fetchStories(String url) throws IOException;
}
