package ca.book.shelf.adapters;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.recyclerview.widget.RecyclerView;
import ca.book.shelf.R;
import ca.book.shelf.fragments.LoadManager;
import ca.book.shelf.models.Story;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.StoryViewHolder> {

    private LoadManager mLoadManager;
    private ArrayList<Story> mCatalog;

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
        if((mCatalog.size() - 1) == i) {
            mLoadManager.next();
        }
        Story story = mCatalog.get(i);
        Picasso.get().load(story.cover).into(viewHolder.getStoryImage());
        viewHolder.getStoryTitle().setText(story.title);
        viewHolder.getStoryAuthor().setText(story.user.name);
    }

    @Override
    public int getItemCount() {
        return mCatalog.size();
    }

    public void updateCatalog(ArrayList<Story> catalog) {
        int end = this.mCatalog.size() - 1;
        this.mCatalog = catalog;
        notifyItemInserted(end);
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
