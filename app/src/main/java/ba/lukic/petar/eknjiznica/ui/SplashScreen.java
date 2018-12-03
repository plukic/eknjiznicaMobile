package ba.lukic.petar.eknjiznica.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import javax.inject.Inject;

import ba.lukic.petar.eknjiznica.base.BaseDaggerActivity;
import ba.lukic.petar.eknjiznica.data.account.IAccountRepo;
import ba.lukic.petar.eknjiznica.ui.home.HomeActivity;
import ba.lukic.petar.eknjiznica.ui.login.LoginActivity;
import ba.lukic.petar.eknjiznica.util.ISchedulersProvider;
import io.reactivex.disposables.Disposable;

public class SplashScreen extends BaseDaggerActivity {
    @Inject
    IAccountRepo accountRepo;
    @Inject
    ISchedulersProvider schedulersProvider;
    private Disposable subscribe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        subscribe = accountRepo.isUserLogged()
                .subscribeOn(schedulersProvider.network())
                .observeOn(schedulersProvider.main())
                .subscribe(isLogged -> {
                    if (isLogged)
                        goToHomeScreen();
                    else {
                        goToLoginScreen();
                    }
                }, throwable -> {
                    goToLoginScreen();
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscribe != null)
            subscribe.dispose();
    }

    private void goToHomeScreen() {
        Intent intent = HomeActivity.getInstance(this);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void goToLoginScreen() {
        Intent instance = LoginActivity.getInstance(SplashScreen.this);
        instance.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(instance);
    }


    public static Intent getInstance(Context context) {
        return new Intent(context, SplashScreen.class);
    }
}
