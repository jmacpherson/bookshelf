package ca.book.shelf.providers.network;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import ca.book.shelf.models.Bookshelf;

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
    public Bookshelf fetchStories(String url) throws IOException {
        Gson gson = new Gson();
        URL storyApi = new URL(url);

        HttpURLConnection connection = (HttpURLConnection) storyApi.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("accept", "application/json");

        InputStreamReader isr = new InputStreamReader(connection.getInputStream());

        Bookshelf results = gson.fromJson(isr, Bookshelf.class);

        isr.close();

        return results;
    }
}
