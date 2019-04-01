package ca.book.shelf.providers;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadFactory;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import ca.book.shelf.R;
import ca.book.shelf.TestUtils;
import ca.book.shelf.constants.ApiConstants;
import ca.book.shelf.models.Bookshelf;
import ca.book.shelf.models.Story;
import ca.book.shelf.models.User;
import ca.book.shelf.providers.network.NetworkRequestRunner;
import ca.book.shelf.providers.network.StoryProvider;
import ca.book.shelf.providers.persistence.StoryDao;
import ca.book.shelf.utils.ToastUtils;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mock;
import mockit.MockUp;
import mockit.integration.junit4.JMockit;
import static org.junit.Assert.*;

@RunWith(JMockit.class)
public class RepositoryTest {

    Repository repository;

    @Injectable
    Context mockContext;

    @Injectable
    StoryProvider mockStoryProvider;

    @Injectable
    StoryDao mockStoryDao;

    @Injectable
    ThreadFactory mockThreadFactory;

    @Injectable
    ToastUtils mockToastUtils;

    @Before
    public void setUp() {
        TestUtils.mockLogging();
        TestUtils.mockTextUtils();
        mockLiveData();
        repository = new Repository(mockContext, new MockRequestRunner(mockThreadFactory), mockStoryProvider, mockStoryDao, mockToastUtils);
    }

    @Test
    public void fetchStories_online_test() {
        try {
            final String mockNextUrl1 = "http://mockNextUrl/1",
                mockNextUrl2 = "http://mockNextUrl/2";

            final Bookshelf bookshelf1 = constructBookshelf(10, mockNextUrl1);
            final Bookshelf bookshelf2 = constructBookshelf(5, mockNextUrl2);

            new Expectations() {{
                mockStoryProvider.fetchStories(ApiConstants.INITIAL_URL);
                times = 1;
                result = bookshelf1;

                mockStoryProvider.fetchStories(mockNextUrl1);
                times = 1;
                result = bookshelf2;

                mockStoryProvider.fetchStories(mockNextUrl2);
                times = 0;
            }};

            LiveData<List<Story>> result = repository.fetchStories();

            assertEquals(bookshelf1.stories, result.getValue());

            result = repository.fetchStories();

            assertEquals(bookshelf2.stories, result.getValue());
        } catch(IOException ex) {
            assertFalse(true);
        }
    }

    @Test
    public void fetchStories_online_then_offline_test() {
        try {
            final String mockNextUrl1 = "http://mockNextUrl/1",
                mockNextUrl2 = "http://mockNextUrl/2",
                failureMessage = "Failed to access API, loading from cache";

            final Bookshelf bookshelf1 = constructBookshelf(10, mockNextUrl1);
            final Bookshelf bookshelf2 = constructBookshelf(5, mockNextUrl2);

            new Expectations() {{
                mockStoryProvider.fetchStories(ApiConstants.INITIAL_URL);
                times = 1;
                result = bookshelf1;

                mockStoryProvider.fetchStories(mockNextUrl1);
                times = 1;
                result = new IOException();

                mockStoryDao.getStoriesLoadedBefore(bookshelf1.stories.get(bookshelf1.stories.size() -1).id);
                times = 1;
                result = bookshelf2.stories;

                mockStoryProvider.fetchStories(mockNextUrl2);
                times = 0;

                mockContext.getString(R.string.failed_to_access_api);
                times = 1;
                result = failureMessage;

                mockToastUtils.post(failureMessage);
                times = 1;
            }};

            LiveData<List<Story>> result = repository.fetchStories();

            assertEquals(bookshelf1.stories, result.getValue());

            result = repository.fetchStories();

            assertEquals(bookshelf2.stories, result.getValue());
        } catch(IOException ex) {
            assertFalse(true);
        }
    }

    @Test
    public void fetchStories_offline_test() {
        try {
            final String mockNextUrl1 = "http://mockNextUrl/1",
                mockNextUrl2 = "http://mockNextUrl/2",
                failureMessage = "Failed to access API, loading from cache";

            final Bookshelf bookshelf1 = constructBookshelf(10, mockNextUrl1);
            final Bookshelf bookshelf2 = constructBookshelf(5, mockNextUrl2);

            new Expectations() {{
                mockStoryProvider.fetchStories((String) any);
                result = new IOException();

                mockStoryDao.getMostRecentStories();
                times = 1;
                result = bookshelf1.stories;

                mockStoryDao.getStoriesLoadedBefore(bookshelf1.stories.get(bookshelf1.stories.size() -1).id);
                times = 1;
                result = bookshelf2.stories;

                mockContext.getString(R.string.failed_to_access_api);
                times = 2;
                result = failureMessage;

                mockToastUtils.post(failureMessage);
                times = 2;
            }};

            LiveData<List<Story>> result = repository.fetchStories();

            assertEquals(bookshelf1.stories, result.getValue());

            result = repository.fetchStories();

            assertEquals(bookshelf2.stories, result.getValue());
        } catch(IOException ex) {
            assertFalse(true);
        }
    }

    @Test
    public void search_results_found_test() {
        final String mockSearchTerm = "mockSearchTerm",
                resultsFoundMessage = "Found 10 results for: " + mockSearchTerm;
        final ArrayList<Story> stories = constructStories(10);

        new Expectations() {{
            mockStoryDao.searchStoryByTitle("%" + mockSearchTerm + "%");
            times = 1;
            result = stories;

            mockContext.getString(R.string.search_results, stories.size(), mockSearchTerm);
            times = 1;
            result = resultsFoundMessage;

            mockToastUtils.post(resultsFoundMessage );
            times = 1;
        }};

        LiveData<List<Story>> result = repository.search(mockSearchTerm);

        assertEquals(stories, result.getValue());
    }

    @Test
    public void search_no_results_found_test() {
        final String mockSearchTerm = "mockSearchTerm",
                noResultsFoundMessage = "No results found for: " + mockSearchTerm;
        final ArrayList<Story> stories = constructStories(0);

        new Expectations() {{
            mockStoryDao.searchStoryByTitle("%" + mockSearchTerm + "%");
            times = 1;
            result = stories;

            mockContext.getString(R.string.no_search_results, mockSearchTerm);
            times = 1;
            result = noResultsFoundMessage;

            mockToastUtils.post(noResultsFoundMessage);
            times = 1;
        }};

        LiveData<List<Story>> result = repository.search(mockSearchTerm);

        assertEquals(stories, result.getValue());
    }

    private Bookshelf constructBookshelf(int numberOfStories, String nextUrl) {
        Bookshelf bookshelf = new Bookshelf();

        bookshelf.stories = constructStories(numberOfStories);
        bookshelf.nextUrl = nextUrl;

        return bookshelf;
    }

    private ArrayList<Story> constructStories(int numberOfStories) {
        ArrayList<Story> stories = new ArrayList<>();
        for(int i = 0; i < numberOfStories; i++) {
            Story story = new Story();
            story.title = "mockTitle" + i;
            story.id = "" + i;
            story.timestamp = 1000000L + i;
            story.cover = "https://mockCover.randomTLD";

            User user = new User();
            user.name = "mockUser" + i;
            user.fullname = "mockFullName" + i;
            user.avatar = "https://mockAvatar.randomTLD";

            story.user = user;

            stories.add(story);
        }
        return stories;
    }

    public void mockLiveData() {
        new MockUp<MutableLiveData<List<Story>>>() {
            ArrayList<Story> storedValue;

            @Mock
            public void setValue(ArrayList<Story> value) {
                storedValue = (ArrayList) value;
            }

            @Mock
            public void postValue(List<Story> value) {
                storedValue = (ArrayList) value;
            }

            @Mock
            public ArrayList<Story> getValue() {
                return storedValue;
            }
        };
    }

    class MockRequestRunner extends NetworkRequestRunner {

        public MockRequestRunner(ThreadFactory threadFactory) {
            super(threadFactory);
        }

        @Override
        public void run(Runnable runnable) {
            runnable.run();
        }
    }
}
