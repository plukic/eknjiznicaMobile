package ba.lukic.petar.eknjiznica.ui.profile;

import ba.lukic.petar.eknjiznica.base.BaseAsyncView;
import ba.lukic.petar.eknjiznica.base.BasePresenter;

public interface ProfileContract {

    interface  Presenter extends BasePresenter<View> {

        boolean isValidFirstName(String value);
        boolean isValidLastName(String value);
        boolean isValidEmailAddress(String value);
        boolean isValidUsername(String value);
        boolean isValidPhoneNumber(String value);


        void updateUser(String firstName, String lastName, String email, String phoneNumber);
    }
    interface View extends BaseAsyncView<Presenter> {

        void displayFillFormsError();

        void displayError(String error);

        void displayUpdateSuccessfully();

    }

}
