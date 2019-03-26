package ca.pen.sieve.providers;

import android.content.Context;

import javax.inject.Inject;
import javax.inject.Singleton;

import ca.pen.sieve.models.BookShelf;

@Singleton
public class Repository {

    NetworkRequestFactory mRequestFactory;
    StoryProvider mStoryProvider;
    static BookShelf shelf;

    @Inject
    public Repository(NetworkRequestFactory requestFactory, StoryProvider storyProvider) {
        mRequestFactory = requestFactory;
        mStoryProvider = storyProvider;
    }

    public void fetchStories() {
        mRequestFactory.run(new Runnable() {
            @Override
            public void run() {
//                shelf =
                        mStoryProvider.fetchStories();
            }
        });
    }
}
