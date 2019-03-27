package ca.pen.sieve.providers;

import ca.pen.sieve.models.Bookshelf;

public interface StoryProvider {
    Bookshelf fetchStories(String url);
}
