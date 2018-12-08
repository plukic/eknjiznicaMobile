package ba.lukic.petar.eknjiznica.ui.my_books;

import ba.lukic.petar.eknjiznica.di.ActivityScoped;
import ba.lukic.petar.eknjiznica.util.DialogFactory;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class MyBooksModule {

    @ActivityScoped
    @Binds
    abstract MyBooksContract.Presenter presenter(MyBooksPresenter presenter);

    @Provides
    @ActivityScoped
    static DialogFactory dialogFactory(MyBooksActivity myBooksActivity){
        return new DialogFactory(myBooksActivity);
    }
}
