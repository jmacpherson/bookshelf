package ca.pen.sieve.providers;

import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import ca.pen.sieve.models.Stories;

@Singleton
public class Repository {

    NetworkRequestRunner mRequestFactory;
    StoryProvider mStoryProvider;
    static HashMap<String, Stories> cachedStories = new HashMap<>();

    @Inject
    public Repository(NetworkRequestRunner requestFactory, StoryProvider storyProvider) {
        mRequestFactory = requestFactory;
        mStoryProvider = storyProvider;
    }

    public LiveData<Stories> fetchStories(final String url) {
        final MutableLiveData<Stories> results = new MutableLiveData<>();
        mRequestFactory.run(new Runnable() {
            @Override
            public void run() {
                Stories stories = mStoryProvider.fetchStories(url);
                cachedStories.put(stories.nextUrl, stories);
                results.postValue(stories);
            }
        });

        return results;
    }
}
