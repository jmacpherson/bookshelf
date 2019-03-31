package ca.book.shelf.viewmodels;

import java.util.ArrayList;
import java.util.List;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import ca.book.shelf.models.Story;
import ca.book.shelf.providers.Repository;

public class MainViewModel extends ViewModel {

    Repository mRepository;

    public ObservableField<ArrayList<Story>> currentStories = new ObservableField<>(new ArrayList<Story>());
    public ObservableField<Boolean> showProgress = new ObservableField<>(false);

    public void init(Repository repository) {
        mRepository = repository;
    }

    public void next(LifecycleOwner owner) {
        showProgress.set(true);
        mRepository.fetchStories().observe(owner, new Observer<List<Story>>() {
            @Override
            public void onChanged(List<Story> catalog) {
                if(catalog.size() > 0) {
                    currentStories.get().addAll(catalog);
                    currentStories.notifyChange();
                }
                showProgress.set(false);
            }
        });
    }
}
