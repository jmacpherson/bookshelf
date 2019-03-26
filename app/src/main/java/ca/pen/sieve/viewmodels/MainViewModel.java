package ca.pen.sieve.viewmodels;

import javax.inject.Inject;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;
import ca.pen.sieve.models.BookShelf;
import ca.pen.sieve.providers.Repository;

public class MainViewModel extends ViewModel {

    Repository mRepository;

    public void init(Repository repository) {
        mRepository = repository;
    }

    ObservableField<BookShelf> bookShelf = new ObservableField<>();

    public void fetchBookshelf() {
        mRepository.fetchStories();
    }
}
