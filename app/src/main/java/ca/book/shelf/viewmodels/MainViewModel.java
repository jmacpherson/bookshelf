package ca.book.shelf.viewmodels;

import android.util.Log;

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
    public ObservableField<Boolean> showProgress = new ObservableField<>(false);

    public void init(Repository repository) {
        mRepository = repository;
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
                    if (catalog.size() > 0) {
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
}
