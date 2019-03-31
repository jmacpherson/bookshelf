package ca.book.shelf.providers.persistence;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import ca.book.shelf.models.Story;

@Dao
public interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Story> stories);

    @Query("SELECT * FROM story ORDER BY timestamp DESC")
    List<Story> loadAllStories();

//    @Query("SELECT id FROM story WHERE timestamp > :oldestTimestampInView ORDER BY timestamp DESC")
//    @Query("SELECT id FROM story WHERE timestamp > (SELECT timestamp FROM story WHERE id = :storyId) ORDER BY timestamp DESC LIMIT 10")
//    List<String> getStoriesLoadedBefore(String storyId);
    @Query("SELECT * FROM story WHERE timestamp > (SELECT timestamp FROM story WHERE id = :storyId) ORDER BY timestamp DESC LIMIT 10")
    List<Story> getStoriesLoadedBefore(String storyId);

    @Query("SELECT * FROM story WHERE id = :storyId")
    Story getStory(String storyId);
}
