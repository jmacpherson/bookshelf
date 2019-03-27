package ca.pen.sieve.providers;

import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import ca.pen.sieve.models.Bookshelf;

@Singleton
public class Repository {

    NetworkRequestRunner mRequestFactory;
    StoryProvider mStoryProvider;
    final HashMap<String, Bookshelf> cachedStories = new HashMap<>();

    @Inject
    public Repository(NetworkRequestRunner requestFactory, StoryProvider storyProvider) {
        mRequestFactory = requestFactory;
        mStoryProvider = storyProvider;
    }

    public LiveData<Bookshelf> fetchStories(final String url) {
        final MutableLiveData<Bookshelf> results = new MutableLiveData<>();
        mRequestFactory.run(new Runnable() {
            @Override
            public void run() {
                Bookshelf bookshelf = mStoryProvider.fetchStories(url);
                cachedStories.put(bookshelf.nextUrl, bookshelf);
                results.postValue(bookshelf);
            }
        });

        return results;
    }
}
