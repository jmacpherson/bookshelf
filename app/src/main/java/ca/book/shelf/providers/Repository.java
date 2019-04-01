package ca.book.shelf.providers;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import ca.book.shelf.R;
import ca.book.shelf.constants.ApiConstants;
import ca.book.shelf.models.Bookshelf;
import ca.book.shelf.models.Story;
import ca.book.shelf.providers.network.NetworkRequestRunner;
import ca.book.shelf.providers.network.StoryProvider;
import ca.book.shelf.providers.persistence.StoryDao;
import ca.book.shelf.utils.ToastUtils;

@Singleton
public class Repository {

    private static final String TAG = "Repository";

    private Context mApplicationContext;
    private NetworkRequestRunner mRequestFactory;
    private StoryProvider mStoryProvider;
    private StoryDao mStoryDao;
    private ToastUtils mToastUtils;

    private String nextUrl = ApiConstants.INITIAL_URL;
    private String oldestStoryLoaded;
//    private MutableLiveData<String> mUserMessage = new MutableLiveData<>();

    @Inject
    public Repository(Context applicationContext, NetworkRequestRunner requestFactory, StoryProvider storyProvider, StoryDao storyDao, ToastUtils toastUtils) {
        mApplicationContext = applicationContext;
        mRequestFactory = requestFactory;
        mStoryProvider = storyProvider;
        mStoryDao = storyDao;
        mToastUtils = toastUtils;
    }

    public LiveData<List<Story>> fetchStories() {
        final MutableLiveData<List<Story>> results = new MutableLiveData<>();
        mRequestFactory.run(new Runnable() {
            @Override
            public void run() {
                try {
                    /**
                     * Try to load from API
                     */
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
                    /**
                     * Fallback to loading from cache
                     */
                    Log.e(TAG, "Failed to access API: " + ex.getMessage());
                    mToastUtils.post(mApplicationContext.getString(R.string.failed_to_access_api));
                    List<Story> catalog = !TextUtils.isEmpty(oldestStoryLoaded)
                            ? mStoryDao.getStoriesLoadedBefore(oldestStoryLoaded)
                            : mStoryDao.getMostRecentStories();

                    ArrayList<Story> result = new ArrayList<>(catalog);
                    if(!result.isEmpty()) {
                        oldestStoryLoaded = result.get(result.size() - 1).id;
                    }

                    Log.i(TAG, "Loaded stories from DB: " + result.size());
                    results.postValue(result);
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
                    mToastUtils.post(mApplicationContext.getString(R.string.no_search_results, query));
                } else {
                    mToastUtils.post(mApplicationContext.getString(R.string.search_results, searchResults.size(), query));
                }
                Log.i(TAG, "Found results: " + searchResults.size());
                results.postValue(searchResults);
            }
        });

        return results;
    }

    public void observeUserMessages(Context context) {
        mToastUtils.observe(context);
    }
}
