package ca.book.shelf.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ca.book.shelf.R;
import ca.book.shelf.adapters.StoryAdapter;
import ca.book.shelf.databinding.FragmentStoriesBinding;
import ca.book.shelf.viewmodels.MainViewModel;

import static ca.book.shelf.providers.network.NetworkRequestRunner.TAG;

public class StoriesFragment extends Fragment implements LoadManager {

    private RecyclerView mRecyclerView;
    private MainViewModel mViewModel;
    private StoryAdapter mAdapter;
    private Observable.OnPropertyChangedCallback mListener;
    private LoadManager mLoadManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoadManager = this;
        mViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
    }

    @Override
    public View onCreateView(@Nullable LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentStoriesBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_stories, container, false);

        setupUi(binding.getRoot());

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        mViewModel.currentStories.removeOnPropertyChangedCallback(getCatalogListener());
        super.onDestroyView();
    }

    private void setupUi(View rootView) {
        mRecyclerView = rootView.findViewById(R.id.stories);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new StoryAdapter(this, mViewModel.currentStories.get());
        mRecyclerView.setAdapter(mAdapter);

        mViewModel.currentStories.addOnPropertyChangedCallback(getCatalogListener());
    }

    public Observable.OnPropertyChangedCallback getCatalogListener() {
        if(mListener == null) {
            mListener = new Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(Observable sender, int propertyId) {
//                    mAdapter.updateCatalog(mViewModel.currentStories.get());
                    mAdapter = new StoryAdapter(mLoadManager, mViewModel.currentStories.get());
                    mRecyclerView.setAdapter(mAdapter);
                }
            };
        }

        return mListener;
    }

    @Override
    public void next() {
        Log.i(TAG, "Loading more stories");
        mViewModel.next(this);
    }
}
