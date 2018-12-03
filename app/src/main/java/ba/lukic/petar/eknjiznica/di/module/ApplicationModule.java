package ba.lukic.petar.eknjiznica.di.module;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.inject.Named;
import javax.inject.Singleton;


import ba.lukic.petar.eknjiznica.BuildConfig;
import ba.lukic.petar.eknjiznica.data.shared_prefs.SharedPreferenceHelper;
import ba.lukic.petar.eknjiznica.data.shared_prefs.SharedPrefsRepo;
import ba.lukic.petar.eknjiznica.util.BaseErrorFactory;
import ba.lukic.petar.eknjiznica.util.DateAndTimeUtil;
import ba.lukic.petar.eknjiznica.util.DateTimeTypeAdapter;
import ba.lukic.petar.eknjiznica.util.ISchedulersProvider;
import ba.lukic.petar.eknjiznica.util.MyAuthenticator;
import ba.lukic.petar.eknjiznica.util.MyInterceptor;
import ba.lukic.petar.eknjiznica.util.MyRegex;
import ba.lukic.petar.eknjiznica.util.SchedulersProvider;
import ba.lukic.petar.eknjiznica.util.ServicesUtil;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import okhttp3.Authenticator;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


@Module
public abstract class ApplicationModule {


    //expose Application as an injectable context
    @Binds
    abstract Context bindContext(Application application);


    @Provides
    static SharedPreferences provideSharedPrefs(Application application) {
        return application.getSharedPreferences("PREF_EKNJIZNICA", Context.MODE_PRIVATE);
    }

    @Binds
    @Singleton
    abstract ISchedulersProvider schedulersProvider(SchedulersProvider schedulersProvider);

    @Provides
    @Singleton
    static DateTimeFormatter dateTimeFormatter(){
        return DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss");
    }

    @Provides
    @Singleton
    static Gson gson(DateTimeFormatter dateTimeFormatter) {
        return new GsonBuilder()
                .registerTypeAdapter(DateTime.class, new DateTimeTypeAdapter(dateTimeFormatter))
                .create();
    }

    @Provides
    @Singleton
    static SharedPrefsRepo sharedPrefsRepo(SharedPreferences sharedPreferences, Gson gson) {
        return new SharedPrefsRepo(new SharedPreferenceHelper(sharedPreferences), gson);
    }



    @Provides
    @Singleton
    static ServicesUtil servicesUtil(){
        return new ServicesUtil();
    }


    @Binds
    @Singleton
    abstract Interceptor authInterceptor(MyInterceptor myInterceptor);


    @Provides
    @Singleton
    static EventBus providesEventBus() {
        return EventBus.getDefault();
    }

    @Provides
    @Singleton
    static Authenticator providesAuthenticator(SharedPrefsRepo sharedPrefsRepo, EventBus eventBus,@Named("client_id") String clientId) {
        return new MyAuthenticator(sharedPrefsRepo, eventBus, clientId);
    }

    @Provides
    @Singleton
    static OkHttpClient providesOkHttp(Interceptor interceptor, Authenticator authenticator) {
        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .authenticator(authenticator)
                .build();
    }


    @Provides
    @Singleton
    static Retrofit providesRetrofit(OkHttpClient okHttpClient, Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }


    @Provides
    @Singleton
    static BaseErrorFactory baseErrorFactory(Gson gson, Context ctx) {
        return new BaseErrorFactory(gson, ctx.getResources());
    }

    @Provides
    @Singleton
    static MyRegex myRegex() {
        return new MyRegex();
    }

    @Provides
    @Singleton
    static DateAndTimeUtil dateAndTimeUtil() {
        return new DateAndTimeUtil();
    }




}
