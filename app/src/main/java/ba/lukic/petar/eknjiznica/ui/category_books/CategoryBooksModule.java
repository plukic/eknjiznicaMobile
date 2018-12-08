package ba.lukic.petar.eknjiznica.ui.category_books;

import ba.lukic.petar.eknjiznica.di.ActivityScoped;
import ba.lukic.petar.eknjiznica.ui.my_books.MyBooksActivity;
import ba.lukic.petar.eknjiznica.util.DialogFactory;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class CategoryBooksModule {

    @ActivityScoped
    @Binds
    abstract CategoryBooksContract.Presenter presenter(CategoryBooksPresenter presenter);

    @Provides
    @ActivityScoped
    static DialogFactory dialogFactory(CategoryBooksActivity categoryBooksActivity){
        return new DialogFactory(categoryBooksActivity);
    }
}
