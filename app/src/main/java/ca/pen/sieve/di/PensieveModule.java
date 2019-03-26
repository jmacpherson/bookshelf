package ca.pen.sieve.di;

import android.app.Application;
import android.content.Context;

import ca.pen.sieve.providers.NetworkRequestFactory;
import ca.pen.sieve.providers.StoryProvider;
import ca.pen.sieve.providers.StoryProviderImpl;
import dagger.Module;
import dagger.Provides;

@Module
public class PensieveModule {

    Application mApplication;

    public PensieveModule(Application application) {
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
    NetworkRequestFactory provideNetworkRequestFactory() {
        return NetworkRequestFactory.getInstance();
    }
}
