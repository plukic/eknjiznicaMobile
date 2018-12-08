package ba.lukic.petar.eknjiznica.ui.shopping_cart;

import ba.lukic.petar.eknjiznica.di.ActivityScoped;
import ba.lukic.petar.eknjiznica.util.DialogFactory;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class ShoppingCartModule {



    @Binds
    @ActivityScoped
    abstract  ShoppingCartContract.Presenter presenter(ShoppingCartPresenter presenter);


    @Provides
    @ActivityScoped
    static DialogFactory dialogFactory(ShoppingCartActivity activity){
        return new DialogFactory(activity);
    }
}
