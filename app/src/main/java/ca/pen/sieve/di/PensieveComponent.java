package ca.pen.sieve.di;

import javax.inject.Singleton;

import ca.pen.sieve.PensieveApplication;
import dagger.Component;

@Singleton
@Component(modules = {PensieveModule.class})
public interface PensieveComponent {
}
