package ca.book.shelf.models;

import java.time.Instant;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Story {
    @PrimaryKey
    @NonNull
    public String id;

    @ColumnInfo
    public String title;
    @Embedded
    public User user;
    @ColumnInfo
    public String cover;
    @ColumnInfo
    public long timestamp = new Date().getTime();


    /**
     * Override equals so that comparator operations will
     * only consider story ID
     *
     * @param otherStory
     * @return
     */
    @Override
    public boolean equals(Object otherStory) {
        if(!(otherStory instanceof Story)) {
            return false;
        } else {
            return this.id.equals(((Story) otherStory).id);
        }
    }
}
