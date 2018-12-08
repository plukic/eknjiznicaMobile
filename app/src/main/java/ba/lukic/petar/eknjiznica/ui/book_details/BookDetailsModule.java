package ba.lukic.petar.eknjiznica.ui.book_details;

import ba.lukic.petar.eknjiznica.di.ActivityScoped;
import ba.lukic.petar.eknjiznica.util.DialogFactory;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class BookDetailsModule {

    @ActivityScoped
    @Binds
    abstract BookDetailsContract.Presenter presenter(BookDetailsPresenter presenter);

    @Provides
    @ActivityScoped
    static DialogFactory dialogFactory(BookDetailsActivity bookDetailsActivity){
        return new DialogFactory(bookDetailsActivity);
    }
}
