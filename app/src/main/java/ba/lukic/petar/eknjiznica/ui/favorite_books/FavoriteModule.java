package ba.lukic.petar.eknjiznica.ui.favorite_books;

import ba.lukic.petar.eknjiznica.di.ActivityScoped;
import ba.lukic.petar.eknjiznica.di.FragmentScoped;
import ba.lukic.petar.eknjiznica.ui.home.HomeActivity;
import ba.lukic.petar.eknjiznica.ui.recommended.RecommededBooksContract;
import ba.lukic.petar.eknjiznica.ui.recommended.RecommendedBooksFragment;
import ba.lukic.petar.eknjiznica.ui.recommended.RecommendedBooksPresenter;
import ba.lukic.petar.eknjiznica.util.DialogFactory;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FavoriteModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract FavoriteBooksFragment favoriteBooksFragment();

    @Binds
    @ActivityScoped
    abstract FavoriteBooksContract.Presenter presenter(FavoriteBooksPresenter presenter);

}
