package ba.lukic.petar.eknjiznica.di.module;

import ba.lukic.petar.eknjiznica.di.ActivityScoped;
import ba.lukic.petar.eknjiznica.ui.book_details.BookDetailsActivity;
import ba.lukic.petar.eknjiznica.ui.SplashScreen;
import ba.lukic.petar.eknjiznica.ui.book_details.BookDetailsModule;
import ba.lukic.petar.eknjiznica.ui.categories.CategoriesModule;
import ba.lukic.petar.eknjiznica.ui.category_books.CategoryBooksActivity;
import ba.lukic.petar.eknjiznica.ui.category_books.CategoryBooksModule;
import ba.lukic.petar.eknjiznica.ui.favorite_books.FavoriteModule;
import ba.lukic.petar.eknjiznica.ui.home.HomeActivity;
import ba.lukic.petar.eknjiznica.ui.login.LoginActivity;
import ba.lukic.petar.eknjiznica.ui.login.LoginModule;
import ba.lukic.petar.eknjiznica.ui.my_books.MyBooksActivity;
import ba.lukic.petar.eknjiznica.ui.my_books.MyBooksModule;
import ba.lukic.petar.eknjiznica.ui.profile.ProfileActivity;
import ba.lukic.petar.eknjiznica.ui.profile.ProfileModule;
import ba.lukic.petar.eknjiznica.ui.recommended.RecommendedModule;
import ba.lukic.petar.eknjiznica.ui.register.RegisterActivity;
import ba.lukic.petar.eknjiznica.ui.register.RegisterModule;
import ba.lukic.petar.eknjiznica.ui.shopping_cart.ShoppingCartActivity;
import ba.lukic.petar.eknjiznica.ui.shopping_cart.ShoppingCartModule;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector()
    abstract SplashScreen splashScreenActivity();


    @ActivityScoped
    @ContributesAndroidInjector(modules = LoginModule.class)
    abstract LoginActivity loginActivity();


    @ActivityScoped
    @ContributesAndroidInjector(modules = RegisterModule.class)
    abstract RegisterActivity registerActivity();


    @ActivityScoped
    @ContributesAndroidInjector(modules = {RecommendedModule.class,CategoriesModule.class,FavoriteModule.class})
    abstract HomeActivity homeActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = ProfileModule.class)
    abstract ProfileActivity profileActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = BookDetailsModule.class)
    abstract BookDetailsActivity BookDetailsActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = ShoppingCartModule.class)
    abstract ShoppingCartActivity ShoppingCartActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = MyBooksModule.class)
    abstract MyBooksActivity MyBooksActivity ();

    @ActivityScoped
    @ContributesAndroidInjector(modules = CategoryBooksModule.class)
    abstract CategoryBooksActivity CategoryBooksActivity();



}
