package ba.lukic.petar.eknjiznica.di.module;

import android.app.Application;

import javax.inject.Named;
import javax.inject.Singleton;

import ba.lukic.petar.eknjiznica.R;
import ba.lukic.petar.eknjiznica.data.account.AccountRepo;
import ba.lukic.petar.eknjiznica.data.account.IAccountRepo;
import ba.lukic.petar.eknjiznica.data.api.IApiService;
import ba.lukic.petar.eknjiznica.data.books.BookRepo;
import ba.lukic.petar.eknjiznica.data.books.IBookRepo;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public abstract class DataModule {

    @Singleton
    @Provides
    static IApiService providesApi(Retrofit retrofit){
        return retrofit.create(IApiService.class);
    }

    @Singleton
    @Provides
    @Named("client_id")
    static String providesClientId(Application app){
        return app.getResources().getString(R.string.client_id);
    }


    @Singleton
    @Binds
    abstract IAccountRepo providesAccountRepo(AccountRepo accountRepo);

    @Singleton
    @Binds
    abstract IBookRepo providesBookRepo(BookRepo bookRepo);


}
