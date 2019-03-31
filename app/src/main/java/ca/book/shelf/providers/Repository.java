package ca.book.shelf.providers;

import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import ca.book.shelf.constants.ApiConstants;
import ca.book.shelf.models.Bookshelf;
import ca.book.shelf.models.Story;
import ca.book.shelf.providers.network.NetworkRequestRunner;
import ca.book.shelf.providers.network.StoryProvider;
import ca.book.shelf.providers.persistence.StoryDao;

@Singleton
public class Repository {

    private static final String TAG = "Repository";

    private NetworkRequestRunner mRequestFactory;
    private StoryProvider mStoryProvider;
    private StoryDao mStoryDao;

    private String nextUrl = ApiConstants.INITIAL_URL;
    private String oldestStoryLoaded;
    private MutableLiveData<String> mUserMessage = new MutableLiveData<>();

    @Inject
    public Repository(NetworkRequestRunner requestFactory, StoryProvider storyProvider, StoryDao storyDao) {
        mRequestFactory = requestFactory;
        mStoryProvider = storyProvider;
        mStoryDao = storyDao;
    }

    public LiveData<List<Story>> fetchStories() {
        final MutableLiveData<List<Story>> results = new MutableLiveData<>();
        mRequestFactory.run(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i(TAG, "Loading stories from API");
                    Bookshelf bookshelf = mStoryProvider.fetchStories(nextUrl);
                    nextUrl = bookshelf.nextUrl;

                    if(TextUtils.isEmpty(oldestStoryLoaded)) {
                        oldestStoryLoaded = bookshelf.stories.get(bookshelf.stories.size() -1).id;
                    }

                    mStoryDao.insertAll(bookshelf.stories);

                    Log.i(TAG, "Loaded stories from API: " + bookshelf.stories.size());
                    results.postValue(bookshelf.stories);
                } catch (IOException ex) {
                    Log.i(TAG, "Failed to access API: " + ex.getMessage());
                    mUserMessage.postValue("Failed to access API, loading from cache");
                    List<Story> catalog = !TextUtils.isEmpty(oldestStoryLoaded)
                            ? mStoryDao.getStoriesLoadedBefore(oldestStoryLoaded)
                            : mStoryDao.getMostRecentStories();

                    if(!catalog.isEmpty()) {
                        oldestStoryLoaded = catalog.get(catalog.size() - 1).id;
                    }

                    Log.i(TAG, "Loaded stories from DB: " + catalog.size());
                    results.postValue(catalog);
                }
            }
        });

        return results;
    }

    public MutableLiveData<List<Story>> search(final String query) {
        final MutableLiveData<List<Story>> results = new MutableLiveData<>();
        mRequestFactory.run(new Runnable() {
            @Override
            public void run() {
                String queryRegex = "%" + query + "%";
                List<Story> searchResults = mStoryDao.searchStoryByTitle(queryRegex);
                if(searchResults.isEmpty()) {
                    mUserMessage.postValue("No results found for: " + query);
                } else {
                    mUserMessage.postValue("Found " + searchResults.size() + " results for: " + query);
                }
                Log.i(TAG, "Found results: " + searchResults.size());
                results.postValue(searchResults);
            }
        });

        return results;
    }

    public MutableLiveData<String> getUserMessage() {
        return mUserMessage;
    }
}
