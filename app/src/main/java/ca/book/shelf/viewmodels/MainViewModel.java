package ca.book.shelf.viewmodels;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import ca.book.shelf.models.Story;
import ca.book.shelf.providers.Repository;

public class MainViewModel extends ViewModel {

    private static final String TAG = "MainViewModel";

    private Repository mRepository;

    public ObservableField<ArrayList<Story>> currentStories = new ObservableField<>(new ArrayList<Story>());
    public ObservableField<ArrayList<Story>> searchResults = new ObservableField<>(new ArrayList<Story>());
    public ObservableField<Boolean> showProgress = new ObservableField<>(false);

    public void init(final Context context, Repository repository) {
        mRepository = repository;
        mRepository.getUserMessage().observe((LifecycleOwner) context, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(!TextUtils.isEmpty(s)) {
                    Toast toast = Toast.makeText(context, s, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    public void next(final LifecycleOwner owner) {
        if(!showProgress.get()) {
            showProgress.set(true);
            mRepository.fetchStories().observe(owner, new Observer<List<Story>>() {
                @Override
                public void onChanged(List<Story> catalog) {
                    /**
                     * Avoid duplicates since stories that may have been included
                     * in an earlier API response can appear in later API responses also
                    */
                    catalog.removeAll(currentStories.get());
                    if (!catalog.isEmpty()) {
                        currentStories.get().addAll(catalog);
                        currentStories.notifyChange();
                    } else {
                        Log.i(TAG, "all loaded stories are duplicates");
                        next(owner);
                    }
                    showProgress.set(false);
                }
            });
        }
    }

    public void search(LifecycleOwner owner, String query) {
        if(!TextUtils.isEmpty(query)) {
            showProgress.set(true);
            mRepository.search(query).observe(owner, new Observer<List<Story>>() {
                @Override
                public void onChanged(List<Story> results) {
                    showProgress.set(false);
                    searchResults.get().clear();
                    if (!results.isEmpty()) {
                        searchResults.get().addAll(results);
                        searchResults.notifyChange();
                    }
                }
            });
        }
    }
}
