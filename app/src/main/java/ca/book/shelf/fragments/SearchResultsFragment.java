package ca.book.shelf.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ca.book.shelf.MainActivity;
import ca.book.shelf.R;
import ca.book.shelf.adapters.StoryAdapter;
import ca.book.shelf.databinding.FragmentStoriesBinding;
import ca.book.shelf.viewmodels.MainViewModel;

public class SearchResultsFragment extends Fragment implements LoadManager{

    public static final String TAG = "SearchResultsFragment";

    private RecyclerView mRecyclerView;
    private MainViewModel mViewModel;
    private StoryAdapter mAdapter;
    private Observable.OnPropertyChangedCallback mResultsListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
    }

    @Override
    public View onCreateView(@Nullable LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentStoriesBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_stories, container, false);

        setupUi(binding.getRoot());
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        setListeners();
    }

    @Override
    public void onPause() {
        super.onPause();
        clearListeners();
    }

    private void setupUi(View rootView) {
        mRecyclerView = rootView.findViewById(R.id.stories);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new StoryAdapter(this, mViewModel.searchResults.get());
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setListeners() {
        mViewModel.searchResults.addOnPropertyChangedCallback(getSearchResultsListener());
    }

    private void clearListeners() {
        mViewModel.searchResults.removeOnPropertyChangedCallback(getSearchResultsListener());
    }

    private Observable.OnPropertyChangedCallback getSearchResultsListener() {
        if(mResultsListener == null) {
            mResultsListener = new Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(Observable sender, int propertyId) {
                    mAdapter.notifyReplaced();
                }
            };
        }

        return mResultsListener;
    }

    @Override
    public void next() {
        /**
         * No-op so that I can reuse the StoryAdapter
         */
    }
}
