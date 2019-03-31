package ca.book.shelf.viewmodels;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import ca.book.shelf.models.Story;
import ca.book.shelf.providers.Repository;

public class MainViewModel extends ViewModel {

    Repository mRepository;

//    public ObservableField<Bookshelf> currentStories = new ObservableField<>();
//    public ObservableField<ArrayList<String>> currentStories = new ObservableField<>(new ArrayList<String>());
    public ObservableField<ArrayList<Story>> currentStories = new ObservableField<>(new ArrayList<Story>());
    public ObservableField<Boolean> showProgress = new ObservableField<>(false);

    public void init(Repository repository) {
        mRepository = repository;
    }

//    public LiveData<Bookshelf> fetchBookshelf() {
////        return mRepository.fetchStories();
//    }

//    public void next(LifecycleOwner owner) {
//        showProgress.set(true);
//        mRepository.fetchStories().observe(owner, new Observer<Bookshelf>() {
//            @Override
//            public void onChanged(Bookshelf bookshelf) {
//                if (bookshelf != null) {
//                    currentStories.set(bookshelf);
//                    showProgress.set(false);
//                }
//            }
//        });
//    }
    public void next(LifecycleOwner owner) {
        showProgress.set(true);
        mRepository.fetchStories().observe(owner, new Observer<List<Story>>() {
            @Override
            public void onChanged(List<Story> catalog) {
                currentStories.get().addAll(catalog);
                currentStories.notifyChange();
                showProgress.set(false);
            }
        });
    }

//    public Story getStory(String storyId) {
//        return mRepository.getStory(storyId);
//    }
}
