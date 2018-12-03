package ba.lukic.petar.eknjiznica.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import ba.lukic.petar.eknjiznica.R;
import ba.lukic.petar.eknjiznica.data.account.IAccountRepo;
import ba.lukic.petar.eknjiznica.ui.login.LoginActivity;
import ba.lukic.petar.eknjiznica.util.ISchedulersProvider;
import ba.lukic.petar.eknjiznica.util.MyAuthenticator;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public abstract class BaseDaggerAuthorizedActivity extends BaseDaggerActivity {
    @Inject
    EventBus eventBus;

    @Inject
     IAccountRepo accountRepo;
    @Inject
    ISchedulersProvider schedulersProvider;
    private Disposable subscribe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventBus.register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscribe != null)
            subscribe.dispose();

        eventBus.unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MyAuthenticator.UnauthorizedException exception) {
        logoutUser(getResources().getString(R.string.warning_log_out_session_expired));
    }

    protected void logoutUser(String message) {
        subscribe = accountRepo.LogoutCurrentUser()
                .subscribeOn(schedulersProvider.computation())
                .observeOn(schedulersProvider.main())
                .subscribe(() -> {
                    Intent intent;
                    if (message != null) {
                        intent = LoginActivity.getInstanceWithMessage(BaseDaggerAuthorizedActivity.this, message);
                    } else
                        intent = LoginActivity.getInstance(BaseDaggerAuthorizedActivity.this);
//
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }, Timber::e);
    }

}
