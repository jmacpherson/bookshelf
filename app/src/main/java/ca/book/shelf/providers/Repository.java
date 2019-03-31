package ca.book.shelf.providers;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.databinding.ObservableField;
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
//    private final HashMap<String, Bookshelf> cachedStories = new HashMap<>();

    private String nextUrl = ApiConstants.INITIAL_URL;
    private String oldestStoryLoaded;

    @Inject
    public Repository(NetworkRequestRunner requestFactory, StoryProvider storyProvider, StoryDao storyDao) {
        mRequestFactory = requestFactory;
        mStoryProvider = storyProvider;
        mStoryDao = storyDao;
    }

//    public LiveData<Bookshelf> fetchStories(final String url) {
//        final MutableLiveData<Bookshelf> results = new MutableLiveData<>();
//        mRequestFactory.run(new Runnable() {
//            @Override
//            public void run() {
//                Bookshelf bookshelf = mStoryProvider.fetchStories(url);
//                cachedStories.put(bookshelf.nextUrl, bookshelf);
//                List<Story> storiesBefore = mStoryDao.loadAllStories();
//                mStoryDao.insertAll(bookshelf.stories);
//                List<Story> storiesAfter = mStoryDao.loadAllStories();
//
//                results.postValue(bookshelf);
//            }
//        });
//
//        return results;
//    }
    public LiveData<List<Story>> fetchStories() {
        final MutableLiveData<List<Story>> results = new MutableLiveData<>();
        mRequestFactory.run(new Runnable() {
            @Override
            public void run() {
                try {
                    Bookshelf bookshelf = mStoryProvider.fetchStories(nextUrl);
                    nextUrl = bookshelf.nextUrl;

                    if(TextUtils.isEmpty(oldestStoryLoaded)) {
                        oldestStoryLoaded = bookshelf.stories.get(bookshelf.stories.size() -1).id;
                    }

                    mStoryDao.insertAll(bookshelf.stories);

                    logTimestamps(bookshelf.stories);
                    results.postValue(bookshelf.stories);
                } catch (IOException ex) {
                    Log.i(TAG, "Failed to access API: " + ex.getMessage());
//                    List<String> catalog = mStoryDao.getStoriesLoadedBefore(oldestStoryLoaded);
                    List<Story> catalog = mStoryDao.getStoriesLoadedBefore(oldestStoryLoaded);
                    oldestStoryLoaded = catalog.get(catalog.size() - 1).id;

                    logTimestamps(catalog);

                    results.postValue(catalog);
                }
            }
        });

        return results;
    }

    public void logTimestamps(List<Story> stories) {
        for(Story story : stories) {
            Log.i(TAG, "Timestamped: " + story.timestamp);
        }
    }

    public Story getStory(String storyId) {
        return mStoryDao.getStory(storyId);
    }
}
