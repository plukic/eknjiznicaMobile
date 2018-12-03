package ba.lukic.petar.eknjiznica.di;

import android.app.Application;

import javax.inject.Singleton;

import ba.lukic.petar.eknjiznica.LibraryApp;
import ba.lukic.petar.eknjiznica.di.module.ActivityBindingModule;
import ba.lukic.petar.eknjiznica.di.module.ApplicationModule;
import ba.lukic.petar.eknjiznica.di.module.DataModule;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import dagger.android.support.DaggerApplication;


@Singleton
@Component(modules = {
        ApplicationModule.class,
        ActivityBindingModule.class,
        DataModule.class,
        AndroidSupportInjectionModule.class})
public interface AppComponent extends AndroidInjector<DaggerApplication> {

    void inject(LibraryApp application);

    @Override
    void inject(DaggerApplication instance);

    // Gives us syntactic sugar. we can then do DaggerAppComponent.builder().application(this).build().inject(this);
    // never having to instantiate any modules or say which module we are passing the application to.
    // Application will just be provided into our app graph now.
    @Component.Builder
    interface Builder {

        @BindsInstance
        AppComponent.Builder application(Application application);

        AppComponent build();
    }
}
