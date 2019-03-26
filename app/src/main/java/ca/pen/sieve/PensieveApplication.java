package ca.pen.sieve;

import android.app.Application;

import ca.pen.sieve.di.DaggerPensieveComponent;
import ca.pen.sieve.di.PensieveComponent;
import ca.pen.sieve.di.PensieveModule;

public class PensieveApplication extends Application {

    PensieveComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerPensieveComponent.builder()
                .pensieveModule(new PensieveModule(this)).build();
    }
}
