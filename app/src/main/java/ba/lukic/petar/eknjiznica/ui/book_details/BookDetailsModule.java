package ba.lukic.petar.eknjiznica.ui.book_details;

import ba.lukic.petar.eknjiznica.di.ActivityScoped;
import dagger.Binds;
import dagger.Module;

@Module
public abstract class BookDetailsModule {

    @ActivityScoped
    @Binds
    abstract BookDetailsContract.Presenter presenter(BookDetailsPresenter presenter);
}
