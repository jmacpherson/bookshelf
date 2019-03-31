package ca.book.shelf.providers.persistence;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import ca.book.shelf.models.Story;

@Database(entities = {Story.class}, version = 1, exportSchema = false)
public abstract class StoryDatabase extends RoomDatabase {
    public static final String TAG = "StoryDatabase";

    public abstract StoryDao storyDao();
}
