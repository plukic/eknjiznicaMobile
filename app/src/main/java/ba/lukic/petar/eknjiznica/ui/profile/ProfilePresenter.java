package ba.lukic.petar.eknjiznica.ui.profile;

import javax.inject.Inject;

import ba.lukic.petar.eknjiznica.data.account.IAccountRepo;
import ba.lukic.petar.eknjiznica.model.user.ClientUpdateVM;
import ba.lukic.petar.eknjiznica.util.BaseErrorFactory;
import ba.lukic.petar.eknjiznica.util.ISchedulersProvider;
import ba.lukic.petar.eknjiznica.util.MyRegex;
import io.reactivex.disposables.CompositeDisposable;

public class ProfilePresenter implements ProfileContract.Presenter {
    @Inject

    MyRegex myRegex;
    @Inject
    IAccountRepo accountRepo;

    @Inject
    BaseErrorFactory baseErrorFactory;
    @Inject
    ISchedulersProvider schedulersProvider;


    private ProfileContract.View view;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Inject
    public ProfilePresenter(MyRegex myRegex) {
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
    public boolean isValidPhoneNumber(String value) {
        return myRegex.isValidPhoneNumber(value);
    }

    private boolean areFieldsValid(String firstName, String lastName, String email,  String phoneNumber) {
        return isValidFirstName(firstName) && isValidLastName(lastName) && isValidEmailAddress(email) &&  isValidPhoneNumber(phoneNumber);
    }

    @Override
    public void updateUser(String firstName, String lastName, String email, String phoneNumber) {
        boolean formStatus = areFieldsValid(firstName, lastName, email, phoneNumber);
        if (!formStatus) {
            view.displayFillFormsError();
            return;
        }
        ClientUpdateVM clientUpdateVM = new ClientUpdateVM();
        clientUpdateVM.FirstName = firstName;
        clientUpdateVM.LastName = lastName;
        clientUpdateVM.Email = email;
        clientUpdateVM.PhoneNumber = phoneNumber;


        compositeDisposable.add(
                accountRepo.updateUser(clientUpdateVM)
                        .subscribeOn(schedulersProvider.network())
                        .observeOn(schedulersProvider.main())
                        .doOnSubscribe(disposable -> view.displayLoading(true))
                        .subscribe(() -> {
                            view.displayLoading(false);
                            view.displayUpdateSuccessfully();
                        }, throwable -> {
                            view.displayLoading(false);
                            String error = baseErrorFactory.parseSingleError(throwable, "profile_update");
                            view.displayError(error);
                        }));

    }


    @Override
    public void takeView(ProfileContract.View view) {
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
