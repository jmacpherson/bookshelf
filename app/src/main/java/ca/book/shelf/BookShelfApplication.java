package ca.book.shelf;

import android.app.Application;

import ca.book.shelf.di.BookShelfComponent;
import ca.book.shelf.di.BookShelfModule;
import ca.book.shelf.di.DaggerBookShelfComponent;

public class BookShelfApplication extends Application {

    private static BookShelfApplication sApplication;
    private BookShelfComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerBookShelfComponent.builder()
                .bookShelfModule(new BookShelfModule(this)).build();
        sApplication = this;
    }

    public static BookShelfApplication getApp() {
        return sApplication;
    }

    public BookShelfComponent getComponent() {
        return component;
    }
}
