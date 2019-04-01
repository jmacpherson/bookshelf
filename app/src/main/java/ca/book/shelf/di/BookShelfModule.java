package ca.book.shelf.di;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;
import ca.book.shelf.providers.network.NetworkRequestRunner;
import ca.book.shelf.providers.network.StoryProvider;
import ca.book.shelf.providers.network.StoryProviderImpl;
import ca.book.shelf.providers.persistence.StoryDao;
import ca.book.shelf.providers.persistence.StoryDatabase;
import ca.book.shelf.utils.ToastUtils;
import dagger.Module;
import dagger.Provides;

@Module
public class BookShelfModule {

    Application mApplication;
    StoryDatabase db;

    public BookShelfModule(Application application) {
        mApplication = application;
    }

    @Provides
    Context provideContext() {
        return mApplication.getApplicationContext();
    }

    @Provides
    StoryProvider provideStoryProvider() {
        return StoryProviderImpl.getInstance();
    }

    @Provides
    NetworkRequestRunner provideNetworkRequestRunner() {
        return NetworkRequestRunner.getInstance();
    }

    @Provides
    StoryDao provideStoryDao() {
        if(db == null) {
            db = Room.databaseBuilder(mApplication.getApplicationContext(), StoryDatabase.class, StoryDatabase.TAG).build();
        }
        return db.storyDao();
    }

    @Provides
    ToastUtils provideToastUtils() {
        return ToastUtils.getInstance();
    }
}
