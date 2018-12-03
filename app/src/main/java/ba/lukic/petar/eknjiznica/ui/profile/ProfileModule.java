package ba.lukic.petar.eknjiznica.ui.profile;

import ba.lukic.petar.eknjiznica.di.ActivityScoped;
import ba.lukic.petar.eknjiznica.ui.register.RegisterActivity;
import ba.lukic.petar.eknjiznica.ui.register.RegisterContract;
import ba.lukic.petar.eknjiznica.ui.register.RegisterPresenter;
import ba.lukic.petar.eknjiznica.util.DialogFactory;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class ProfileModule {

    @Provides
    @ActivityScoped
    static DialogFactory dialogFactory(ProfileActivity profileActivity){
        return new DialogFactory(profileActivity);
    }

    @Binds
    @ActivityScoped
    abstract ProfileContract.Presenter presenter(ProfilePresenter profilePresenter);


}
