package ca.pen.sieve.viewmodels;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import ca.pen.sieve.constants.ApiConstants;
import ca.pen.sieve.models.Stories;
import ca.pen.sieve.providers.Repository;

public class MainViewModel extends ViewModel {

    Repository mRepository;

    ObservableField<Stories> currentStories = new ObservableField<>();
    ObservableField<Boolean> showProgress = new ObservableField<>(false);

    public void init(Repository repository) {
        mRepository = repository;
    }

//    public LiveData<Stories> fetchBookshelf() {
////        return mRepository.fetchStories();
//    }

    public void next(LifecycleOwner owner) {
        String url = ApiConstants.INITIAL_URL;

        if(currentStories.get() != null && currentStories.get().nextUrl != null) {
            url = currentStories.get().nextUrl;
        }

        showProgress.set(true);
        mRepository.fetchStories(url).observe(owner, new Observer<Stories>() {
            @Override
            public void onChanged(Stories stories) {
                if (stories != null) {
                    currentStories.set(stories);
                    showProgress.set(false);
                }
            }
        });
    }

}
