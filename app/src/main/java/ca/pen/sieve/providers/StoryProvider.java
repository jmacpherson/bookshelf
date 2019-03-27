package ca.pen.sieve.providers;

import ca.pen.sieve.models.Stories;

public interface StoryProvider {
    Stories fetchStories(String url);
}
