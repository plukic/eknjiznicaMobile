package ba.lukic.petar.eknjiznica.ui.register;

import javax.inject.Inject;

import ba.lukic.petar.eknjiznica.data.account.IAccountRepo;
import ba.lukic.petar.eknjiznica.model.user.ClientAddVM;
import ba.lukic.petar.eknjiznica.util.BaseErrorFactory;
import ba.lukic.petar.eknjiznica.util.ISchedulersProvider;
import ba.lukic.petar.eknjiznica.util.MyRegex;
import io.reactivex.disposables.CompositeDisposable;

public class RegisterPresenter implements RegisterContract.Presenter {
    @Inject

    MyRegex myRegex;
    @Inject
    IAccountRepo accountRepo;

    @Inject
    BaseErrorFactory baseErrorFactory;
    @Inject
    ISchedulersProvider schedulersProvider;


    private RegisterContract.View view;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Inject
    public RegisterPresenter(MyRegex myRegex) {
        this.myRegex = myRegex;
    }

    @Override
    public boolean isValidFirstName(String value) {
        return myRegex.isValidFirstName(value);
    }

    @Override
    public boolean isValidLastName(String value) {
        return myRegex.isValidLastName(value);
    }

    @Override
    public boolean isValidEmailAddress(String value) {
        return myRegex.isValidEmail(value);
    }

    @Override
    public boolean isValidUsername(String value) {
        return myRegex.isValidUsername(value);
    }

    @Override
    public boolean isValidPassword(String value) {
        return myRegex.isValidPassword(value);
    }

    @Override
    public boolean isValidPhoneNumber(String value) {
        return myRegex.isValidPhoneNumber(value);
    }

    private boolean areFieldsValid(String firstName, String lastName, String email, String username, String password, String phoneNumber) {
        return isValidFirstName(firstName) && isValidLastName(lastName) && isValidEmailAddress(email) && isValidUsername(username) && isValidPassword(password) && isValidPhoneNumber(phoneNumber);
    }

    @Override
    public void registerUser(String firstName, String lastName, String email, String username, String password, String phoneNumber) {
        boolean formStatus = areFieldsValid(firstName, lastName, email, username, password, phoneNumber);
        if (!formStatus) {
            view.displayFillFormsError();
            return;
        }
        ClientAddVM clientCreateModel = new ClientAddVM();
        clientCreateModel.FirstName = firstName;
        clientCreateModel.LastName = lastName;
        clientCreateModel.Email = email;
        clientCreateModel.UserName = username;
        clientCreateModel.Password = password;
        clientCreateModel.PhoneNumber = phoneNumber;


        compositeDisposable.add(
                accountRepo.registerUser(clientCreateModel)
                        .andThen(accountRepo.loginUser(clientCreateModel.UserName, clientCreateModel.Password))
                        .flatMap(authResponse -> accountRepo.loadUserProfileInfo())
                        .subscribeOn(schedulersProvider.network())
                        .observeOn(schedulersProvider.main())
                        .doOnSubscribe(disposable -> view.displayLoading(true))
                        .subscribe(clientProfileData -> {
                            view.displayLoading(false);
                            view.navigateToMainScreen();
                        }, throwable -> {
                            view.displayLoading(false);
                            String error = baseErrorFactory.parseSingleError(throwable, "create_client");
                            view.displayError(error);
                        }));

    }


    @Override
    public void takeView(RegisterContract.View view) {

        this.view = view;
    }

    @Override
    public void dropView() {
        this.view = null;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {
        compositeDisposable.dispose();
    }
}
