package ba.lukic.petar.eknjiznica.di.module;

import ba.lukic.petar.eknjiznica.di.ActivityScoped;
import ba.lukic.petar.eknjiznica.ui.SplashScreen;
import ba.lukic.petar.eknjiznica.ui.home.HomeActivity;
import ba.lukic.petar.eknjiznica.ui.login.LoginActivity;
import ba.lukic.petar.eknjiznica.ui.login.LoginModule;
import ba.lukic.petar.eknjiznica.ui.profile.ProfileActivity;
import ba.lukic.petar.eknjiznica.ui.profile.ProfileModule;
import ba.lukic.petar.eknjiznica.ui.register.RegisterActivity;
import ba.lukic.petar.eknjiznica.ui.register.RegisterModule;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector()
    abstract SplashScreen splashScreenActivity();


    @ActivityScoped
    @ContributesAndroidInjector(modules = LoginModule.class)
    abstract LoginActivity loginActivity();


    @ActivityScoped
    @ContributesAndroidInjector(modules = RegisterModule.class)
    abstract RegisterActivity registerActivity();


    @ActivityScoped
    @ContributesAndroidInjector()
    abstract HomeActivity homeActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = ProfileModule.class)
    abstract ProfileActivity profileActivity();



}
