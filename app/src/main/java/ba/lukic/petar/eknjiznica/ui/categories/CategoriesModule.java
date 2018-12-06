package ba.lukic.petar.eknjiznica.ui.categories;

import android.support.v7.app.AppCompatDelegate;

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
public abstract class CategoriesModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract CategoriesFragment categoriesFragment();

    @Binds
    @ActivityScoped
    abstract CategoriesContract.Presenter presenter(CategoriesPresenter presenter);




}
