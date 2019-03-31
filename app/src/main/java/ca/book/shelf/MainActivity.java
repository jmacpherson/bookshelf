package ca.book.shelf;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.SearchView;

import java.util.ArrayList;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;
import ca.book.shelf.databinding.ActivityMainBinding;
import ca.book.shelf.fragments.NavigationManager;
import ca.book.shelf.fragments.SearchResultsFragment;
import ca.book.shelf.models.Story;
import ca.book.shelf.providers.Repository;
import ca.book.shelf.viewmodels.MainViewModel;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Inject
    Repository mRepository;

    MainViewModel mViewModel;
    SearchView mSearchView;
    NavigationManager mNavigationManager;
    Observable.OnPropertyChangedCallback mSearchResultsListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        BookShelfApplication.getApp().getComponent().inject(this);
        mViewModel.init(this, mRepository);

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setModel(mViewModel);

        if(mSearchView == null) {
            mSearchView = binding.getRoot().findViewById(R.id.search);
        }

        if(mNavigationManager == null) {
            mNavigationManager = new NavigationManager(getSupportFragmentManager(), R.id.fragment_placeholder);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mNavigationManager.showFeed();
    }

    @Override
    public void onResume() {
        super.onResume();

        setListeners();

    }

    @Override
    public void onPause() {
        super.onPause();
        mSearchView.setOnQueryTextListener(null);
        clearListeners();
    }

    private void setListeners() {
        final LifecycleOwner owner = this;
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.i(TAG, "Searching: " + query);
                mViewModel.search(owner, query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i(TAG, "Typing: " + newText);
                return false;
            }
        });

        mViewModel.searchResults.addOnPropertyChangedCallback(getSearchResultsListener());
    }

    private void clearListeners() {
        mSearchView.setOnQueryTextListener(null);
        mViewModel.searchResults.removeOnPropertyChangedCallback(getSearchResultsListener());
    }

    private Observable.OnPropertyChangedCallback getSearchResultsListener() {
        if(mSearchResultsListener == null) {
            mSearchResultsListener = new Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(Observable sender, int propertyId) {
                    if(!((ObservableField<ArrayList<Story>>) sender).get().isEmpty()) {
                        if(!mNavigationManager.getTopFragment().equals(SearchResultsFragment.TAG)) {
                            mNavigationManager.showSearchResults();
                        }
                    } else {
                        mNavigationManager.showFeed();
                    }
                }
            };
        }

        return mSearchResultsListener;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                clearSearch();
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void clearSearch() {
        mViewModel.searchResults.get().clear();
        mSearchView.setQuery("", false);
        mSearchView.setIconified(true);
    }
}
