package ca.book.shelf.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ca.book.shelf.R;
import ca.book.shelf.fragments.LoadManager;
import ca.book.shelf.models.Story;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.StoryViewHolder> {

    private static final String TAG = "StoryAdapter";

    private LoadManager mLoadManager;
    private ArrayList<Story> mCatalog;
    private int lastLoaded;

    public StoryAdapter(LoadManager loadManager, ArrayList<Story> catalog) {
        mLoadManager = loadManager;
        mCatalog = catalog;
    }

    @Override
    public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_story, viewGroup, false);

        return new StoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final StoryViewHolder viewHolder, int i) {
        /**
         * i is 0 indexed, size() is not
         */
        if((mCatalog.size() - 1) == i) {
            lastLoaded = i;
            mLoadManager.next();
        }


        Story story = mCatalog.get(i);
        /**
         * Used to ensure loading is sequential and duplicates are eliminated
         */
        Log.d(TAG, "Binding: " + story.id + " at " + i + " has title: " + story.title);

        /**
         * Use Picasso to load images because it removes a *lot* of boilerplate,
         * and autocaches for a better offline experience (were this going to production,
         * would definitely want to look at cache trimming)
         */
        Picasso.get().load(story.cover).into(viewHolder.getStoryImage());
        viewHolder.getStoryTitle().setText(story.title);
        viewHolder.getStoryAuthor().setText(story.user.name);
    }

    @Override
    public int getItemCount() {
        return mCatalog.size();
    }

    public void notifyAppended() {
        /**
         * New stories are added after the last loaded story
         */
        notifyItemInserted(lastLoaded + 1);
    }

    public void notifyReplaced() {
        /**
         * New search results replace previous search results
         */
        notifyDataSetChanged();
    }

    public static class StoryViewHolder extends RecyclerView.ViewHolder {

        private final ImageView storyImage;
        private final TextView storyTitle;
        private final TextView storyAuthor;

        public StoryViewHolder(@NonNull View itemView) {
            super(itemView);

            storyImage = itemView.findViewById(R.id.story_image);
            storyTitle = itemView.findViewById(R.id.story_title);
            storyAuthor = itemView.findViewById(R.id.story_author);
        }

        public ImageView getStoryImage() {
            return storyImage;
        }

        public TextView getStoryTitle() {
            return storyTitle;
        }

        public TextView getStoryAuthor() {
            return storyAuthor;
        }
    }
}
