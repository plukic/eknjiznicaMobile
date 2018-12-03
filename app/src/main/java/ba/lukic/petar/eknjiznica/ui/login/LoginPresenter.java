package ba.lukic.petar.eknjiznica.ui.login;

import javax.inject.Inject;

import ba.lukic.petar.eknjiznica.data.account.IAccountRepo;
import ba.lukic.petar.eknjiznica.util.BaseErrorFactory;
import ba.lukic.petar.eknjiznica.util.ISchedulersProvider;
import ba.lukic.petar.eknjiznica.util.MyRegex;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class LoginPresenter implements LoginContract.Presenter {
    private LoginContract.View view;

    @Inject
    MyRegex myRegex;

    @Inject
    IAccountRepo accountRepo;
    @Inject
    ISchedulersProvider schedulersProvider;

    @Inject
    BaseErrorFactory baseErrorFactory;

    CompositeDisposable compositeDisposable=new CompositeDisposable();

    @Inject
    public LoginPresenter(MyRegex myRegex, IAccountRepo accountRepo, ISchedulersProvider schedulersProvider, BaseErrorFactory baseErrorFactory) {
        this.myRegex = myRegex;
        this.accountRepo = accountRepo;
        this.schedulersProvider = schedulersProvider;
        this.baseErrorFactory = baseErrorFactory;
    }

    @Override
    public boolean isValidPassword(String password) {
        return myRegex.isValidPassword(password);
    }

    @Override
    public boolean isValidUsername(String username) {
        return myRegex.isValidUsername(username);
    }

    private boolean isFormInValidState(String username, String pass){
        return isValidUsername(username) && isValidPassword(pass);
    }
    @Override
    public void signIn(String username, String password) {
        if(!isFormInValidState(username,password))
        {
            view.displayPleaseFillInForm();
            return;
        }

        Disposable subscribe = accountRepo.loginUser(username, password)
                .flatMap(authenticationResponse -> accountRepo.loadUserProfileInfo())
                .subscribeOn(schedulersProvider.network())
                .observeOn(schedulersProvider.main())
                .doOnSubscribe(disposable -> view.displayLoading(true))
                .subscribe(authenticationResponse -> {
                    view.displayLoading(false);
                    view.navigateToHomeScreen();

                }, throwable ->{
                    view.displayLoading(false);

                    String error = baseErrorFactory.parseSingleError(throwable,"login_error");
                    view.displayLoginError(error);
                });
        compositeDisposable.add(subscribe);
    }

    @Override
    public void takeView(LoginContract.View view) {
        this.view = view;
    }

    @Override
    public void dropView() {
        this.view=null;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {
        compositeDisposable.dispose();
    }
}
