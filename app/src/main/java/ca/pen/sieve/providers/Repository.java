package ca.pen.sieve.providers;

import android.content.Context;

import javax.inject.Inject;
import javax.inject.Singleton;

import ca.pen.sieve.models.BookShelf;

@Singleton
public class Repository {

    Context mApplicationContext;
    StoryProvider mStoryProvider;

    @Inject
    public Repository(Context context, StoryProvider storyProvider) {
        mApplicationContext = context;
        mStoryProvider = storyProvider;
    }

    public BookShelf fetchStories() {
        return mStoryProvider.fetchStories();
    }
}
