package ca.pen.sieve.providers;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import ca.pen.sieve.models.BookShelf;
import ca.pen.sieve.models.Story;

public class StoryProviderImpl implements StoryProvider {

    public static final String TAG = "StoryProvider";

    public static StoryProvider sInstance;

    public static StoryProvider getInstance() {
        if(sInstance == null) {
            sInstance = new StoryProviderImpl();
        }
        return sInstance;
    }

    @Override
    public BookShelf fetchStories() {
        try {
            Gson gson = new Gson();
            URL storyApi = new URL("https://www.wattpad.com/api/v3/stories?offset=0&limit=30&fields=stories(id,title,cover,user)");

            HttpURLConnection connection = (HttpURLConnection) storyApi.openConnection();
            InputStreamReader isr = new InputStreamReader(connection.getInputStream());

            BookShelf results = gson.fromJson(isr, BookShelf.class);

//            ArrayList<Story> results = new ArrayList<>();
//            results.add(new Story());

            return results;
        } catch (IOException ex) {
            Log.i(TAG, "Exception fetching stories: " + ex.getMessage());
            return null;
        }
    }
}
