package ba.lukic.petar.eknjiznica.ui.recommended;

import ba.lukic.petar.eknjiznica.di.ActivityScoped;
import ba.lukic.petar.eknjiznica.di.FragmentScoped;
import ba.lukic.petar.eknjiznica.ui.home.HomeActivity;
import ba.lukic.petar.eknjiznica.util.DialogFactory;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class RecommendedModule {


    @FragmentScoped
    @ContributesAndroidInjector
    abstract RecommendedBooksFragment recommendedBooksFragment();

    @Binds
    @ActivityScoped
    abstract RecommededBooksContract.Presenter presenter(RecommendedBooksPresenter presenter);


    @Provides
    @ActivityScoped
    static DialogFactory dialogFactory(HomeActivity activity){
        return new DialogFactory(activity);
    };
}
