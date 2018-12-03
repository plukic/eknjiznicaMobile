package ba.lukic.petar.eknjiznica.ui.register;

import ba.lukic.petar.eknjiznica.di.ActivityScoped;
import ba.lukic.petar.eknjiznica.util.DialogFactory;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class RegisterModule {

    @Provides
    @ActivityScoped
    static DialogFactory dialogFactory(RegisterActivity registerActivity){
        return new DialogFactory(registerActivity);
    }

    @Binds
    @ActivityScoped
    abstract RegisterContract.Presenter presenter(RegisterPresenter registerPresenter);


}
