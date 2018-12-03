package ba.lukic.petar.eknjiznica.ui.login;

import ba.lukic.petar.eknjiznica.di.ActivityScoped;
import ba.lukic.petar.eknjiznica.util.DialogFactory;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class LoginModule {

    @ActivityScoped
    @Provides
    static DialogFactory dialogFactory(LoginActivity loginActivity){
        return new DialogFactory(loginActivity);
    }

    @ActivityScoped
    @Binds
    abstract LoginContract.Presenter presenter(LoginPresenter presenter);
}
