package ca.book.shelf.di;

import javax.inject.Singleton;

import ca.book.shelf.MainActivity;
import dagger.Component;

@Singleton
@Component(modules = {BookShelfModule.class})
public interface BookShelfComponent {
    void inject(MainActivity mainActivity);
}
