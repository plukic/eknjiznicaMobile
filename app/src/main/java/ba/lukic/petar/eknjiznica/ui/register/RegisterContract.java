package ba.lukic.petar.eknjiznica.ui.register;

import ba.lukic.petar.eknjiznica.base.BaseAsyncView;
import ba.lukic.petar.eknjiznica.base.BasePresenter;

public interface RegisterContract {

    interface  Presenter extends BasePresenter<View> {

        boolean isValidFirstName(String value);
        boolean isValidLastName(String value);
        boolean isValidEmailAddress(String value);
        boolean isValidUsername(String value);
        boolean isValidPassword(String value);
        boolean isValidPhoneNumber(String value);

        void registerUser(String firstName, String lastName, String email, String username, String password, String phoneNumber);
    }
    interface View extends BaseAsyncView<Presenter> {

        void displayFillFormsError();

        void displayError(String error);

        void navigateToMainScreen();
    }

}
