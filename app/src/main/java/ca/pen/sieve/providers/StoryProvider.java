package ca.pen.sieve.providers;

import java.util.Collection;

import ca.pen.sieve.models.BookShelf;
import ca.pen.sieve.models.Story;

public interface StoryProvider {
    BookShelf fetchStories();
}
